package lostandfound.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lostandfound.model.Claim;
import lostandfound.model.Item;
import lostandfound.model.Location;

/**
 * Turns items and claims into JSON text, and reads simple JSON request bodies.
 * <p>
 * This is a small hand-written helper so the project needs no JSON library.
 * {@link #parse(String)} only handles flat objects such as
 * {@code {"name": "Phone", "id": 3}}, which is all the API needs.
 *
 * @author Osayuki
 * @version 1.0
 */
public class JsonHelper {
    private static final Pattern FIELD = Pattern.compile("\"(\\w+)\"\\s*:\\s*(\"([^\"]*)\"|[^,}\\s]+)");


    /** Not used: this class only has static methods. */
    private JsonHelper() {
    }

    /**
     * Turns one item into a JSON object.
     *
     * @param item the item
     * @return the item as JSON
     */
    public static String itemToJson(Item item) {
        Location location = item.getLocation();
        return "{"
                + "\"id\": " + item.getId() + ", "
                + "\"category\": " + text(item.getCategory()) + ", "
                + "\"name\": " + text(item.getName()) + ", "
                + "\"description\": " + text(item.getDescription()) + ", "
                + "\"foundBy\": " + text(item.getFoundBy()) + ", "
                + "\"dateFound\": " + text(item.getDateFound().toString()) + ", "
                + "\"claimed\": " + item.isClaimed() + ", "
                + "\"building\": " + text(location != null ? location.getBuilding() : null) + ", "
                + "\"room\": " + text(location != null ? location.getRoom() : null)
                + "}";
    }

    /**
     * Turns a list of items into a JSON array.
     *
     * @param items the items
     * @return the items as a JSON array
     */
    public static String itemsToJson(List<Item> items) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                json.append(", ");
            }
            json.append(itemToJson(items.get(i)));
        }
        return json.append("]").toString();
    }

    /**
     * Turns one claim into a JSON object.
     *
     * @param claim the claim
     * @return the claim as JSON
     */
    public static String claimToJson(Claim claim) {
        return "{"
                + "\"claimId\": " + claim.getClaimId() + ", "
                + "\"itemId\": " + claim.getItem().getId() + ", "
                + "\"itemName\": " + text(claim.getItem().getName()) + ", "
                + "\"claimant\": " + text(claim.getClaimant().getName()) + ", "
                + "\"email\": " + text(claim.getClaimant().getEmail()) + ", "
                + "\"claimDate\": " + text(claim.getClaimDate().toString())
                + "}";
    }

    /**
     * Turns a list of claims into a JSON array.
     *
     * @param claims the claims
     * @return the claims as a JSON array
     */
    public static String claimsToJson(List<Claim> claims) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < claims.size(); i++) {
            if (i > 0) {
                json.append(", ");
            }
            json.append(claimToJson(claims.get(i)));
        }
        return json.append("]").toString();
    }

    /**
     * Builds an error response body.
     *
     * @param message the error message
     * @return JSON of the form {@code {"error": "message"}}
     */
    public static String error(String message) {
        return "{\"error\": " + text(message) + "}";
    }

    /**
     * Reads the fields of a flat JSON object. Every value is returned as text,
     * so numbers must be converted by the caller.
     *
     * @param json the JSON text, for example a request body
     * @return a map from field name to value
     */
    public static Map<String, String> parse(String json) {
        Map<String, String> fields = new HashMap<>();
        Matcher matcher = FIELD.matcher(json);
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(3) != null ? matcher.group(3) : matcher.group(2);
            fields.put(key, value);
        }
        return fields;
    }

    /**
     * Turns a Java string into a quoted JSON string, escaping quotes and backslashes.
     *
     * @param value the text, or {@code null}
     * @return the quoted text, or {@code null} as JSON
     */
    private static String text(String value) {
        if (value == null) {
            return "null";
        }
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
}
