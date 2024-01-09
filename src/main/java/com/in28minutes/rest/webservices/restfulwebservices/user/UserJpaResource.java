package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.in28minutes.rest.webservices.restfulwebservices.jpa.PostRepository;
import com.in28minutes.rest.webservices.restfulwebservices.jpa.UserRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {
	
	private UserRepository userRepository;
	
	private PostRepository postRepository;
	
	public UserJpaResource(UserRepository repository, PostRepository postRepository) {
		this.userRepository = repository;
		this.postRepository = postRepository;
	}

	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		
		if(user==null)
			throw new UserNotFoundException("id:"+id);
				
		/*HATEOAS son las siglas de "Hypermedia As The Engine Of Application State" Especifica que las 
		 * API REST deben proveer de información suficiente al cliente para interactuar con el servidor.
		 * Esto es diferente de los servicios basados en SOAP, donde el cliente y el servidor interactúan 
		 * mediante un contrato fijado.
		 * 
		 * The code below is for that*/
		EntityModel<User> entityModel = EntityModel.of(user.get());
		
		/* Adding the linsk to the others routes of the API.
		 * Instead of adding the the route, it's method associated with that it's added*/
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		entityModel.add(link.withRel("all-users"));
		
		/* All of this is done to add the "_links:" property to the response
		 * example:
		 * {
			"id": 1,
			"name": "Adam",
			"birthDate": "1994-01-07",
			"_links": {
				"all-users": {
				"href": "http://localhost:8080/users"
					}
				}
			}*/
		return entityModel;
	}
	
	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userRepository.deleteById(id);
	}

	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrieveUserPosts(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
				
		if(user==null)
			throw new UserNotFoundException("id:"+id);
		return user.get().getPosts();
	}

	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
				
		User savedUser = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{id}")
						.buildAndExpand(savedUser.getId())
						.toUri();   
		
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Post> createUserPosts(@PathVariable int id, @Valid @RequestBody Post post) {
		Optional<User> user = userRepository.findById(id);
				
		if(user==null)
			throw new UserNotFoundException("id:"+id);
		post.setUser(user.get());
		
		Post savedPost = postRepository.save(post);
		
		/*What the code below does is to add a property in the header that show a url that shows the 
		 * post created
		 * 
		 * Example:
		 * 
		 * HEADERS
			pretty 
			Location:	http://localhost:8080/jpa/users/10001/posts/2
			Content-Length:	0 byte
			Date:	
			Tue, 09 Jan 2024 01:08:52 GMT
			Keep-Alive:	timeout=60
			Connection:	keep-alive
			
			*/
		
		URI location = ServletUriComponentsBuilder
				/*Adds the current url
				 * In this case is: http://localhost:8080/jpa/users/10001/posts
				 */
				.fromCurrentRequest()
				/*Adds the path needed to access the url that each individual post*/
				.path("/{id}")
				/*Adding the id to the url of the post created*/
				.buildAndExpand(savedPost.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
}
