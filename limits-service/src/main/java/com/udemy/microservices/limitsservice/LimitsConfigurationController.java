package com.udemy.microservices.limitsservice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.udemy.microservices.limitsservice.bean.LimitConfiguration;

@RestController
public class LimitsConfigurationController {
	
	public final static Log LOG = LogFactory.getLog(LimitsConfigurationController.class);

	@Autowired
	private Configuration configuration;
	
	@GetMapping("/limits")
	public LimitConfiguration retrieveLimitsFromConfiguration() {
		LOG.info("Properties: "+configuration.toString());
		return new LimitConfiguration(configuration.getMaximum(),configuration.getMinimum());

	}
	@GetMapping("/fault-tolerance-example")
	@HystrixCommand(fallbackMethod="fallbackretrieveConfiguration")
	public LimitConfiguration retrieveConfiguration() {
		throw new RuntimeException("No disponible: Deyviz Perez");
	}
	
	public LimitConfiguration fallbackretrieveConfiguration() {
		return new LimitConfiguration(999,9);
	}
}
