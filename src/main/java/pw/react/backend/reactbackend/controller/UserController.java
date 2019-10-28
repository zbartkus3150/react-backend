package pw.react.backend.reactbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.reactbackend.errors.ErrorResponse;
import pw.react.backend.reactbackend.errors.UserAlreadyExistsException;
import pw.react.backend.reactbackend.errors.UserNotFoundException;
import pw.react.backend.reactbackend.model.User;
import pw.react.backend.reactbackend.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/user")
	public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String login) {
		List<User> result;
		if (login != null && login.length() > 0)
			result = userService.findByLogin(login);
		else
			result = userService.findAll();
			if(result.isEmpty())result=null;
		if (result == null ) {
			throw new UserNotFoundException("Login: " + login);
		}
		return ResponseEntity.ok(result);
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<User> getUser(@PathVariable(value = "id") int id) {
		User result = userService.findById(id);
		if (result == null) {
			throw new UserNotFoundException("Id: " + id);
		}
		return ResponseEntity.ok(result);
	}

	@PostMapping("/user/")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		if (userService.exists(user)) {
			throw new UserAlreadyExistsException("Login: " + user.getLogin());
		}
		User result = userService.save(user);
		return ResponseEntity.ok(result);
	}

	@ExceptionHandler({UserAlreadyExistsException.class})
	public ResponseEntity<ErrorResponse> alreadyExists(UserAlreadyExistsException ex) {
		return new ResponseEntity<>(
				new ErrorResponse("The user already exists in database"),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({UserNotFoundException.class})
	public ResponseEntity<ErrorResponse> notFound(UserNotFoundException ex) {
		return new ResponseEntity<>(
				new ErrorResponse("The user does not exist in database"),
				HttpStatus.NOT_FOUND);
	}
}