package io.arnab.springmetricpoc.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateUserRequest(String name, String email) {
}
