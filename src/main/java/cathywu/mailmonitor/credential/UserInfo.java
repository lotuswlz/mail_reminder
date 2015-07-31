package cathywu.mailmonitor.credential;

import java.util.Date;

/**
 * @author lzwu
 * @since 7/29/15
 */
public class UserInfo {
    private String ip;
    private String nickName;
    private String userName;
    private String host;
    private Date loginTime;

    public String getIp() {
        return ip;
    }

    public String getNickName() {
        return nickName;
    }

    public String getUserName() {
        return userName;
    }

    public String getHost() {
        return host;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public UserInfo(String ip, String nickName, String userName, String host, Date loginTime) {
        this.ip = ip;
        this.nickName = nickName;
        this.userName = userName;
        this.host = host;
        this.loginTime = loginTime;
    }
}
