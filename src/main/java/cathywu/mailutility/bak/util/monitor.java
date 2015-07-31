package cathywu.mailutility.bak.util;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPNestedMessage;
import sun.misc.BASE64Decoder;

import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class monitor {
    
    private final static String pathStr = "D:\\projects\\Maintenance\\email_jmrp\\practise\\";
    
    public static String generateFileName() {
        return generateFileName("", ".txt");
    }
        
    public static String generateFileName(String appendName, String extendName) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String path = pathStr + sdf.format(d) + "\\";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path + appendName + extendName;
    }

    public static void main(String argv[]) {
        if (argv.length != 5) {
            System.out
                    .println("Usage: monitor <host> <user> <password> <mbox> <freq>");
            System.exit(1);
        }
        System.out.println("\nTesting monitor\n");

        long timeout = System.currentTimeMillis();
        try {
            Properties props = System.getProperties();

            // set this session up to use SSL for IMAP connections
            props.setProperty("mail.imap.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            // don't fallback to normal IMAP connections on failure.
            props.setProperty("mail.imap.socketFactory.fallback", "false");
            // use the simap port for imap/ssl connections.
            props.setProperty("mail.imap.socketFactory.port", "993");

            // Get a Session object
            Session session = Session.getInstance(props, null);
            // session.setDebug(true);

            // Get a Store object
            Store store = session.getStore("imap");

            // Connect
            store.connect(argv[0], argv[1], argv[2]);

            // Open a Folder
            Folder folder = store.getFolder(argv[3]);
            if (folder == null || !folder.exists()) {
                System.out.println("Invalid folder");
                System.exit(1);
            }

            folder.open(Folder.READ_WRITE);

            Flags q = new Flags(Flags.Flag.SEEN);
            // q.add("PROCESSED");
            javax.mail.search.FlagTerm flagTerm = new javax.mail.search.FlagTerm(
                    q, false);
            Message[] unreads = folder.search(flagTerm);
//            displayEmailAddress(unreads, false);
            List<String> emailList = getUserEmailListUnsubscribed(unreads);
            System.out.println(emailList.size());
            StringBuffer sb = new StringBuffer();
            if (emailList.size() > 0) {
                String email = null;
                sb.append("'");
                sb.append(emailList.get(0));
                sb.append("'");
                for (int i = 1; i < emailList.size(); i++) {
                    email = emailList.get(i);
                    sb.append(",'");
                    sb.append(email);
                    sb.append("'");
                }
            }
            String content = JmrpFromMailUtils.generateSqlFile(sb.toString());
            File file = new File(generateFileName("exec_file", ".sql"));
            OutputStream os = new FileOutputStream(file);
            os.write(content.getBytes());
            os.close();
            timeout = System.currentTimeMillis() - timeout;
            System.out.println("\n Use time " + (timeout / 1000) + " seconds.");

            // Add messageCountListener to listen for new messages
            folder.addMessageCountListener(new MessageCountAdapter() {
                public void messagesAdded(MessageCountEvent ev) {
                    Message[] msgs = ev.getMessages();
                    List<String> emailList = getUserEmailListUnsubscribed(msgs);
                    for (String email : emailList) {
                        System.out.println("\t" + email);
                    }
                }
            });

            boolean supportsIdle = false;
            try {
                if (folder instanceof IMAPFolder) {
                    IMAPFolder f = (IMAPFolder) folder;
                    f.idle();
                    supportsIdle = true;
                }
            } catch (FolderClosedException fex) {
                throw fex;
            } catch (MessagingException mex) {
                supportsIdle = false;
            }
            for (;;) {
                if (supportsIdle && folder instanceof IMAPFolder) {
                    IMAPFolder f = (IMAPFolder) folder;
                    f.idle();
                    System.out.println("IDLE done");
                } else {
                    int freq = Integer.parseInt(argv[4]);
                    Thread.sleep(freq); // sleep for freq milliseconds

                    // This is to force the IMAP server to send us
                    // EXISTS notifications.
                    folder.getMessageCount();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private static List<String> getUserEmailListUnsubscribed(Message[] messages) {
        if (messages == null || messages.length == 0) {
            return null;
        }
        try {
            List<String> list = new ArrayList<String>();
            
            String fileContent = null;
            String fileName = null;
            String senderAddress = null;
            String temp = null;
            int len = 0;
            int count = 0;
            String fmt = "";
            Map<String, Integer> map = new HashMap<String, Integer>();
            System.out.print("Fetching mails...... ");
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse("2010-05-28");
            for (Message msg : messages) {
                if (msg.getReceivedDate().before(date)) {
                	continue;
                }
                senderAddress = ((InternetAddress[])msg.getFrom())[0].getAddress();

                if (msg.getSubject().startsWith("complaint about message from")
                        && senderAddress.trim().equals("staff@hotmail.com")) {
                	
//
//                	String content = readMailTest(msg.getInputStream());
//                    writeLog(String.valueOf(msg.getMessageNumber()), content);
                	
                    count = count + 1;
                    len = fmt.length();
                    for (int j = 0; j < len; j++) {
                        System.out.print("\b");
                    }

                    fmt = String.valueOf(count + " / " + messages.length);
                    System.out.print(fmt);
                    JmrpMailBean bean = getMailContent(msg, false);
                    if (bean == null) {
                        System.out.println("Error: bean is null");
                        continue;
                    }
                    fileName = writeToFile(msg);
                    fileContent = getFileContent(fileName);

                    temp = "www.offerme.com.au/unsubscribe?mail=";
                    
                    if (fileContent.contains(temp)) {
                        bean.setUnsubscribeSetting(1);
                    } else {
                        System.out.print("Message Number "
                                + msg.getMessageNumber()
                                + ": doesn\'t contains " + temp);
                    }

                    if (!bean.isNewsLetter()) {
                        System.out.println("\tIs Newsletter: " + bean.isNewsLetter());
                    }
                    if (bean.isNewsLetter()) {
                        msg.setFlag(Flags.Flag.SEEN, true);
                        for (int i = 0; i < bean.getTo().length; i++) {
                            
                            if (list.contains(bean.getTo()[i])) {
                                map.put(bean.getTo()[i], map.get(bean.getTo()[i]) + 1);
                                continue;
                            }
                            list.add(bean.getTo()[i]);
                            map.put(bean.getTo()[i], 1);
                        }
                    }
                }
            }
            if (map.size() > 0) {
                for (Iterator<Entry<String, Integer>> it = map.entrySet().iterator(); it.hasNext();) {
                    Entry<String, Integer> e = it.next();
                    System.out.println("From: " + e.getKey() + "\t" + e.getValue() + " times");
                }
                map.clear();
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static int getUnsubscribedMailType (String content, String str) {
        str = str.replaceAll("(\\.|\\\\\\|\\\\d|\\]|\\[|\\{|\\}|\\^|\\$|\\-|\\+|\\*|\\(|\\)|\\?)", "\\\\$1");
        String regex = "(" + str + "[\\d]+)";
        Pattern ptn = Pattern.compile(regex);
        Matcher mtc = ptn.matcher(content);
        String temp = null;
        while (mtc.find()) {
            temp = mtc.group();
        }
        if (temp == null) {
            return 0;
        }
        temp = temp.replaceAll(".*&mailType=([\\d]+)", "$1");
        return Integer.parseInt(temp);
    }
    
    private static JmrpMailBean getMailContent(Object message, boolean isChild) throws Exception {
        JmrpMailBean bean = null;
        Part part = (Part) message;
        String contenttype = part.getContentType();
        int nameindex = contenttype.indexOf("name");
        boolean conname = false;
        if (nameindex != -1)
            conname = true;
        if (part.isMimeType("text/plain") && !conname) {
            bean = new JmrpMailBean(((Message)message));
            try {
                bean.setContent((String) ((Message) message).getContent());
            } catch (Exception e) {
                System.out.println("Error for message: "
                        + ((Message) message).getMessageNumber()
                        + "; Exception: " + e.getMessage());
            }
        } else if (part.isMimeType("text/html") && !conname) {
            if (isChild) {
                IMAPNestedMessage im = (IMAPNestedMessage) message;
                InternetAddress[] address = (InternetAddress[]) im.getAllRecipients();
                String[] receivers = new String[address.length];
                for (int i = 0; i < address.length; i++) {
                    receivers[i] = address[i].getAddress();
                }
                bean = new JmrpMailBean(im.getMessageNumber(), im.getSubject(),
                        ((InternetAddress) im.getFrom()[0]).getAddress(),
                        receivers, im.getContentType(), 0);
            } else {
                bean = new JmrpMailBean((Message) message);
                bean.setContent((String) ((Message) message).getContent());
            }
        } else if (part.isMimeType("multipart/*")) {
//            Multipart multipart = (Multipart) part.getContent();
//            int counts = multipart.getCount();
//            for (int i = 0; i < counts; i++) {
//                bean = getMailContent(multipart.getBodyPart(i), false);
//            }
            bean = new JmrpMailBean((Message) message);
        } else if (part.isMimeType("message/rfc822")) {
            bean = getMailContent(part.getContent(), true);
        } else {
            System.out.println("other type: " + part.getContentType());
        }
        return bean;
    }
    
    private static String writeToFile(Message message) {
        String fileName = generateFileName("original_mail_" + message.getMessageNumber(), ".txt");
        try {
            File file = new File(fileName);
            OutputStream os = new FileOutputStream(file);
//            MimeUtility.encode(os, "7bit");
//            MimeUtility.encode(os, "7bit");
            message.writeTo(os);
            os.close();
            
//            file = new File("D:/development_documents/jmrp_test_log/" + message.getMessageNumber() + "_" + MimeUtility.getEncoding(message.getDataHandler()));
//            os = new FileOutputStream(file);
//            String temp = MimeUtility.decodeText(getFileContent(fileName));
//            os.write(temp.getBytes());
//            os.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return fileName;
    }
    
    private static String getFileContent(String fileName) {
        try {
            StringBuffer sbf = new StringBuffer();
            File file = new File(fileName);
            InputStream is = new FileInputStream(file);
            is = MimeUtility.decode(is, "7bit");
            int tempbyte = 0;
            while ((tempbyte = is.read()) != -1) {
                sbf.append((char) tempbyte);
            }
            return sbf.toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
    
    private static String getFileContent(Message message) {
        try {
            StringBuffer sbf = new StringBuffer();
            InputStream is = message.getInputStream();
            int tempbyte = 0;
            while ((tempbyte = is.read()) != -1) {
                sbf.append((char) tempbyte);
            }
            return sbf.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static void displayEmailAddress(Message[] unreads, boolean flag)
            throws FileNotFoundException, IOException {
        if (unreads != null) {
            System.out.println("Got " + unreads.length
                    + " messages without processed");
            String fileName = generateFileName();
            File file = new File(fileName);
            OutputStream os = new FileOutputStream(file, true);
            InternetAddress [] address = null;
            String senderAddress = null;
            String subject = null;
            for (int i = 0; i < unreads.length; i++) {
                try {
                    address = (InternetAddress[]) unreads[i].getFrom();
                    if (address.length > 1) {
                        System.out.println("Error: one mail has " + address.length + "sender.");
                    }
                    senderAddress = address[0].getAddress();
                    subject = unreads[i].getSubject();
                    System.out.println("No: " + unreads[i].getMessageNumber()
                            + " From: " + senderAddress + " Subject: "
                            + subject);
                    if (senderAddress.trim().equals("staff@hotmail.com")
                            && subject.trim().startsWith(
                                    "complaint about message from")) {
                        
//                        unreads[i].writeTo(os);
//                        unreads[i].setFlag(Flags.Flag.SEEN, true);
                    }
                    ReceiveOneMail r = new ReceiveOneMail((MimeMessage) unreads[i]);
                    r.getMailContent(unreads[i], false);
//                    System.out.println(r.getBodyText());
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            os.close();
            System.out.println("email get complete...");
            if (flag) {
                JmrpFromMailUtils.exeCommand("grep \"To: \" \"" + fileName + "\"", null);
            }
        }
    }
    
    private static String getMailMessage(MimeMultipart m) throws MessagingException, IOException {
        return m.getBodyPart(0).getContent().toString();
    }

    private static void writeEmailToFile(Message[] unreads)
            throws FileNotFoundException, IOException {
        if (unreads != null) {
            System.out.println("Got " + unreads.length
                    + " messages without processed");
            File file = new File("mail.txt");
            OutputStream os = new FileOutputStream(file, true);
            for (int i = 0; i < unreads.length; i++) {
                try {
                    System.out.println("-----");
                    System.out.println("Message "
                            + unreads[i].getMessageNumber() + ":");
                    // unreads[i].writeTo(os);
                    System.out.println(unreads[i].getSubject());
                    InputStream is = ((javax.mail.internet.MimeMultipart) unreads[i]
                            .getContent()).getBodyPart(0).getInputStream();
                    int tempbyte;
                    StringBuffer sbf = new StringBuffer();
                    while ((tempbyte = is.read()) != -1) {
                        sbf.append((char) tempbyte);
                    }
                    System.out
                            .println("\n\n------------------------------------------------------------");
                    System.out.println(unreads[i].getFrom()[0].toString());
                    System.out.println(unreads[i].getContent().getClass()
                            .getName()
                            + ":"
                            + unreads[i].getContent().getClass()
                                    .getCanonicalName()
                            + ":"
                            + unreads[i].getContent().getClass()
                                    .getSimpleName());
                    sun.misc.BASE64Decoder b = new BASE64Decoder();
                    System.out
                            .println(((javax.mail.internet.MimeMultipart) unreads[i]
                                    .getContent()).toString());
                    byte[] bytes = ((javax.mail.internet.MimeMultipart) unreads[i]
                            .getContent()).getBodyPart(0).getContent()
                            .toString().getBytes("UTF-8");
                    os.write(bytes);
                    is.close();
                    /**
                     * unreads[i].setFlags(new Flags("PROCESSED"), true);
                     * unreads[i].setFlag(Flags.Flag.SEEN, true);
                     */
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                } catch (MessagingException mex) {
                    mex.printStackTrace();
                }
            }
            os.flush();
            os.close();
            System.out.println("complete");
        }
    }
    
    
    public static List<String> callJmrpUtil(String address, String emailStr, String password, String folderName) {
        
        System.out.println("\nTesting monitor\n");

        long timeout = System.currentTimeMillis();
        try {
            Properties props = System.getProperties();

            // set this session up to use SSL for IMAP connections
            props.setProperty("mail.imap.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            // don't fallback to normal IMAP connections on failure.
            props.setProperty("mail.imap.socketFactory.fallback", "false");
            // use the simap port for imap/ssl connections.
            props.setProperty("mail.imap.socketFactory.port", "993");

            // Get a Session object
            Session session = Session.getInstance(props, null);

            // Get a Store object
            Store store = session.getStore("imap");

            // Connect
            store.connect(address, emailStr, password);

            // Open a Folder
            Folder folder = store.getFolder(folderName);
            if (folder == null || !folder.exists()) {
                System.out.println("Invalid folder");
                System.exit(1);
            }

            folder.open(Folder.READ_WRITE);

            Flags q = new Flags(Flags.Flag.SEEN);

            javax.mail.search.FlagTerm flagTerm = new javax.mail.search.FlagTerm(
                    q, false);
            Message[] unreads = folder.search(flagTerm);

            List<String> emailList = getUserEmailListUnsubscribed(unreads);

            StringBuffer sb = new StringBuffer();
            if (emailList.size() > 0) {
                String email = null;
                sb.append("'");
                sb.append(emailList.get(0));
                sb.append("'");
                for (int i = 1; i < emailList.size(); i++) {
                    email = emailList.get(i);
                    sb.append(",'");
                    sb.append(email);
                    sb.append("'");
                }
            }
            String content = JmrpFromMailUtils.generateSqlFile(sb.toString());
            File file = new File(generateFileName("exec_file", ".sql"));
            OutputStream os = new FileOutputStream(file);
            os.write(content.getBytes());
            os.close();
            timeout = System.currentTimeMillis() - timeout;
            System.out.println("\n Use time " + (timeout / 1000) + " seconds.");
            
            return emailList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    public static String readMailTest(InputStream is) {
    	StringBuffer sbf = new StringBuffer();
    	try {
			int tempbyte = 0;
			while ((tempbyte = is.read()) != -1) {
			    sbf.append((char) tempbyte);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return sbf.toString();
    }
    
    private static void writeLog(String filename, String content) {
    	try {
			String path = "D:/development_documents/jmrp_test_log/" + filename + ".log";
			
			File file = new File(path);
			OutputStream os = new FileOutputStream(file);
			os.write(content.getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
