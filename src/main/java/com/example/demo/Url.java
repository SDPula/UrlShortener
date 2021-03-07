package com.example.demo;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "URL")
public class Url{
	
	private @Id @GeneratedValue Long id;
	private String accountNumber;
	private String url;
	private String shortUrl;
	private int redirectType;
	private int numCalled;
	
	public Url() {}
	
	public Url (String accountNumber, String url, String shortUrl,
			int redirectType, int numCalled)
	{
		this.accountNumber = accountNumber;
		this.url = url;
		this.shortUrl = shortUrl;
		this.redirectType = redirectType;
		this.numCalled = numCalled;
	}
	
	public int getRedirectType() {
		return redirectType;
	}

	public void setRedirectType(int redirectType) {
		this.redirectType = redirectType;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public int getNumCalled() {
		return numCalled;
	}

	public void setNumCalled(int numCalled) {
		this.numCalled = numCalled;
	}
	
	@Override
	  public boolean equals(Object o) {

	    if (this == o)
	      return true;
	    if (!(o instanceof Url))
	      return false;
	    Url url = (Url) o;
	    return Objects.equals(this.id, url.accountNumber) 
	    		&& Objects.equals(this.accountNumber, url.id) 
	    		&& Objects.equals(this.url, url.url)
	    		&& Objects.equals(this.shortUrl, url.shortUrl)
	    		&& Objects.equals(this.redirectType, url.redirectType)
	    		&& Objects.equals(this.numCalled, url.numCalled);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(this.id, this.accountNumber, this.url, this.shortUrl, 
	    		this.redirectType, this.numCalled);
	  }

	  @Override
	  public String toString() {
	    return "Url{" + "id=" + this.id 
	    		+ ", number='" + this.accountNumber + '\'' 
	    		+ ", url='" + this.url + '\'' 
	    		+ ", shortUrl='" + this.shortUrl + '\'' 
	    		+ ", redirectType='" + this.redirectType + '\'' 
	    		+ ", called='" + this.numCalled + '\'' + '}';
	  }
}

