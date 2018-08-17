package it.etoken.base.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HtmlUtils {

	static final String template = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"></head><body><div id=\"news_content\">${content}</div></body></html>";
	//添加快讯跟广告图的模板
	public static String gemHtml(String html, String savePath) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String folder = df.format(System.currentTimeMillis());
		String filePath = folder + "/" + System.currentTimeMillis() + ".html";
		File f = new File(savePath + filePath);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		PrintStream printStream = null;
		try {
			printStream = new PrintStream(new FileOutputStream(f));
			printStream.println(template.replace("${content}", html));
			return filePath;
		} catch (Exception e) {
			throw e;
		} finally {
			if (printStream != null)
				printStream.close();
		}
	}
	
	//添加资讯的html模板
	public static String gemHtmlforAlerts(String content, String title,String eosprice,String savePath,String template) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String folder = df.format(System.currentTimeMillis());
		String filePath = folder + "/" + System.currentTimeMillis() + ".html";
		File f = new File(savePath + filePath);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		PrintStream printStream = null;
		try {
			template = template.replace("&quot;", "\"");
			if(eosprice==null || "".equals(eosprice)){
				eosprice="55.7";
				System.out.println(eosprice);
	        }
			printStream = new PrintStream(new FileOutputStream(f));
			printStream.println(String.format(template, title,content,eosprice));
			return filePath;
		} catch (Exception e) {
			throw e;
		} finally {
			if (printStream != null)
				printStream.close();
		}
	}
	//修改快讯的已经产生的html模板
	public static void regemHtml(String content,String title,String eosprice,String filePath,String template) throws Exception {
		File f = new File(filePath);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		PrintStream printStream = null;
//		SimpleDateFormat datefmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String str=datefmt.format(date);  
		try {
			template = template.replace("&quot;", "\"");
			if(eosprice==null || "".equals(eosprice)){
				eosprice="55.7";
	        }
			printStream = new PrintStream(new FileOutputStream(f,false));
			printStream.println(String.format(template, title,content,eosprice));
		} catch (Exception e) {
			throw e;
		} finally {
			if (printStream != null)
				printStream.close();
		}
	}
	//修改资讯的已经产生的html模板
		public static void regemHtmlForInformation(String html,String filePath) throws Exception {
			File f = new File(filePath);
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			PrintStream printStream = null;
			try {
				printStream = new PrintStream(new FileOutputStream(f));
				printStream.println(template.replace("${content}", html));
			} catch (Exception e) {
				throw e;
			} finally {
				if (printStream != null)
					printStream.close();
			}
		}
	
	
}
