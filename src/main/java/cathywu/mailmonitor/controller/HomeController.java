package cathywu.mailmonitor.controller;

import cathywu.mailmonitor.credential.CredentialCache;
import cathywu.mailmonitor.credential.UserInfo;
import cathywu.mailmonitor.exception.LoginException;
import cathywu.mailmonitor.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

/**
 * @author lzwu
 * @since 7/28/15
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String settings(@CookieValue(value = "credential_key", defaultValue = "") String credentialKey) {
        if (checkLogin(credentialKey)) {
            return "tasks";
        } else {
            return "login";
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String login(@RequestParam HashMap<String, String> valueMap, HttpServletRequest request, HttpServletResponse response) throws LoginException {
        UserInfo userInfo = new UserInfo(request.getRemoteAddr(), valueMap.get("nickName"), valueMap.get("userName"), valueMap.get("host"), new Date());
        try {
            String credentialKey = CredentialCache.getInstance().addUser(userInfo);
            CookieUtil.getInstance(response).addCookie("credential_key", credentialKey, 24 * 3600);
        } catch (Exception e) {
            throw new LoginException("Something wrong with creating user.");
        }
        return "tasks";
    }

    @RequestMapping(value = "/logout")
    public String logout(@CookieValue(value = "credential_key", defaultValue = "") String credentialKey, HttpServletResponse response) {
        if (!credentialKey.isEmpty()) {
            CookieUtil.getInstance(response).removeCookie("credential_key");
            CredentialCache.getInstance().removeUser(credentialKey);
        }
        return "login";
    }

    private boolean checkLogin(String credentialKey) {
        return !credentialKey.isEmpty() && CredentialCache.getInstance().getUser(credentialKey) != null;
    }
}
