package io.arnab.springmetricpoc.application;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(String name, String email) {}
