package cathywu.mailmonitor.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lzwu
 * @since 7/29/15
 */
public class CookieUtil {

    private HttpServletResponse response;

    private CookieUtil(HttpServletResponse response) {
        this.response = response;
    }

    public static CookieUtil getInstance(HttpServletResponse response) {
        return new CookieUtil(response);
    }

    public void addCookie(String key, String value, int expirySeconds) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(expirySeconds);
        response.addCookie(cookie);
    }

    public void removeCookie(String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
