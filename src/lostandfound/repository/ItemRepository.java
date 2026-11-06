package lostandfound.repository;

import java.util.List;
import lostandfound.model.Item;

/**
 * Stores and loads {@link Item} objects.
 * <p>
 * This interface only says what a store must be able to do (create, read,
 * update and delete items). Each implementation decides how: for example
 * {@link InMemoryItemRepository} uses a HashMap, and
 * {@link lostandfound.repository.mysql.MySqlItemRepository} uses a database.
 * {@link lostandfound.service.LostAndFoundBox} depends only on this interface,
 * so the store can be swapped without changing the box.
 *
 * @author Osayuki
 * @version 1.0
 */
public interface ItemRepository {

    /**
     * Adds a new item to the store.
     *
     * @param item the item to store
     * @throws IllegalArgumentException if an item with the same id is already stored
     */
    void add(Item item);

    /**
     * Finds one item by its id.
     *
     * @param id the id of the item to find
     * @return the item, or {@code null} if no item has that id
     */
    Item findById(int id);

    /**
     * Returns every stored item.
     *
     * @return a new list containing all items; changing the list does not change the store
     */
    List<Item> findAll();

    /**
     * Saves changes to an item that is already stored, such as it being claimed.
     *
     * @param item the changed item
     * @throws lostandfound.exception.ItemNotFoundException if no item with that id is stored
     */
    void update(Item item);

    /**
     * Removes an item from the store.
     *
     * @param id the id of the item to remove
     * @return {@code true} if an item was removed, {@code false} if there was no item with that id
     */
    boolean delete(int id);
}
