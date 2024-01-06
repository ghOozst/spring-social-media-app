package com.in28minutes.rest.webservices.restfulwebservices.helloworld;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
	
	private MessageSource messageSource;
	
	public HelloWorldController(MessageSource messageSource) {
		super();
		this.messageSource = messageSource;
	}

	@GetMapping(path = "/hello-world")
	public String helloWorld() {
		return "Hello World"; 
	}
	
	@GetMapping(path = "/hello-world-bean")
	public HelloWorldBean helloWorldBean() {
		return new HelloWorldBean("Hello World"); 
	}
	
	// Path Parameters
	// /users/{id}/todos/{id}  => /users/2/todos/200
	// /hello-world/path-variable/{name}
	// /hello-world/path-variable/Ranga

	@GetMapping(path = "/hello-world/path-variable/{name}")
	public HelloWorldBean helloWorldPathVariable(@PathVariable String name) {
		return new HelloWorldBean(String.format("Hello World, %s", name)); 
	}

	@GetMapping(path = "/hello-world-internationalized")
	public String helloWorldInternationalized() {
		//Getting the language indication from the header
		Locale locale = LocaleContextHolder.getLocale();
		
		return messageSource.getMessage(
				/*Name of the variable in the file messages.properties that is located in the
				 * resources folder
				   Automatically is going to get the name of other files for the rest of the languages
				 * by just adding "_(language)"
				 * example:
				 * fr = french
				 * 
				 * filename= good.morning.message_fr.properties
				 * */
				"good.morning.message", 
				null, 
//				Indicating this is the default message
				"Default Message", 
				//Inserting the language of the message
				locale);
	}	
}
