package com.example.ecommerce.controller;

import com.example.ecommerce.dto.UserNameDto;
import com.example.ecommerce.exception.DuplicateUserNameException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/user")
public class UserController {
    UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @NotNull Iterable<User> getUserList() {
        return this.userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody UserNameDto name){

        checkForDuplicate(name.getName());
        User user = new User(name.getName());
        System.out.println(user.getName());
        user = this.userService.create(user);


        String uri = ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/user/{id}")
                .buildAndExpand(user.getId())
                .toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uri);

        return new ResponseEntity<>(user, headers, HttpStatus.CREATED);

    }

    private void checkForDuplicate(String name){
        for(User user : userService.getAllUsers()){
            if(user.getName().equals(name)){
                new DuplicateUserNameException("User " + name + "already exists.");
            }
        }
    }
}
