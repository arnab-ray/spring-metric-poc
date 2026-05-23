package io.arnab.springmetricpoc.application.impl;

import io.arnab.springmetricpoc.application.CreateUserResponse;
import io.arnab.springmetricpoc.application.UserResponse;
import io.arnab.springmetricpoc.application.UserService;
import io.arnab.springmetricpoc.application.latency.MeasureLatency;
import io.arnab.springmetricpoc.domain.User;
import io.arnab.springmetricpoc.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    @MeasureLatency("user.add")
    @SneakyThrows
    public CreateUserResponse addUser(String name, String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        var newUser = new User(name, email);
        Thread.sleep(200); // Simulate a delay
        userRepository.save(newUser);
        return new CreateUserResponse(newUser.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(String id) {
        return userRepository.findById(id)
                .map(user -> new UserResponse(user.getName(), user.getEmail()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
