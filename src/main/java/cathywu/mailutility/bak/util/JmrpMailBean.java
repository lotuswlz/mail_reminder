/*
 * All Rights Reserved. Copyright(C) 2008 - 2010 OfferMe.com.au, Australia
 *
 * History
 *   Version     Update Date ����        Updater��������       Details
 *   1.0.00  2010-2-4      Cathy Wu        Create
 */

package cathywu.mailutility.bak.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

@Deprecated
public class JmrpMailBean {

    private int messageNumber;
    private String subject;
    private String from;
    private String[] to;
    private String content;
    private String contentType;
    /**
     * s1 - Group Buy Updates
     * s2 - Holiday & Promotional Special
     * s3 - New Features & General Tips
     */
    private int unsubscribeSetting;
    
    private static long[] counter = new long[3];
    
    public JmrpMailBean() {
        counter[0]++;
    }
    
    public JmrpMailBean(int messageNumber, String subject, String from,
            String[] to, String contentType,
            int unsubscribeSetting) {
        this.messageNumber = messageNumber;
        this.subject = subject;
        this.from = from;
        this.to = to;
        this.contentType = contentType;
    }
    
    public JmrpMailBean(Message message) {
        this.messageNumber = message.getMessageNumber();
        try {
            this.subject = message.getSubject();
            InternetAddress [] address = null;
            address = (InternetAddress[]) message.getFrom();
            this.from = address[0].getAddress();
            address = (InternetAddress[]) message.getAllRecipients();
            this.to = new String[address.length];
            InternetAddress adr = null;
            for (int i = 0; i < address.length; i++) {
                adr = address[i];
                this.to[i] = adr.getAddress();
            }
            this.contentType = message.getContentType();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    public int getUnsubscribeSetting() {
        return unsubscribeSetting;
    }
    public void setUnsubscribeSetting(int unsubscribeSetting) {
        this.unsubscribeSetting = unsubscribeSetting;
    }
    
    public boolean isNewsLetter() {
        return this.unsubscribeSetting != 0;
    }
    public int getMessageNumber() {
        return messageNumber;
    }
    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String[] getTo() {
        return to;
    }
    public void setTo(String[] to) {
        this.to = to;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    @Override
    public String toString() {
        String sbf = "MessageNumber=" + this.messageNumber + ", From: "
                + this.from + ", To: " + this.to[0] + ", mailSetting: " + this.unsubscribeSetting + ", Subject: "
                + this.subject;
        return sbf;
    }
}
