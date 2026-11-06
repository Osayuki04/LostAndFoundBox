package lostandfound.api;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import lostandfound.repository.ClaimRepository;
import lostandfound.repository.ItemRepository;
import lostandfound.repository.mysql.MySqlClaimRepository;
import lostandfound.repository.mysql.MySqlItemRepository;
import lostandfound.service.LostAndFoundBox;

/**
 * Starts the lost and found web API on {@code http://localhost:8080}.
 * <p>
 * Requests to {@code /items} go to {@link ItemHandler} and requests to
 * {@code /claims} go to {@link ClaimHandler}. Example requests are in
 * {@code requests.http} at the top of the project.
 *
 * @author Osayuki
 * @version 1.0
 */
public class ApiServer {
    private static final int PORT = 8080;


    /** Not used: start the server with {@link #main(String[])}. */
    private ApiServer() {
    }

    /**
     * Connects the box to the MySQL database and starts the server.
     *
     * @param args not used
     * @throws IOException if the port is already in use
     */
    public static void main(String[] args) throws IOException {
        ItemRepository repository = new MySqlItemRepository();
        ClaimRepository claimRepository = new MySqlClaimRepository(repository);
        LostAndFoundBox box = new LostAndFoundBox("Main Reception", repository, claimRepository);

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/items", new ItemHandler(box));
        server.createContext("/claims", new ClaimHandler(box));
        server.start();

        System.out.println("Server running at http://localhost:" + PORT + "/items");
        System.out.println("Press the red stop button in VS Code to stop it.");
    }
}
