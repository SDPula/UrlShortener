package com.example.demo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;


@RestController 
public class UrlController {
	
	private final AccountRepository accountRepository;
	private final UrlRepository urlRepository;
	private ShortenerService shortenerService;
	
	public UrlController(AccountRepository accountRepository, UrlRepository urlRepository, ShortenerService shortenerService) {
		this.accountRepository = accountRepository;
		this.urlRepository = urlRepository;
		this.shortenerService = shortenerService;
	}
	

	@RequestMapping(value = "/register", 
	        method = RequestMethod.POST, 
	        produces = MediaType.APPLICATION_JSON_VALUE)
	    @ResponseBody
	    public ResponseEntity<Map<String,String>> registerUrl(
	    		@RequestHeader (value="Authorization") String authString,
	    		@RequestBody Url url){
		
		String username = "";
		String password = "";
		Pattern pattern = Pattern.compile(":");
		Matcher matcher = pattern.matcher(authString);
		if (matcher.find()) {
		    username = authString.substring(0, matcher.start());
		    password = authString.substring(matcher.end());
		}
		
		String shortUrl = "";
		Map<String,String> response = new LinkedHashMap<String, String>();
		boolean isRegistrationSuccesful = false;
		boolean urlExistsInDatabase = false;
		int redirectType = 302;
		
		List<Account> accountList = accountRepository.findAll();
		for (Account a : accountList)
	    {
			if(a.getAccountId().equals(username)) {
				if (a.getPassword().equals(password))
					{
					isRegistrationSuccesful = true;
					break;
					}
			   }
		}
			
		if(url.getRedirectType() != 0)
		{
			if(url.getRedirectType() == 301)
			{
				redirectType = 301;
			}
		}
		
		List<Url> urlList = urlRepository.findAll();
		for (Url u : urlList)
	    {
			if(u.getUrl().equals(url.getUrl())) {
				urlExistsInDatabase = true;
				Url urlToUpdate = new Url();
				urlToUpdate.setId(u.getId());
				urlToUpdate.setAccountNumber(u.getAccountNumber());
				urlToUpdate.setUrl(u.getUrl());
				urlToUpdate.setShortUrl(u.getShortUrl());
				urlToUpdate.setRedirectType(u.getRedirectType());
				urlToUpdate.setNumCalled(u.getNumCalled());
				urlRepository.save(urlToUpdate);
				shortUrl = u.getShortUrl();
			   }
		}
		
		if(isRegistrationSuccesful)
		{
			
		}
		
	    if (isRegistrationSuccesful){
	    	if(!urlExistsInDatabase)
			{
			shortUrl = shortenerService.convertToShortUrl(url, username, redirectType);
			response.put("shortUrl", shortUrl);
	    	return ResponseEntity.status(HttpStatus.CREATED).body(response);
			}
	    	else 
	    	{
    		response.put("shortUrl", shortUrl);
	    	return ResponseEntity.status(HttpStatus.OK).body(response);
			}
	    }
	    else{
	        response.put("sucess", "false");
	        response.put("description", "Authorization failed");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
	}
	

	@RequestMapping(value = "/statistic/{accountId}", 
	        method = RequestMethod.GET, 
	        produces = MediaType.APPLICATION_JSON_VALUE)
	    @ResponseBody
	    public ResponseEntity<?> getStat(
	    		@RequestHeader (value="Authorization") String authString,
	    		@PathVariable String accountId)
	    		{
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		boolean isRegistrationSuccesful = false;
		String username = "";
		String password = "";
		Pattern pattern = Pattern.compile(":");
		Matcher matcher = pattern.matcher(authString);
		
		if (matcher.find()) {
		    username = authString.substring(0, matcher.start());
		    password = authString.substring(matcher.end());
		}
		
		List<Account> accountList = accountRepository.findAll();
		for (Account a : accountList)
	    {
			if(a.getAccountId().equals(username)) {
				if (a.getPassword().equals(password))
					{
					isRegistrationSuccesful = true;
					break;
					}
			   }
		}

		List<Url> urlList = urlRepository.findAll();
		for (Url u : urlList)
	    {
			if(u.getAccountNumber().equals(accountId)) {
				map.put(u.getUrl(), u.getNumCalled());				
			   }
		}
		
		Map<String,String> response = new LinkedHashMap<String, String>();
		if (isRegistrationSuccesful){
	    	return ResponseEntity.ok(map);
	    }
	    else{
	        response.put("sucess", "false");
	        response.put("description", "Authorization failed");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
	  }
	
	
	@RequestMapping(value = "/{url1:.+}/{url2}", 
	        method = RequestMethod.GET, 
	        produces = MediaType.APPLICATION_JSON_VALUE)
		@ResponseBody
		RedirectView redirect(@PathVariable("url1") String url1, @PathVariable("url2") String url2){
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("http://");
		stringBuilder.append(url1);
		stringBuilder.append("/");
		stringBuilder.append(url2);
		
		String newString = stringBuilder.toString();
		
		List<Url> urlList = urlRepository.findAll();
		String longUrl = "empty";
		int redirectType = 302;
		
		for (Url u : urlList)
	    {
			if(u.getShortUrl().equals(newString)) {
				longUrl = u.getUrl();
				redirectType = u.getRedirectType();
				Url urlToUpdate = new Url();
				urlToUpdate.setId(u.getId());
				urlToUpdate.setAccountNumber(u.getAccountNumber());
				urlToUpdate.setUrl(u.getUrl());
				urlToUpdate.setShortUrl(u.getShortUrl());
				urlToUpdate.setRedirectType(u.getRedirectType());
				urlToUpdate.setNumCalled(u.getNumCalled() + 1);
				urlRepository.save(urlToUpdate);
				break;
			   }
		}

		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(longUrl);
		if(redirectType == 301)
			{
			redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
		}
		else {
			redirectView.setStatusCode(HttpStatus.FOUND);
		}
	    return redirectView;
    }
}


