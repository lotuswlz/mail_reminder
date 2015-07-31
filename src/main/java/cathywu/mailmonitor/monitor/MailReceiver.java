package cathywu.mailmonitor.monitor;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import javax.mail.search.RecipientTerm;

/**
 * @author lzwu
 * @since 7/30/15
 */
public class MailReceiver implements Runnable {

    private Folder folder;
    private NotificationHandler notificationHandler;

    public MailReceiver(Folder folder, NotificationHandler notificationHandler) {
        this.folder = folder;
        this.notificationHandler = notificationHandler;
    }

    @Override
    public void run() {
        try {
            processExistMails();
        } catch (MessagingException e) {
            notificationHandler.sendNotification("M_EMAIL_FETCH_ERROR");
        }


        // add listener to receive new email
    }

    private void processExistMails() throws MessagingException {
        // PROCESS UNREAD email
        folder.open(Folder.READ_WRITE);

        Flags q = new Flags(Flags.Flag.SEEN);
        FlagTerm flagTerm = new FlagTerm(q, false);
        Message[] unreads = folder.search(flagTerm);
        RecipientTerm recipientTerm = new RecipientTerm(Message.RecipientType.TO, new InternetAddress(""));
    }
}
