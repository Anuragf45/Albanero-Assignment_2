package com.springsecurity.springsecuritydemo.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class CsvData {
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
//    private String column1;
//    private String column2;

}
