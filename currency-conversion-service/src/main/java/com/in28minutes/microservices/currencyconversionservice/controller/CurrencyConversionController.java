package com.in28minutes.microservices.currencyconversionservice.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.in28minutes.microservices.currencyconversionservice.CurrencyExchangeServiceProxy;
import com.in28minutes.microservices.currencyconversionservice.bean.CurrencyConversionBean;

@RestController
public class CurrencyConversionController {

	@Autowired
	private CurrencyExchangeServiceProxy proxy;

	public static final Log LOG = LogFactory.getLog(CurrencyConversionController.class);

	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean converCurrency(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);

		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class,
				uriVariables);
		CurrencyConversionBean response = responseEntity.getBody();

		CurrencyConversionBean currencyConversion = new CurrencyConversionBean(response.getId(), from, to,
				response.getConversionMultiple(), quantity, quantity.multiply(response.getConversionMultiple()),
				response.getPort());

		return currencyConversion;
		// CurrencyConversionBean currencyConversion =
//				new CurrencyConversionBean(1L, from, to, BigDecimal.ONE, quantity, quantity, 0);
	}
	
	
	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {
		
			CurrencyConversionBean response = proxy.retrieveExchangeValue(from, to);
			LOG.info("response: "+response);
			CurrencyConversionBean currencyConversion = new CurrencyConversionBean(response.getId(), from, to,
				response.getConversionMultiple(), quantity, quantity.multiply(response.getConversionMultiple()),
				response.getPort());
			
		return currencyConversion;
	}
}