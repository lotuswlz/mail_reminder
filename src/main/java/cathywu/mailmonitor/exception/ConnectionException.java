package cathywu.mailmonitor.exception;

/**
 * @author lzwu
 * @since 7/30/15
 */
public class ConnectionException extends RuntimeException {
    public ConnectionException() {
    }

    public ConnectionException(String s) {
        super(s);
    }

    public ConnectionException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ConnectionException(Throwable throwable) {
        super(throwable);
    }
}
