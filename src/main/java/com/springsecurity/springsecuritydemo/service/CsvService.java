package com.springsecurity.springsecuritydemo.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.springsecurity.springsecuritydemo.Model.CsvData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CsvService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<CsvData> sortCsvData(MultipartFile file, String column, String order) throws Exception {
        List<CsvData> csvData = readCsvData(file);

        Comparator<CsvData> comparator = getComparator(column, order);
        Collections.sort(csvData, comparator);

        mongoTemplate.insertAll(csvData);
        return csvData;
    }

    private List<CsvData> readCsvData(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        CsvToBean<CsvData> csvToBean = new CsvToBeanBuilder<CsvData>(reader)
                .withType(CsvData.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        return csvToBean.parse();
    }

    private Comparator<CsvData> getComparator(String column, String order) {
        Comparator<CsvData> comparator = null;

        switch (column) {
            case "name":
                comparator = Comparator.comparing(CsvData::getName);
                break;
            case "email":
                comparator = Comparator.comparing(CsvData::getEmail);
                break;
            case "phone":
                comparator = Comparator.comparing(CsvData::getPhone);
                break;
            default:
                comparator = Comparator.comparing(CsvData::getId);
                break;
        }

        if (order.equalsIgnoreCase("decending")) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}
