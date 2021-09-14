package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
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


@RestController
public class UserJPAResource {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PostRepository postRepo;

	
	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers() {
		return userRepo.findAll();
	}	
	
	@GetMapping("/jpa//users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		Optional<User> user = userRepo.findById(id);
		if (!user.isPresent()) {
			throw new UserNotFoundException("id-" + id);
		}
		
		EntityModel<User> resource = EntityModel.of(user.get());
		WebMvcLinkBuilder linkTo = 
				linkTo(methodOn(this.getClass()).retrieveAllUsers());
		
		resource.add(linkTo.withRel("all-users"));
		
		return resource;
	}
	
	@PostMapping("/jpa//users")
	public ResponseEntity<Object> CreateUser(@Valid @RequestBody User user) {
		User savedUser = userRepo.save(user);
		
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedUser.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/jpa//users/{id}")
	public void deleteUser(@PathVariable int id) {
		userRepo.deleteById(id);		
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrieveUserPosts(@PathVariable int id) {
		Optional<User> optionals = userRepo.findById(id);
		
		if (!optionals.isPresent()) {
			throw new UserNotFoundException("id-" + id);
		}
		
		return optionals.get().getPosts();	
	}	
	
	@PostMapping("/jpa//users/{id}/posts")
	public ResponseEntity<Object> CreatePost(@PathVariable int id, @RequestBody Post post) {
		Optional<User> userOpt = userRepo.findById(id);
		
		if (!userOpt.isPresent()) {
			throw new UserNotFoundException("id-" + id);
		}
		
		User user = userOpt.get();
		
		post.setUser(user);
		postRepo.save(post);
		
		
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(post.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
}
