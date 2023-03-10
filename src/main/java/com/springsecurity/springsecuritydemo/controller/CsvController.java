package com.springsecurity.springsecuritydemo.controller;

import com.springsecurity.springsecuritydemo.Model.CsvData;
import com.springsecurity.springsecuritydemo.repository.CsvDataRepository;
import com.springsecurity.springsecuritydemo.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
//@RequestMapping("/csv")
public class CsvController {

    @Autowired
    private CsvDataRepository csvDataRepository;

    @Autowired
    private CsvService csvService;

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            // read the CSV file and save the data to MongoDB
            InputStream is = file.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                CsvData csvData = new CsvData();
               csvData.setName(parts[0]);
                csvData.setEmail(parts[1]);
                // set other columns as needed
                csvDataRepository.save(csvData);
            }
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> handleFileDownload() {
        // get the CSV data from MongoDB and generate a CSV file
        List<CsvData> csvDataList = csvDataRepository.findAll();
        StringBuilder csvData = new StringBuilder();
        for (CsvData csv : csvDataList) {
            csvData.append(csv.getName()).append(",").append(csv.getEmail()).append("\n");
            // add other columns as needed
        }

        // create a temporary file and write the CSV data to it
        File tempFile = null;
        try {
            tempFile = File.createTempFile("csv", ".csv");
            FileWriter writer = new FileWriter(tempFile);
            writer.write(csvData.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // return the CSV file as a Resource
        Path path = tempFile.toPath();

        ByteArrayResource resource = null;
        try {
            resource = new ByteArrayResource(Files.readAllBytes((java.nio.file.Path) path));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(tempFile.length())
                .body(resource);
    }

    @PostMapping("/sort")
    public ResponseEntity<List<CsvData>> sortCsv(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("column") String column,
                                                  @RequestParam("order") String order) throws Exception {
        List<CsvData> sortedData = csvService.sortCsvData(file, column, order);
        return ResponseEntity.ok().body(sortedData);
    }
}

