package com.in28minutes.rest.webservices.restfulwebservices.filtering;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@RestController
public class FilteringController {
	
	@GetMapping("/filtering")
	public MappingJacksonValue retrieveSomeBean() {
		SomeBean bean = new SomeBean("value1", "value2", "value3");
		SimpleBeanPropertyFilter filter = 
				SimpleBeanPropertyFilter.filterOutAllExcept("value1", "value2");
		FilterProvider filters = new SimpleFilterProvider()
				.addFilter("sbfilter", filter);
		
		MappingJacksonValue mapping = new MappingJacksonValue(bean);
		mapping.setFilters(filters);
		
		return mapping;
	}
	
	@GetMapping("/filtering-list")
	public MappingJacksonValue retrieveListOfSomeBeans() {
		List<SomeBean> list = Arrays.asList( new SomeBean("value1", "value2", "value3"),
				new SomeBean("value4", "value5", "value6"));
		
		SimpleBeanPropertyFilter filter = 
				SimpleBeanPropertyFilter.filterOutAllExcept("value2", "value3");
		FilterProvider filters = new SimpleFilterProvider()
				.addFilter("sbfilter", filter);
		
		MappingJacksonValue mapping = new MappingJacksonValue(list);
		mapping.setFilters(filters);
		
		return mapping;
	}
}
