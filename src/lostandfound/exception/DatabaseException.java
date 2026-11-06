package lostandfound.exception;

/**
 * Thrown when the database cannot be reached or a query fails.
 * <p>
 * It wraps the original {@link java.sql.SQLException} so callers outside the
 * repository classes do not need to know any SQL details.
 *
 * @author Osayuki
 * @version 1.0
 */
public class DatabaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    /**
     * Creates the exception.
     *
     * @param message what was being done when it failed
     * @param cause   the original database error
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
