package util;

import java.util.regex.Pattern;

public class Htmltools {
	public static String htmlRemoveTag(String inputString) {
	    if (inputString == null)
	        return null;
	    String htmlStr = inputString; // 含html标签的字符串
	    String textStr = "";
	    java.util.regex.Pattern p_script;
	    java.util.regex.Matcher m_script;
	    java.util.regex.Pattern p_style;
	    java.util.regex.Matcher m_style;
	    java.util.regex.Pattern p_html;
	    java.util.regex.Matcher m_html;
	    try {
	        //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
	        String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; 
	        //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
	        String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; 
	        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	        p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
	        m_script = p_script.matcher(htmlStr);
	        htmlStr = m_script.replaceAll(""); // 过滤script标签
	        p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
	        m_style = p_style.matcher(htmlStr);
	        htmlStr = m_style.replaceAll(""); // 过滤style标签
	        p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
	        m_html = p_html.matcher(htmlStr);
	        htmlStr = m_html.replaceAll(""); // 过滤html标签
	        
	        // 
	        htmlStr   =   htmlStr.replaceAll("&(quot|#34);", "\"");   
	        htmlStr   =   htmlStr.replaceAll("&(amp|#38);", "&");   
	        htmlStr   =   htmlStr.replaceAll("&(lt|#60);", "<");   
	        htmlStr   =   htmlStr.replaceAll("&(gt|#62);", ">");   
	        htmlStr   =   htmlStr.replaceAll("&(nbsp|#160);", "   ");   
	        htmlStr   =   htmlStr.replaceAll("&(iexcl|#161);", "\\xa1");   
	        htmlStr   =   htmlStr.replaceAll("&(cent|#162);", "\\xa2");   
	        htmlStr   =   htmlStr.replaceAll("&(pound|#163);", "\\xa3");   
	        htmlStr   =   htmlStr.replaceAll("&(copy|#169);", "\\xa9");   
	        htmlStr   =   htmlStr.replaceAll("&#(\\d+);", "");   
	        
	        
	        textStr = htmlStr;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return textStr;// 返回文本字符串
	}
	
	public static void main(String argsp[]) {
		String teststr = "<p>生命的旅途，&nbsp;一程有一程的风景，一程有一程的盛放。打开心灵的窗子，" + 
						"静看时光旖旎着一曲花开花落，用一种看山是山，<script>function foo(){}</script>看水是水的境界来生活就会快乐，" +
						"人生的最美，便是来自心灵深处的通透与清欢。</p><style>" + 
						"一种看山是山，</style><script>function f一种看山是山，";
		System.out.println(Htmltools.htmlRemoveTag(teststr));
	}
}
