package lostandfound.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lostandfound.exception.ItemNotFoundException;
import lostandfound.model.Claim;
import lostandfound.model.Item;
import lostandfound.model.Person;
import lostandfound.model.items.Jewellery;
import lostandfound.repository.ClaimRepository;
import lostandfound.repository.InMemoryClaimRepository;
import lostandfound.repository.ItemRepository;

/**
 * The lost and found box: the main logic of the application.
 * <p>
 * It adds, finds, searches and claims items, and builds reports. It does not
 * know how items are stored; it uses an {@link ItemRepository} and a
 * {@link ClaimRepository}, so the same box works with the in-memory stores
 * (used by the tests) and the MySQL stores (used by the real app).
 *
 * @author Osayuki
 * @version 1.0
 */
public class LostAndFoundBox {
    private String boxName;
    private ItemRepository repository;
    private ClaimRepository claimRepository;


    /**
     * Creates a box that uses the given item and claim stores.
     *
     * @param boxName         the name shown in the menu, for example "Main Reception"
     * @param repository      where items are stored
     * @param claimRepository where claims are stored
     */
    public LostAndFoundBox(String boxName, ItemRepository repository, ClaimRepository claimRepository) {
        this.boxName = boxName;
        this.repository = repository;
        this.claimRepository = claimRepository;
    }

    /**
     * Creates a box whose claims are only kept in memory.
     *
     * @param boxName    the name shown in the menu
     * @param repository where items are stored
     */
    public LostAndFoundBox(String boxName, ItemRepository repository) {
        this(boxName, repository, new InMemoryClaimRepository());
    }

    /**
     * Returns the box's name.
     *
     * @return the name
     */
    public String getBoxName() {
        return boxName;
    }

    /**
     * Adds a newly found item.
     *
     * @param item the item to add
     * @throws IllegalArgumentException if an item with the same id already exists
     */
    public void addItem(Item item) {
        repository.add(item);
    }

    /**
     * Finds an item by its id.
     *
     * @param id the id to look for
     * @return the item
     * @throws ItemNotFoundException if no item has that id
     */
    public Item findById(int id) {
        Item item = repository.findById(id);

        if (item == null) {
            throw new ItemNotFoundException(id);
        }
        return item;
    }

    /**
     * Changes an item's description and saves the change.
     *
     * @param id          the id of the item to change
     * @param description the new description
     * @return the changed item
     * @throws ItemNotFoundException if no item has that id
     */
    public Item updateDescription(int id, String description) {
        Item item = findById(id);
        item.setDescription(description);
        repository.update(item);
        return item;
    }

    /**
     * Removes an item.
     *
     * @param id the id of the item to remove
     * @return {@code true} if it was removed, {@code false} if there was no item with that id
     */
    public boolean removeItem(int id) {
        return repository.delete(id);
    }

    /**
     * Returns every item in the box.
     *
     * @return all items
     */
    public List<Item> getAllItems() {
        return repository.findAll();
    }

    /**
     * Finds items whose name contains some text, ignoring capital letters.
     *
     * @param text the text to search for
     * @return the matching items, or an empty list if none match
     */
    public List<Item> searchByName(String text) {
        List<Item> results = new ArrayList<>();
        for (Item item : repository.findAll()) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                results.add(item);
            }
        }
        return results;
    }

    /**
     * Returns the items that are still waiting to be claimed.
     *
     * @return the unclaimed items
     */
    public List<Item> getUnclaimedItems() {
        List<Item> results = new ArrayList<>();
        for (Item item : repository.findAll()) {
            if (!item.isClaimed()) {
                results.add(item);
            }
        }
        return results;
    }

    /**
     * Returns the unclaimed items that have passed their holding days.
     *
     * @param today the date to check against
     * @return the overdue items
     */
    public List<Item> getOverdueItems(LocalDate today) {
        List<Item> results = new ArrayList<>();
        for (Item item : repository.findAll()) {
            if (item.isOverdue(today)) {
                results.add(item);
            }
        }
        return results;
    }

    /**
     * Counts how many items there are in each category.
     *
     * @return a map from category name, for example "Keys", to the number of items in it
     */
    public Map<String, Integer> countByCategory() {
        Map<String, Integer> counts = new HashMap<>();
        for (Item item : repository.findAll()) {
            String category = item.getCategory();
            counts.put(category, counts.getOrDefault(category, 0) + 1);
        }
        return counts;
    }

    /**
     * Adds up the estimated value of all the jewellery in the box.
     *
     * @return the total value in pounds (GBP)
     */
    public int getTotalJewelleryValue() {
        int total = 0;
        for (Item item : repository.findAll()) {
            if (item instanceof Jewellery) {
                Jewellery jewellery = (Jewellery) item;
                total = total + jewellery.getEstimatedValue();
            }
        }
        return total;
    }

    /**
     * Finds the unclaimed item that has been waiting the longest.
     *
     * @param today the date to count up to
     * @return the oldest unclaimed item, or {@code null} if every item is claimed
     */
    public Item getLongestWaitingItem(LocalDate today) {
        Item longest = null;
        for (Item item : getUnclaimedItems()) {
            if (longest == null || item.getDaysWaiting(today) > longest.getDaysWaiting(today)) {
                longest = item;
            }
        }
        return longest;
    }

    /**
     * Hands an item back to its owner: marks it as claimed and records the claim.
     *
     * @param itemId   the id of the item being collected
     * @param claimant the person collecting it
     * @param date     the date it was collected
     * @return the new claim
     * @throws ItemNotFoundException  if no item has that id
     * @throws IllegalStateException  if the item has already been claimed
     */
    public Claim claimItem(int itemId, Person claimant, LocalDate date) {
        Item item = findById(itemId);
        item.markClaimed();
        Claim claim = new Claim(claimRepository.nextClaimId(), item, claimant, date);
        claimRepository.add(claim);
        repository.update(item);
        return claim;
    }

    /**
     * Returns every claim made so far.
     *
     * @return all claims
     */
    public List<Claim> getClaims() {
        return claimRepository.findAll();
    }

    /**
     * Returns how many items are in the box, claimed or not.
     *
     * @return the number of items
     */
    public int getItemCount() {
        return repository.findAll().size();
    }
}
