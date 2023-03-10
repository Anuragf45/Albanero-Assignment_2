package com.springsecurity.springsecuritydemo.repository;

import com.springsecurity.springsecuritydemo.Model.CsvData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsvDataRepository extends MongoRepository<CsvData, String> {
}
