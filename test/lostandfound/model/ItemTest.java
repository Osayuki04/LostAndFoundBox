package lostandfound.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import lostandfound.model.items.Document;
import lostandfound.model.items.Jewellery;
import lostandfound.model.items.Keys;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Item}: claiming, overdue rules, holding days and locations.
 * {@link Keys} is used as the example item because {@link Item} is abstract.
 *
 * @author Osayuki
 * @version 1.0
 */
public class ItemTest {

    /**
     * Makes a set of keys found on the given date. Keys are kept for 30 days.
     *
     * @param dateFound the date the keys were found
     * @return a new, unclaimed set of keys
     */
    private Item makeKeys(LocalDate dateFound) {
        return new Keys(1, "House keys", "Blue tag", "Amara", dateFound, 3, true);
    }

    /** A new item starts unclaimed. */
    @Test
    void newItemIsNotClaimed() {
        Item keys = makeKeys(LocalDate.of(2026, 10, 1));
        assertFalse(keys.isClaimed());
    }

    /** Claiming an item marks it as claimed. */
    @Test
    void markClaimedSetsClaimedToTrue() {
        Item keys = makeKeys(LocalDate.of(2026, 10, 1));
        keys.markClaimed();
        assertTrue(keys.isClaimed());
    }

    /** An item cannot be claimed twice. */
    @Test
    void claimingTwiceThrowsException() {
        Item keys = makeKeys(LocalDate.of(2026, 10, 1));
        keys.markClaimed();
        assertThrows(IllegalStateException.class, () -> keys.markClaimed());
    }

    /** An item must have a name. */
    @Test
    void blankNameThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new Keys(1, "", "Blue tag", "Amara", LocalDate.of(2026, 10, 1), 3, true));
    }

    /** Keys found on 1 October are overdue on 1 November (31 days later). */
    @Test
    void itemIsOverdueAfterHoldingDays() {
        Item keys = makeKeys(LocalDate.of(2026, 10, 1));
        assertTrue(keys.isOverdue(LocalDate.of(2026, 11, 1)));
    }

    /** Keys found on 1 October are not yet overdue on day 30. */
    @Test
    void itemIsNotOverdueOnLastHoldingDay() {
        Item keys = makeKeys(LocalDate.of(2026, 10, 1));
        assertFalse(keys.isOverdue(LocalDate.of(2026, 10, 31)));
    }

    /** A claimed item is never overdue, however old it is. */
    @Test
    void claimedItemIsNeverOverdue() {
        Item keys = makeKeys(LocalDate.of(2026, 1, 1));
        keys.markClaimed();
        assertFalse(keys.isOverdue(LocalDate.of(2026, 11, 1)));
    }

    /** Different kinds of item are kept for different numbers of days. */
    @Test
    void eachSubclassHasItsOwnHoldingDays() {
        LocalDate date = LocalDate.of(2026, 10, 1);
        Item document = new Document(1, "Passport", "UK", "Lee", date, "Passport", "Sam Okoro");
        Item jewellery = new Jewellery(2, "Ring", "Silver", "Lee", date, "Silver", 120);

        assertEquals(7, document.getHoldingDays());
        assertEquals(90, jewellery.getHoldingDays());
    }

    /** Once a location is set it cannot be changed. */
    @Test
    void locationCanOnlyBeSetOnce() {
        Item keys = makeKeys(LocalDate.of(2026, 10, 1));
        keys.setLocation("Library", "2nd floor");
        assertThrows(IllegalStateException.class, () -> keys.setLocation("Gym", "Hall"));
    }
}
