package util;

import java.net.MalformedURLException;
import java.net.URL;

public class Urltools {
	public static String getDomainName(String url) throws MalformedURLException{
	    if(!url.startsWith("http") && !url.startsWith("https")){
	         url = "http://" + url;
	    }        
	    URL netUrl = new URL(url);
	    String host = netUrl.getHost();
	    if(host.startsWith("www")){
	        host = host.substring("www".length()+1);
	    }
	    return host;
	}
	
	public static String formalizeStr2Url(String str) throws MalformedURLException {
	    if(!str.startsWith("http") && !str.startsWith("https")){
	    	str = "http://" + str;
	    }
		String url = "";
		URL netUrl = new URL(str);
		url = netUrl.getProtocol() + "://"+ netUrl.getAuthority() + ":" + (netUrl.getPort() != -1 ? netUrl.getPort() : netUrl.getDefaultPort()) +
				netUrl.getFile();
		
		return url;
	}
	
	public static  void main(String[] args) throws MalformedURLException {
		String url1 = "http://google.com/dhasjkdas/sadsdds/sdda/sdads.html";
		String url2 = "https://www.google.com/dhasjkdas/sadsdds/sdda/sdads.html";
		String url3 = "http://google.co.uk/dhasjkdas/sadsdds/sdda/sdads.html";
		String url4 = "www.baidu.com/123/asdfasd/asdf.html";
		String url5 = "baidu.com/asdfasdf/123.html";
		System.out.println(Urltools.formalizeStr2Url(url1));
		System.out.println(Urltools.formalizeStr2Url(url2));
		System.out.println(Urltools.formalizeStr2Url(url3));
		System.out.println(Urltools.formalizeStr2Url(url4));
		System.out.println(Urltools.formalizeStr2Url(url5));
		
	}
}
