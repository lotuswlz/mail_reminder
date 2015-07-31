package cathywu.mailmonitor.util;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author lzwu
 * @since 7/29/15
 */
public class NotificationUtil {

    private static Logger LOG  = Logger.getLogger(NotificationUtil.class);

    public static void sendNotification(String messageId, String info) {
        // send notification to desktop of users.
        try {
            ClassLoader classLoader = NotificationUtil.class.getClassLoader();
            File file = new File(classLoader.getResource("notify.sh").getFile());

            Process p = Runtime.getRuntime().exec("sh " + file.getPath() + " " + file.getParent() + " " + messageId + " " + info);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            LOG.warn(sb.toString());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
