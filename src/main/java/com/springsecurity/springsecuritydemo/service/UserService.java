package com.springsecurity.springsecuritydemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springsecurity.springsecuritydemo.Model.User;
import com.springsecurity.springsecuritydemo.repository.UserRepo;

@Service
public class UserService {
    @Autowired
    UserRepo db;
    @Autowired
    PasswordEncoder pe;

    public ResponseEntity<?> addUser(User user) {
        user.setPassword(pe.encode(user.getPassword()));


        Optional<User> optional1= Optional.ofNullable(db.findByuserName(user.getUserName()));

        if(optional1.isPresent()){
            return new ResponseEntity<>("User already exists with this userName", HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(db.save(user),HttpStatus.CREATED);
        }

    }

    public List<User> getAllusers() {
        return db.findAll();
    }

    public User getuser(String userName) {
        return db.findByuserName(userName);
    }
}
