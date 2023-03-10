package com.springsecurity.springsecuritydemo.repository;

import com.springsecurity.springsecuritydemo.Model.Notes;
import com.springsecurity.springsecuritydemo.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoRepo extends MongoRepository<Notes,String> {
    @Query
    Notes findByTitle(String title);

    @Query
    Notes findByUserId(String userId);
}
