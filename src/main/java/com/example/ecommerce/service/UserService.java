package com.example.ecommerce.service;

import com.example.ecommerce.model.User;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public interface UserService {

    @NotNull Iterable<User> getAllUsers();

    User create(@NotNull(message = "The order cannot be null.") @Valid User user);

    void update(@NotNull(message = "The order cannot be null.") @Valid User user);
}