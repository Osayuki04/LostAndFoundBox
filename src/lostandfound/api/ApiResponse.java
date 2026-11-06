package lostandfound.api;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Sends JSON responses back to the client, and logs every request.
 *
 * @author Osayuki
 * @version 1.0
 */
public class ApiResponse {
    /** Shared logger for the API classes. */
    public static final Logger LOGGER = Logger.getLogger(ApiResponse.class.getName());


    /** Not used: this class only has static methods. */
    private ApiResponse() {
    }

    /**
     * Sends a JSON response and closes the exchange.
     *
     * @param exchange the request being answered
     * @param status   the HTTP status code, for example 200 or 404
     * @param json     the JSON text to send as the body
     * @throws IOException if the response cannot be written
     */
    public static void send(HttpExchange exchange, int status, String json) throws IOException {
        LOGGER.info(exchange.getRequestMethod() + " " + exchange.getRequestURI() + " -> " + status);

        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream body = exchange.getResponseBody()) {
            body.write(bytes);
        }
    }
}
