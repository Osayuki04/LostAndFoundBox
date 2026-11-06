package lostandfound.console;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import lostandfound.exception.DatabaseException;
import lostandfound.exception.ItemNotFoundException;
import lostandfound.model.Claim;
import lostandfound.model.Item;
import lostandfound.model.Person;
import lostandfound.model.items.Electronics;
import lostandfound.model.items.Jewellery;
import lostandfound.model.items.Other;
import lostandfound.service.LostAndFoundBox;

/**
 * A text menu for using the lost and found box from the terminal.
 * <p>
 * It shows a numbered list of options, reads the user's choice and calls the
 * matching method on the {@link LostAndFoundBox}. Errors are printed and the
 * menu carries on, so one mistake does not stop the program.
 *
 * @author Osayuki
 * @version 1.0
 */
public class ConsoleMenu {
    private LostAndFoundBox box;
    private Scanner scanner;


    /**
     * Creates the menu.
     *
     * @param box     the box the menu works with
     * @param scanner where the user's input is read from
     */
    public ConsoleMenu(LostAndFoundBox box, Scanner scanner) {
        this.box = box;
        this.scanner = scanner;
    }

    /**
     * Shows the menu over and over until the user chooses 0 to exit.
     */
    public void run() {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");

            try {
                switch (choice) {
                    case 1 -> listAllItems();
                    case 2 -> addItem();
                    case 3 -> searchItems();
                    case 4 -> claimItem();
                    case 5 -> showOverdueItems();
                    case 6 -> showReport();
                    case 7 -> removeItem();
                    case 8 -> showClaims();
                    case 0 -> running = false;
                    default -> System.out.println("Please choose a number from the menu.");
                }
            } catch (ItemNotFoundException | IllegalArgumentException | IllegalStateException | DatabaseException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    /**
     * Prints the list of options.
     */
    private void printMenu() {
        System.out.println();
        System.out.println("=== " + box.getBoxName() + " ===");
        System.out.println("1. List all items");
        System.out.println("2. Add an item");
        System.out.println("3. Search by name");
        System.out.println("4. Claim an item");
        System.out.println("5. Show overdue items");
        System.out.println("6. Show report");
        System.out.println("7. Remove an item");
        System.out.println("8. Show claims");
        System.out.println("0. Exit");
    }

    /**
     * Prints every item in the box.
     */
    private void listAllItems() {
        List<Item> items = box.getAllItems();

        if (items.isEmpty()) {
            System.out.println("The box is empty.");
            return;
        }
        for (Item item : items) {
            System.out.println(item);
        }
    }

    /**
     * Asks for the details of a new item, then adds it.
     */
    private void addItem() {
        System.out.println("Type: 1 = Electronics, 2 = Jewellery, 3 = Other");
        int type = readInt("Choose a type: ");
        int id = readInt("Item id: ");
        String name = readText("Name: ");
        String description = readText("Description: ");
        String foundBy = readText("Found by: ");
        LocalDate today = LocalDate.now();

        Item item;
        switch (type) {
            case 1 -> {
                String brand = readText("Brand: ");
                String serialNumber = readText("Serial number: ");
                item = new Electronics(id, name, description, foundBy, today, brand, serialNumber);
            }
            case 2 -> {
                String material = readText("Material: ");
                int value = readInt("Estimated value (GBP): ");
                item = new Jewellery(id, name, description, foundBy, today, material, value);
            }
            case 3 -> {
                String category = readText("What kind of thing is it? ");
                item = new Other(id, name, description, foundBy, today, category);
            }
            default -> {
                System.out.println("Unknown type, item not added.");
                return;
            }
        }

        String building = readText("Building where found: ");
        String room = readText("Room: ");
        item.setLocation(building, room);

        box.addItem(item);
        System.out.println("Added: " + item);
    }

    /**
     * Asks for some text and prints the items whose names contain it.
     */
    private void searchItems() {
        String text = readText("Search for: ");
        List<Item> results = box.searchByName(text);

        if (results.isEmpty()) {
            System.out.println("No items match \"" + text + "\".");
            return;
        }
        for (Item item : results) {
            System.out.println(item);
        }
    }

    /**
     * Asks which item is being collected and by whom, then records the claim.
     */
    private void claimItem() {
        int itemId = readInt("Item id to claim: ");
        Item item = box.findById(itemId);
        System.out.println("Claiming: " + item.getName());

        int personId = readInt("Your person id: ");
        String name = readText("Your name: ");
        String email = readText("Your email: ");
        String phone = readText("Your phone: ");

        Person claimant = new Person(personId, name, email, phone);
        Claim claim = box.claimItem(itemId, claimant, LocalDate.now());
        System.out.println(claim);
    }

    /**
     * Prints the items that have passed their holding days.
     */
    private void showOverdueItems() {
        List<Item> overdue = box.getOverdueItems(LocalDate.now());

        if (overdue.isEmpty()) {
            System.out.println("Nothing is overdue.");
            return;
        }
        for (Item item : overdue) {
            System.out.println(item);
        }
    }

    /**
     * Prints a summary: item counts, jewellery value, the longest-waiting item
     * and how many items are in each category.
     */
    private void showReport() {
        LocalDate today = LocalDate.now();
        System.out.println("Items: " + box.getItemCount());
        System.out.println("Unclaimed: " + box.getUnclaimedItems().size());
        System.out.println("Overdue: " + box.getOverdueItems(today).size());
        System.out.println("Total jewellery value: GBP " + box.getTotalJewelleryValue());

        Item longest = box.getLongestWaitingItem(today);
        if (longest != null) {
            System.out.println("Waiting longest: " + longest.getName());
        }

        Map<String, Integer> counts = box.countByCategory();
        for (String category : counts.keySet()) {
            System.out.println("  " + category + ": " + counts.get(category));
        }
    }

    /**
     * Asks for an item id and removes that item.
     */
    private void removeItem() {
        int id = readInt("Item id to remove: ");

        if (box.removeItem(id)) {
            System.out.println("Item " + id + " removed.");
        } else {
            System.out.println("No item with id " + id + ".");
        }
    }

    /**
     * Prints every claim made so far.
     */
    private void showClaims() {
        List<Claim> claims = box.getClaims();

        if (claims.isEmpty()) {
            System.out.println("No claims yet.");
            return;
        }
        for (Claim claim : claims) {
            System.out.println(claim);
        }
    }

    /**
     * Shows a prompt and reads one line of text.
     *
     * @param prompt the text to show
     * @return what the user typed, without spaces at either end
     */
    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Shows a prompt and reads a whole number, asking again until one is typed.
     *
     * @param prompt the text to show
     * @return the number the user typed
     */
    private int readInt(String prompt) {
        while (true) {
            String input = readText(prompt);

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\"" + input + "\" is not a whole number, try again.");
            }
        }
    }
}
