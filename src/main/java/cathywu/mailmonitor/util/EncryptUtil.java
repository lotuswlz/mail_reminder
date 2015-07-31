package cathywu.mailmonitor.util;

import java.security.MessageDigest;

/**
 * @author lzwu
 * @since 7/29/15
 */
public class EncryptUtil {

    public static String shaEncode(String text) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA");

        byte[] byteArray = text.getBytes("UTF-8");
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
}
