package com.example;

import com.zhanghe.poi.autoconfig.annotation.ExcelConfigurationScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ExcelConfigurationScanner(packages = "com.example.module.model.dto",templateLocation = "template")
public class ExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class);
    }
}
