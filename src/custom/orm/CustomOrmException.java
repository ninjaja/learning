package custom.orm;

/**
 * @author Dmitry Matrizaev
 * @since 1.0
 */
public class CustomOrmException extends RuntimeException {

    public CustomOrmException() {
        super();
    }

    public CustomOrmException(String message) {
        super(message);
    }

    public CustomOrmException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomOrmException(Throwable cause) {
        super(cause);
    }
}
