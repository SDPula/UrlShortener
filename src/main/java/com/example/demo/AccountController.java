package com.example.demo;

import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;


@RestController
public class AccountController {
	
	private final AccountRepository repository;
	@PersistenceContext
	public EntityManager em;
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();
	
	public AccountController(AccountRepository repository) {
		this.repository = repository;
	}
	
	
	@RequestMapping(value = "/account", 
	        method = RequestMethod.POST, 
	        produces = MediaType.APPLICATION_JSON_VALUE)
	    @ResponseBody
	    public ResponseEntity<Map<String,String>> getData(@RequestBody String myAccountId){
		
		List<Account> list = repository.findAll();
		
		String nameFound = "";
		String nameFound2 = "";
		String nameFound3 = "";
		String nameFromMethod = myAccountId.replace("{\"account\":\"", "");
		String nameFromMethod2 = nameFromMethod.replace("\"}", "");
		
		boolean isAccountRegistered = false;
		
		if(list.size() > 0)
		{
			for(int i = 0; i < list.size(); i++)
			{
				 nameFound = list.get(i).getAccountId().toString();
				 
				 nameFound2 = nameFound.replace("{\\\"account\\\":\\\"", "");
				 nameFound3 = nameFound2.replace("\\\"}", "");
				 
				 if(nameFound3.equals(nameFromMethod2))
				 {
					 isAccountRegistered = true;
					 break;
				 }			 
			}
		}

		Map<String,String> response = new LinkedHashMap<String, String>();
		
	    if (!isAccountRegistered){
		    if(nameFromMethod2.equals(""))
		    {
		    	response.put("sucess", "false");
		        response.put("description", "Field must contain at least one letter");
		        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
		        
		    }else {
				String randomString = "";
		    	StringBuilder sb = new StringBuilder(7);
		    	for(int i = 0; i < 8; i++)
		    	{
		    	      sb.append(AB.charAt(rnd.nextInt(AB.length())));
		    	}
		    	randomString = sb.toString();
		    	
		    	Account newAccount = new Account();
		    	newAccount.setAccountNumber(nameFromMethod2);
		    	newAccount.setPassword(randomString);
		    	repository.save(newAccount);
		    	
		    	response.put("sucess", "true");
		    	response.put("description", "Registration sucessful");
		    	response.put("password", newAccount.getPassword());
		        return ResponseEntity.status(HttpStatus.CREATED).body(response);
			}    	
	    }
	    else{
	        response.put("sucess", "false");
	        response.put("description", "User with that ID already exists");
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	    }
	}
	
	
	@RequestMapping(value = "/help", method = RequestMethod.GET)
    public RedirectView redirectHelp() {
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("http://localhost:8080/help.html");
	    return redirectView;
    }
}
