package cathywu.mailmonitor.exception;

/**
 * @author lzwu
 * @since 7/29/15
 */
public class LoginException extends Throwable {
    public LoginException(String message) {
        super(message);
    }

    public LoginException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public LoginException(Throwable throwable) {
        super(throwable);
    }

    public LoginException() {

    }
}
