package entity;

import java.util.HashSet;
import java.util.Set;

public class UniversityPageinfo extends BaseObject{

	String pageurl;

	String pagetitle;
	
	// add all the emails in the page to a string, splited by ';'
	String emailAddresses;
	
	// add all the names in the to a string, splited by ';'
	String names;
	
	String pagecontent;
	
	String fields;
	
	String contentkeyword;
	
	String keywordsynonym;

	public String getPageurl() {
		return pageurl;
	}

	public void setPageurl(String pageurl) {
		this.pageurl = pageurl;
	}

	public String getPagetitle() {
		return pagetitle;
	}

	public void setPagetitle(String pagetitle) {
		this.pagetitle = pagetitle;
	}

	public String getEmailAddresses() {
		return emailAddresses;
	}

	public void setEmailAddresses(String emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}
	

	public String getPagecontent() {
		return pagecontent;
	}

	public void setPagecontent(String pagecontent) {
		this.pagecontent = pagecontent;
	}
	
	public String getContentkeyword() {
		return contentkeyword;
	}

	public void setContentkeyword(String contentkeyword) {
		this.contentkeyword = contentkeyword;
	}

	public String getKeywordsynonym() {
		return keywordsynonym;
	}

	public void setKeywordsynonym(String keywordsynonym) {
		this.keywordsynonym = keywordsynonym;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		return "Page [pageurl=" + pageurl + ", pagetitle=" + pagetitle
				+ ", emailAddresses=" + emailAddresses + ", names=" + names
				+ ", pagecontent=" + pagecontent + "]";
	}
}
