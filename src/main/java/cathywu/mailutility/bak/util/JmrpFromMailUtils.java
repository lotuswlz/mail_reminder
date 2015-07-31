/*
 * All Rights Reserved. Copyright(C) 2008 OfferMe.com.au, Australia
 *
 * History
 *   Version     Update Date         Updater       Details
 *   1.0.00  2010-1-20      Cathy Wu        Create
 */

package cathywu.mailutility.bak.util;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
public class JmrpFromMailUtils {

    private final static String pathStr = "D:\\projects\\Maintenance\\email_jmrp\\email content archive\\";
    private final static String pathStr2 = "D:\\projects\\Maintenance\\email_jmrp\\";
    
    public static String generateFileName() {
        return generateFileName("", ".txt");
    }
    
    public static String generateFileName(String appendName, String extendName) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return pathStr + sdf.format(d) + appendName + extendName;
    }

    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            System.out.println("Error: Please enter file name.");
            System.exit(0);
            return;
        }
        try {
            String fileName = null;
            if (args[0].trim().equals("0")) {
                fileName = generateFileName();
            } else {
                fileName = args[0];
            }
            exeCommand("grep \"To: \" \"" + fileName + "\"", null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public static boolean exeCommand(String cmd, String msg) {
        System.out.println(cmd);
        boolean result = true;
        Process process = null;
        try {
            // grep
            String[] cmdary = { "cmd", "/C", cmd };
            process = Runtime.getRuntime().exec(cmdary);
            InputStream is = process.getInputStream();
            
            // generate file
            int tempbyte;
            StringBuffer sbf = new StringBuffer();
            while ((tempbyte = is.read()) != -1) {
                sbf.append((char) tempbyte);
            }
            String s = getEmailListStr(sbf.toString());
            String content = generateSqlFile(s);
            File file = new File(generateFileName("_exec_file", ".sql"));
            OutputStream os = new FileOutputStream(file);
            os.write(content.getBytes());
            os.close();

            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    private static String getEmailListStr(String tempStr) {
        List<String> list = new ArrayList<String>();
        String regex = "[^a-zA-Z-]To: (.+)";
        Pattern ptn = Pattern.compile(regex);
        Matcher mc = null;
        mc = ptn.matcher(tempStr);
        String temp = null;
        System.out.println("\n\n------------------------------------");
        while(mc.find()) {
            temp = mc.group(1);
            if (!list.contains(temp)) {
                System.out.println(temp);
                list.add(temp);
            }
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        StringBuffer sbf = new StringBuffer();
        sbf.append(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            sbf.append("','");
            sbf.append(list.get(i));
        }
        sbf.insert(0, "'");
        sbf.append("'");
        return sbf.toString();
    }
    
    public static String generateSqlFile(String userNames) {
        try {
            File file = new File("" + pathStr2 + "README2.sql");
            InputStream is = new FileInputStream(file);
            int tempbyte;
            StringBuffer sbf = new StringBuffer();
            while ((tempbyte = is.read()) != -1) {
                sbf.append((char) tempbyte);
            }
            is.close();
            Map<String, String> map = new HashMap<String, String>();
            map.put("fileName", generateFileName());
            map.put("user_names", userNames);
            Velocity.init();
            VelocityContext vContext = new VelocityContext(map);
            StringWriter w = new StringWriter();
            Velocity.evaluate(vContext, w, "sql", sbf.toString());
            return w.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("return null");
        return null;
    }
}
