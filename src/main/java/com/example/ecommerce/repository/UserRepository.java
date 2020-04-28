package com.example.ecommerce.repository;

import com.example.ecommerce.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}