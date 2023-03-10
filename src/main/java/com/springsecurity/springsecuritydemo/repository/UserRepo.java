package com.springsecurity.springsecuritydemo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.springsecurity.springsecuritydemo.Model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, String> {
    @Query
    User findByuserName(String UserName);
}
