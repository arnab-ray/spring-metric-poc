package io.arnab.springmetricpoc.infrastructure;

import io.arnab.springmetricpoc.application.UserResponse;
import io.arnab.springmetricpoc.application.UserService;
import io.arnab.springmetricpoc.application.CreateUserRequest;
import io.arnab.springmetricpoc.application.CreateUserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public CreateUserResponse createUser(@RequestBody CreateUserRequest user) {
        return userService.addUser(user.name(), user.email());
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable String id) {
        return userService.getUser(id);
    }
}
