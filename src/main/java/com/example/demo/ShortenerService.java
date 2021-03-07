package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class ShortenerService {
	
	private final UrlRepository urlRepository;
    
    public ShortenerService (UrlRepository urlRepository)
    {
    	this.urlRepository = urlRepository;
    }
    
    public String convertToShortUrl(Url urlLong, String accountNumber, int redirect) {
    	UrlShortener shortener = new UrlShortener(5, "http://short.com/");
    	String longURL = urlLong.getUrl();
    	if (!longURL.substring(0, 7).equals("http://"))
		{
			if (!longURL.substring(0, 8).equals("https://"))
			{
				longURL = "http://" + longURL;
			}
		}
    	String shortUrl = shortener.shortenURL(longURL);
    	
        Url url = new Url();
        url.setAccountNumber(accountNumber);
        url.setUrl(longURL);
        url.setShortUrl(shortUrl);
        url.setRedirectType(redirect);
        url.setNumCalled(0);
        
        urlRepository.save(url);
        return shortUrl;
    }	
}
