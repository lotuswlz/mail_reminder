package cathywu.mailutility.bak.util;

import sun.misc.BASE64Decoder;

import javax.mail.internet.MimeUtility;
import java.io.*;

@Deprecated
public class CheckMailFile {

	private static String constant_str = "www.offerme.com.au/unsubscribe?mail=";
	private static String filePath = "D:\\projects\\Maintenance\\email_jmrp\\practise\\20100603";
	
	public static void listMatchedString(String path) {
		
		File dir = new File(path);
		if (!dir.isDirectory()) {
			return;
		}
		
		File[] files = dir.listFiles();
		try {
			for (File f : files) {
//				System.out.println(f.getPath());
				if (f.isDirectory()) {
					listMatchedString(f.getPath());
					continue;
				}
				if (f.getName().startsWith("original_mail_")) {
					printMatchString(f);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void printMatchString(File f) throws IOException {
		System.gc();
		FileReader fr = null;
		BufferedReader br = null;
		if(!f.isFile()){
			return;
		}
		fr = new FileReader(f);
		br = new BufferedReader(fr);
		
		int lineCount = 0;
		String temp;
		do{
			temp = br.readLine();
			lineCount++;
			if (temp!= null && temp.contains(constant_str) && temp.trim().startsWith("=3D")) {
				//f.getName() + "(line " + lineCount + "):\t" +
				System.out.println(f.getPath() + "(line " + lineCount + "):\t" +temp.trim());
				break;
			}
			
		} while(temp != null);
		br.close();
		fr.close();
	}
	
	public static String decryptBase64(String text) {
		try {
//			BASE64Encoder base64 = new BASE64Encoder();
//			String temp = base64.encode(text.getBytes());
//			System.out.println(text);
//			System.out.println(temp);
			BASE64Decoder decoder = new BASE64Decoder();
			System.out.println(new String(decoder.decodeBuffer(text), "utf-8"));
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
	
	public static void main(String[] args) {
//		listMatchedString(filePath);
//		decryptBase64("=09=09=09=09=09This email was sent to the subscription of Brad Smith (brads=mith216@hotmail.com).<br />=09=09=09=09=09=09=09=09=09=09If you wish to man=age your subscriptions, please <a href=3D\"http://www.offerme.com.au/my/acco=unt/preferences?source=3Demail\">click here</a>.<br />=09=09=09=09=09If you are not the intended recipient please click <a href==3D\"http://www.offerme.com.au/unsubscribe?mail=3Dbradsmith216@hotmail.com&m=ailType=3D1\">unsubscribe</a>.<br />");
		try {
			String str = "=09=09=09=09=09This email was sent to the subscription of Brad Smith (brads=mith216@hotmail.com).<br />=09=09=09=09=09=09=09=09=09=09If you wish to man=age your subscriptions, please <a href=3D\"http://www.offerme.com.au/my/acco=unt/preferences?source=3Demail\">click here</a>.<br />=09=09=09=09=09If you are not the intended recipient please click <a href==3D\"http://www.offerme.com.au/unsubscribe?mail=3Dbradsmith216@hotmail.com&m=ailType=3D1\">unsubscribe</a>.<br />";
			String temp = MimeUtility.decodeText(str);
			System.out.println(temp);
			
			System.out.println(MimeUtility.encodeText(str));
			System.out.println(temp);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
