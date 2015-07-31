package cathywu.mailmonitor.credential;

/**
 * @author lzwu
 * @since 7/28/15
 */
public class UserAuthObject {
    private String userName;
    private String password;
    private String host;

    public UserAuthObject() {
    }

    public UserAuthObject(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isComplete() {
        return this.userName != null && this.password != null;
    }
}
