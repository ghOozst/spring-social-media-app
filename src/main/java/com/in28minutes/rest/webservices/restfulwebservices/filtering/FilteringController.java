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
	@GetMapping("filtering")
	public MappingJacksonValue filtering() {
		SomeBean someBean = new SomeBean("Value1", "Value2", "Value3");
		
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBean);
		
		/*This code is used to create dynamic filters.
		 * In this case the filter removes all properties but the ones selected*/
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field1", "field3");
		FilterProvider filters = 
				new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
		mappingJacksonValue.setFilters(filters);
		
		return mappingJacksonValue;
	}
	
	@GetMapping("filtering-list")
	public MappingJacksonValue filteringList() {
		List<SomeBean> beans = Arrays.asList(
				new SomeBean("Value1", "Value2", "Value3"),
				new SomeBean("Value4", "Value5", "Value6"),
				new SomeBean("Value7", "Value8", "Value9")
				);
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(beans);
		
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field2", "field3");
		FilterProvider filters = new SimpleFilterProvider().addFilter("SomeBeanFilter", filter);
		mappingJacksonValue.setFilters(filters);
		
		return mappingJacksonValue;
	}
}
