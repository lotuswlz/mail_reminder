package cathywu.mailmonitor.credential;

import cathywu.mailmonitor.util.EncryptUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lzwu
 * @since 7/28/15
 */
public class CredentialCache {

    private static CredentialCache instance = null;

    private Map<String, UserInfo> users;

    private CredentialCache() {
        users = new ConcurrentHashMap<String, UserInfo>();
    }

    public static CredentialCache getInstance() {
        if (instance == null) {
            instance = new CredentialCache();
        }
        return instance;
    }

    public String addUser(UserInfo userInfo) throws Exception {
        String credentialKey = EncryptUtil.shaEncode(userInfo.getIp() + "_" + userInfo.getUserName() + "_" + userInfo.getLoginTime().getTime());
        this.users.put(credentialKey, userInfo);
        return credentialKey;
    }

    public void removeUser(String credentialKey) {
        this.users.remove(credentialKey);
    }

    public UserInfo getUser(String credentialKey) {
        return this.users.get(credentialKey);
    }
}
