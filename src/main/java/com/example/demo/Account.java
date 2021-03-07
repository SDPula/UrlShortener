package com.example.demo;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "ACCOUNT")
public class Account{
	private @Id @GeneratedValue Long id;
	private String accountId;
	private String password;
	
	public Account() {}
	
	public Account(String accountId, String password)
	{
		this.accountId = accountId;
		this.password = password;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountNumber(String accountId) {
		this.accountId = accountId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String pasword) {
		this.password = pasword;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	  public boolean equals(Object o) {

	    if (this == o)
	      return true;
	    if (!(o instanceof Account))
	      return false;
	    Account account = (Account) o;
	    return Objects.equals(this.id, account.id) 
	    		&& Objects.equals(this.accountId, account.accountId) 
	    		&& Objects.equals(this.password, account.password);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(this.id, this.accountId, this.password);
	  }

	  @Override
	  public String toString() {
	    return "Account{" + "id=" + this.id 
	    		+ ", number='" + this.accountId + '\'' 
	    		+ ", password='" + this.password + '\'' + '}';
	  }
}