package cathywu.mailmonitor.monitor;

import javax.mail.Message;

/**
 * @author lzwu
 * @since 7/30/15
 */
public class MailTask {
    private String subject;
    private String message;
    private String sender;
    private TaskStatus status;

    private Message originMessage;

    public MailTask(String subject, String message, String sender) {
        this.subject = subject;
        this.message = message;
        this.sender = sender;
    }

    public Message getOriginMessage() {
        return originMessage;
    }

    public void setOriginMessage(Message originMessage) {
        this.originMessage = originMessage;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
