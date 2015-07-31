package cathywu.mailmonitor.monitor;

import cathywu.mailmonitor.credential.UserInfo;
import cathywu.mailmonitor.exception.ConnectionException;
import cathywu.mailmonitor.util.NotificationUtil;
import org.apache.log4j.Logger;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

/**
 * @author lzwu
 * @since 7/29/15
 */
public class MailHandler {

    private static Logger LOG = Logger.getLogger(MailHandler.class);

    private UserInfo userInfo;
    private String password;

    private MailReceiver receiver;

    public MailHandler(UserInfo userInfo, String password) {
        this.userInfo = userInfo;
        this.password = password;
    }

    public void connect() throws MessagingException {
        Properties properties = System.getProperties();
        Session session = Session.getDefaultInstance(properties);

        Store store = session.getStore("imaps");

        store.connect(this.userInfo.getHost(), this.userInfo.getUserName(), this.password);

        Folder folder = store.getFolder("INBOX");
        if (folder == null || !folder.exists()) {
            LOG.error("[" + this.userInfo.getUserName() + "] Folder \"INBOX\" not exist.");
            throw new ConnectionException("Wrong folder");
        }

        receiver = new MailReceiver(folder, new NotificationHandler() {
            @Override
            public void sendNotification(String messageId) {
                NotificationUtil.sendNotification(messageId, userInfo.getUserName());
            }
        });
        Thread thread = new Thread(receiver);
        thread.start();
    }
}
