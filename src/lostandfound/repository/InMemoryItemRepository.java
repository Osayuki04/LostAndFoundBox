package lostandfound.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lostandfound.exception.ItemNotFoundException;
import lostandfound.model.Item;

/**
 * An {@link ItemRepository} that keeps items in a {@link HashMap}, keyed by id.
 * <p>
 * Nothing is saved when the program stops, so this is mainly used by the tests.
 *
 * @author Osayuki
 * @version 1.0
 */
public class InMemoryItemRepository implements ItemRepository {
    private Map<Integer, Item> items;


    /**
     * Creates an empty store.
     */
    public InMemoryItemRepository() {
        this.items = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Item item) {

        if (items.containsKey(item.getId())) {
            throw new IllegalArgumentException("An item with id " + item.getId() + " already exists ");
        }
        items.put(item.getId(), item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Item findById(int id) {
        return items.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Item item) {

        if (!items.containsKey(item.getId())) {
            throw new ItemNotFoundException(item.getId());
        }
        items.put(item.getId(), item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(int id) {
        return items.remove(id) != null;
    }
}
