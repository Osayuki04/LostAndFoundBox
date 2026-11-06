package lostandfound.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;
import lostandfound.exception.DatabaseException;
import lostandfound.exception.ItemNotFoundException;
import lostandfound.model.Item;
import lostandfound.model.items.Electronics;
import lostandfound.model.items.Jewellery;
import lostandfound.model.items.Other;
import lostandfound.service.LostAndFoundBox;

/**
 * Handles requests to {@code /items}.
 * <ul>
 *   <li>{@code GET /items} lists every item</li>
 *   <li>{@code POST /items} adds a new item</li>
 *   <li>{@code GET /items/{id}} returns one item</li>
 *   <li>{@code PUT /items/{id}} changes an item's description</li>
 *   <li>{@code DELETE /items/{id}} removes an item</li>
 * </ul>
 *
 * @author Osayuki
 * @version 1.0
 */
public class ItemHandler implements HttpHandler {
    private LostAndFoundBox box;


    /**
     * Creates the handler.
     *
     * @param box the box the requests read and change
     */
    public ItemHandler(LostAndFoundBox box) {
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
                    case "GET" -> ApiResponse.send(exchange, 200, JsonHelper.itemsToJson(box.getAllItems()));
                    case "POST" -> createItem(exchange);
                    default -> ApiResponse.send(exchange, 405, JsonHelper.error("Use GET or POST on /items"));
                }
            } else if (parts.length == 3) {
                int id = Integer.parseInt(parts[2]);
                switch (method) {
                    case "GET" -> ApiResponse.send(exchange, 200, JsonHelper.itemToJson(box.findById(id)));
                    case "PUT" -> updateItem(exchange, id);
                    case "DELETE" -> deleteItem(exchange, id);
                    default -> ApiResponse.send(exchange, 405, JsonHelper.error("Use GET, PUT or DELETE on /items/{id}"));
                }
            } else {
                ApiResponse.send(exchange, 404, JsonHelper.error("Unknown address " + path));
            }
        } catch (NumberFormatException e) {
            ApiResponse.send(exchange, 400, JsonHelper.error("The id must be a whole number"));
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
     * Adds the item described in the request body and replies with 201.
     * The {@code type} field picks Electronics, Jewellery or Other.
     *
     * @param exchange the request and its response
     * @throws IOException if the body cannot be read or the response written
     */
    private void createItem(HttpExchange exchange) throws IOException {
        Map<String, String> fields = JsonHelper.parse(readBody(exchange));

        int id = Integer.parseInt(fields.getOrDefault("id", ""));
        String type = fields.getOrDefault("type", "Other");
        String name = fields.get("name");
        String description = fields.get("description");
        String foundBy = fields.get("foundBy");
        LocalDate today = LocalDate.now();

        Item item = switch (type) {
            case "Electronics" -> new Electronics(id, name, description, foundBy, today,
                    fields.get("brand"), fields.get("serialNumber"));
            case "Jewellery" -> new Jewellery(id, name, description, foundBy, today,
                    fields.get("material"), Integer.parseInt(fields.getOrDefault("estimatedValue", "0")));
            case "Other" -> new Other(id, name, description, foundBy, today, fields.get("kind"));
            default -> throw new IllegalArgumentException("Type must be Electronics, Jewellery or Other ");
        };

        if (fields.containsKey("building")) {
            item.setLocation(fields.get("building"), fields.get("room"));
        }

        box.addItem(item);
        ApiResponse.send(exchange, 201, JsonHelper.itemToJson(item));
    }

    /**
     * Changes an item's description to the one in the request body.
     *
     * @param exchange the request and its response
     * @param id       the id of the item to change
     * @throws IOException if the body cannot be read or the response written
     */
    private void updateItem(HttpExchange exchange, int id) throws IOException {
        Map<String, String> fields = JsonHelper.parse(readBody(exchange));

        if (!fields.containsKey("description")) {
            throw new IllegalArgumentException("Send a new description, for example {\"description\": \"Red case\"}");
        }
        Item item = box.updateDescription(id, fields.get("description"));
        ApiResponse.send(exchange, 200, JsonHelper.itemToJson(item));
    }

    /**
     * Removes an item.
     *
     * @param exchange the request and its response
     * @param id       the id of the item to remove
     * @throws IOException           if the response cannot be written
     * @throws ItemNotFoundException if no item has that id
     */
    private void deleteItem(HttpExchange exchange, int id) throws IOException {

        if (box.removeItem(id)) {
            ApiResponse.send(exchange, 200, "{\"deleted\": " + id + "}");
        } else {
            throw new ItemNotFoundException(id);
        }
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
