package com.springsecurity.springsecuritydemo.Model;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "assignment_Albanero")
public class User {
    @Id
    String id;
    String name;
    String userName;
    String password;
    List<String> roles;
}
