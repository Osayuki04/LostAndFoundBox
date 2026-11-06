package lostandfound.console;

import java.util.Scanner;
import lostandfound.repository.ClaimRepository;
import lostandfound.repository.ItemRepository;
import lostandfound.repository.mysql.MySqlClaimRepository;
import lostandfound.repository.mysql.MySqlItemRepository;
import lostandfound.service.LostAndFoundBox;

/**
 * Starts the console version of the lost and found app.
 * <p>
 * To start the web API instead, run {@link lostandfound.api.ApiServer}.
 *
 * @author Osayuki
 * @version 1.0
 */
public class Main {

    /** Not used: start the app with {@link #main(String[])}. */
    private Main() {
    }

    /**
     * Connects the box to the MySQL database and shows the menu.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        ItemRepository repository = new MySqlItemRepository();
        ClaimRepository claimRepository = new MySqlClaimRepository(repository);
        LostAndFoundBox box = new LostAndFoundBox("Main Reception", repository, claimRepository);

        Scanner scanner = new Scanner(System.in);
        ConsoleMenu menu = new ConsoleMenu(box, scanner);
        menu.run();
    }
}
