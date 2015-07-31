package cathywu.mailmonitor.util;

import cathywu.mailmonitor.exception.ConnectionException;

import javax.mail.*;
import java.util.Properties;

/**
 * @author lzwu
 * @since 7/29/15
 */
public class MailConnection {
//    private static String HOST = "webmail.in.telstra.com.au";
//    private static String HOST = "imap.gmail.com";

    private String host;
    private String userName;
    private String password;

    @Deprecated
    public MailConnection(String host, String userName, String password) {
        this.host = host;
        this.userName = userName;
        this.password = password;
    }

    public static Store createConnection(String host, String userName, String password) {
        String provider = "imaps";
        try {
            Properties properties = System.getProperties();
            Session session = Session.getDefaultInstance(properties);
            Store store = session.getStore(provider);
            store.connect(host, userName, password);
            return store;
        } catch (NoSuchProviderException e) {
            throw new ConnectionException(String.format("provider error [provider=%s, host=%s", provider, host), e);
        } catch (MessagingException e) {
            throw new ConnectionException(String.format("connection error [host=%s, user=%s", host, userName), e);
        }
    }

    @Deprecated
    public Message[] readMail() throws MessagingException {

        Properties properties = System.getProperties();
        Session session = Session.getDefaultInstance(properties);

        // Get a Store object
        Store store = session.getStore("imaps");

        System.out.println("Start to connect host: " + host);
        // Connect
        store.connect(host, this.userName, this.password);

        System.out.println("Connected.");

        // Open a Folder
        Folder folder = store.getFolder("INBOX");
        if (folder == null || !folder.exists()) {
            System.out.println("Invalid folder");
            System.exit(1);
        }

        folder.open(Folder.READ_WRITE);

        Flags q = new Flags(Flags.Flag.SEEN);
        javax.mail.search.FlagTerm flagTerm = new javax.mail.search.FlagTerm(
                q, false);
        Message[] unreads = folder.search(flagTerm);

        store.close();

        return unreads;
    }
}
