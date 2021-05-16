package com.covidHelp.demo.repository;

import java.util.Optional;
import java.util.UUID;

import com.covidHelp.demo.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findByUserName(String userName);
}
