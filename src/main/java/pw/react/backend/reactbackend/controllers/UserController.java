package pw.react.backend.reactbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.reactbackend.errors.ErrorResponse;
import pw.react.backend.reactbackend.errors.UserAlreadyExistsException;
import pw.react.backend.reactbackend.errors.UserNotFoundException;
import pw.react.backend.reactbackend.models.User;
import pw.react.backend.reactbackend.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("")
	public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String login) {
		if (login == null)
			return ResponseEntity.ok().body(userService.findAll());
		return ResponseEntity.ok().body(userService.findByLogin(login));
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable(value = "id") int id) {
		return ResponseEntity.ok().body(userService.findById(id));
	}

	@PostMapping("")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		return ResponseEntity.ok().body(userService.create(user));
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable(value = "id") int id, @Valid @RequestBody User user) {
		return ResponseEntity.ok().body(userService.update(id, user));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable(value = "id") int id) {
		return ResponseEntity.ok().body(userService.delete(id));
	}

	@ExceptionHandler({UserAlreadyExistsException.class})
	public ResponseEntity<ErrorResponse> alreadyExists(UserAlreadyExistsException ex) {
		return new ResponseEntity<>(
				new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), "The user already exists"),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({UserNotFoundException.class})
	public ResponseEntity<ErrorResponse> notFound(UserNotFoundException ex) {
		return new ResponseEntity<>(
				new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value(), "The user was not found"),
				HttpStatus.NOT_FOUND);
	}
}