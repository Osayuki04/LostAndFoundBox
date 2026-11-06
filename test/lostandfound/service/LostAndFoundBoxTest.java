package lostandfound.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lostandfound.exception.ItemNotFoundException;
import lostandfound.model.Item;
import lostandfound.model.Person;
import lostandfound.model.items.Electronics;
import lostandfound.model.items.Jewellery;
import lostandfound.model.items.Keys;
import lostandfound.repository.InMemoryItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link LostAndFoundBox}. Each test starts with a fresh box holding
 * one phone and two pieces of jewellery, stored in memory so no database is needed.
 *
 * @author Osayuki
 * @version 1.0
 */
public class LostAndFoundBoxTest {
    private LostAndFoundBox box;
    private Person sam;


    /** Fills a new box with three items before each test. */
    @BeforeEach
    void setUp() {
        box = new LostAndFoundBox("Test Box", new InMemoryItemRepository());
        box.addItem(new Electronics(1, "Phone", "Blue case", "Amara", LocalDate.of(2026, 10, 24), "Samsung", "SN-1"));
        box.addItem(new Jewellery(2, "Ring", "Small stone", "Lee", LocalDate.of(2026, 7, 1), "Silver", 120));
        box.addItem(new Jewellery(3, "Necklace", "Gold chain", "Tom", LocalDate.of(2026, 10, 10), "Gold", 300));
        sam = new Person(1, "Sam Okoro", "sam@wlv.ac.uk", "07700 900123");
    }

    /** Adding an item makes the count go up by one. */
    @Test
    void addItemIncreasesCount() {
        box.addItem(new Keys(4, "Keys", "Blue tag", "Amara", LocalDate.of(2026, 10, 25), 2, false));
        assertEquals(4, box.getItemCount());
    }

    /** Two items cannot share an id. */
    @Test
    void addingDuplicateIdThrowsException() {
        Item copy = new Keys(1, "Keys", "Blue tag", "Amara", LocalDate.of(2026, 10, 25), 2, false);
        assertThrows(IllegalArgumentException.class, () -> box.addItem(copy));
    }

    /** Looking up an id returns the item with that id. */
    @Test
    void findByIdReturnsTheRightItem() {
        assertEquals("Ring", box.findById(2).getName());
    }

    /** Looking up an id that does not exist throws {@link ItemNotFoundException}. */
    @Test
    void findByMissingIdThrowsItemNotFound() {
        assertThrows(ItemNotFoundException.class, () -> box.findById(99));
    }

    /** Searching ignores capital letters. */
    @Test
    void searchIgnoresCapitalLetters() {
        List<Item> results = box.searchByName("PHONE");
        assertEquals(1, results.size());
        assertEquals("Phone", results.get(0).getName());
    }

    /** Claiming an item marks it as claimed and removes it from the unclaimed list. */
    @Test
    void claimMarksItemAsClaimed() {
        box.claimItem(1, sam, LocalDate.of(2026, 10, 28));
        assertTrue(box.findById(1).isClaimed());
        assertEquals(2, box.getUnclaimedItems().size());
    }

    /** Claiming an id that does not exist throws {@link ItemNotFoundException}. */
    @Test
    void claimingMissingItemThrowsItemNotFound() {
        assertThrows(ItemNotFoundException.class, () -> box.claimItem(99, sam, LocalDate.of(2026, 10, 28)));
    }

    /** The ring (120) and necklace (300) add up to 420. */
    @Test
    void totalJewelleryValueAddsAllJewellery() {
        assertEquals(420, box.getTotalJewelleryValue());
    }

    /** The box has two jewellery items and one electronics item. */
    @Test
    void countByCategoryCountsEachType() {
        Map<String, Integer> counts = box.countByCategory();
        assertEquals(2, counts.get("Jewellery"));
        assertEquals(1, counts.get("Electronics"));
    }

    /** The ring, found in July, has been waiting the longest. */
    @Test
    void longestWaitingItemIsTheOldest() {
        assertEquals("Ring", box.getLongestWaitingItem(LocalDate.of(2026, 10, 28)).getName());
    }
}
