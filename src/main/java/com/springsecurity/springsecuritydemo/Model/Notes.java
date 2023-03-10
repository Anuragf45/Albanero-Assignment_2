package com.springsecurity.springsecuritydemo.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Notes {
    @Id
   private String id;
    private  String userId;
    private String title;
    private List<String> tags;
    private String description;
}
