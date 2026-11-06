package lostandfound.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;
import lostandfound.exception.DatabaseException;
import lostandfound.exception.ItemNotFoundException;
import lostandfound.model.Claim;
import lostandfound.model.Person;
import lostandfound.service.LostAndFoundBox;

/**
 * Handles requests to {@code /claims}.
 * <ul>
 *   <li>{@code GET /claims} lists every claim</li>
 *   <li>{@code POST /claims} claims an item for a person</li>
 * </ul>
 *
 * @author Osayuki
 * @version 1.0
 */
public class ClaimHandler implements HttpHandler {
    private LostAndFoundBox box;


    /**
     * Creates the handler.
     *
     * @param box the box the requests read and change
     */
    public ClaimHandler(LostAndFoundBox box) {
        this.box = box;
    }

    /**
     * Sends each request to the right method, and turns errors into
     * 400, 404 or 500 JSON responses.
     *
     * @param exchange the request and its response
     * @throws IOException if the response cannot be written
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");

        try {
            if (parts.length == 2) {
                switch (method) {
                    case "GET" -> ApiResponse.send(exchange, 200, JsonHelper.claimsToJson(box.getClaims()));
                    case "POST" -> createClaim(exchange);
                    default -> ApiResponse.send(exchange, 405, JsonHelper.error("Use GET or POST on /claims"));
                }
            } else {
                ApiResponse.send(exchange, 404, JsonHelper.error("Unknown address " + path));
            }
        } catch (NumberFormatException e) {
            ApiResponse.send(exchange, 400, JsonHelper.error("itemId and personId must be whole numbers"));
        } catch (ItemNotFoundException e) {
            ApiResponse.send(exchange, 404, JsonHelper.error(e.getMessage()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            ApiResponse.send(exchange, 400, JsonHelper.error(e.getMessage()));
        } catch (DatabaseException e) {
            ApiResponse.LOGGER.severe("Database problem: " + e.getCause());
            ApiResponse.send(exchange, 500, JsonHelper.error("Database problem, please try again later"));
        }
    }

    /**
     * Claims the item named in the request body and replies with 201.
     * The body needs {@code itemId}, {@code personId}, {@code name} and
     * {@code email}; {@code phone} is optional.
     *
     * @param exchange the request and its response
     * @throws IOException if the body cannot be read or the response written
     */
    private void createClaim(HttpExchange exchange) throws IOException {
        Map<String, String> fields = JsonHelper.parse(readBody(exchange));

        int itemId = Integer.parseInt(fields.getOrDefault("itemId", ""));
        int personId = Integer.parseInt(fields.getOrDefault("personId", ""));
        Person claimant = new Person(personId, fields.get("name"), fields.get("email"), fields.get("phone"));

        Claim claim = box.claimItem(itemId, claimant, LocalDate.now());
        ApiResponse.send(exchange, 201, JsonHelper.claimToJson(claim));
    }

    /**
     * Reads the whole request body as text.
     *
     * @param exchange the request
     * @return the body
     * @throws IOException if it cannot be read
     */
    private String readBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
}
