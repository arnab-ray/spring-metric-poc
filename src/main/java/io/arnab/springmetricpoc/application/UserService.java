package io.arnab.springmetricpoc.application;

public interface UserService {

    CreateUserResponse addUser(String name, String email);

    UserResponse getUser(String id);
}
