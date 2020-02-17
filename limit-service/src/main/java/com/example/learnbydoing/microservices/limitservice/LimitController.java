package com.example.learnbydoing.microservices.limitservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learnbydoing.microservices.limitservice.bean.Configuration;

@RestController
public class LimitController {
	
	@Autowired
	private Configuration config;
	
	@GetMapping("/limit")
	public Configuration findConfiguration() {
		
		return new Configuration(config.getMinimum(), config.getMaximum());
		
	}

}
