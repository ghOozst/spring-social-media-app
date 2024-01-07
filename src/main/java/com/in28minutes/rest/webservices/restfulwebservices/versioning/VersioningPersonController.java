package com.in28minutes.rest.webservices.restfulwebservices.versioning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersioningPersonController {
/* URL VERSIONING
 * You can use which version of the api making use of the path
 * In this case is the ../V?/.. path
 * http://localhost:8080/v2/person*/
	@GetMapping("/v1/person")
	public PersonV1 get1stVersionOfPerson() {
		return new PersonV1("Bob Charlie");
	}

	@GetMapping("/v2/person")
	public PersonV2 get2ndVersionOfPerson() {
		return new PersonV2(new Name("Bob", "Charlie"));
	}
	
/*REQUEST PARAM VERSIONING
 * http://localhost:8080/person?version=1*/
	@GetMapping(path="/person", params="version=1")
	public PersonV1 get1stVersionOfPersonResquestParam() {
		return new PersonV1("Bob Charlie");
	}
	
	@GetMapping(path="/person", params="version=2")
	public PersonV2 get2ndVersionOfPersonRequestParam() {
		return new PersonV2(new Name("Bob", "Charlie"));
	}

/*CUSTOM HEARDER REQUEST VERSIONING
 * Before Making the request add "X-API-VERSION" and its value must be the number of 
 * the version of the API*/
	
	@GetMapping(path="/person/header", headers="X-API-VERSION=1")
	public PersonV1 get1stVersionOfPersonResquestHeader() {
		return new PersonV1("Bob Charlie");
	}
	
	@GetMapping(path="/person/header", headers="X-API-VERSION=2")
	public PersonV2 get2ndVersionOfPersonRequestHeader() {
		return new PersonV2(new Name("Bob", "Charlie"));
	}

/*MEDIA TYPE VERSIONING
 * It's the same as the header quest versioning, but the name of the header must be accept and
 * the  value must be something like this "application/vnd.company.app-v1+json".
 * Don't know why
 * 
 * example: accept:application/vnd.company.app-v1+json*/
	
	@GetMapping(path="/person/accept", produces="application/vnd.company.app-v1+json")
	public PersonV1 get2ndVersionOfPersonAcceptHeader() {
		return new PersonV1("Bob Charlie");
	}
	
	@GetMapping(path="/person/accept", produces="application/vnd.company.app-v2+json")
	public PersonV2 get2ndVersionOfPersonAceptHeader() {
		return new PersonV2(new Name("Bob", "Charlie"));
	}
}
