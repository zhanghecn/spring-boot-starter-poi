package com.example.module.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TestTypeEntity {

    private String text;

    private Integer number;

    private float decimal1;

    private double decimal2;

    private BigDecimal decimal;

    private Date date;

    public TestTypeEntity(String text, Integer number, float decimal1, double decimal2, BigDecimal decimal, Date date) {
        this.text = text;
        this.number = number;
        this.decimal1 = decimal1;
        this.decimal2 = decimal2;
        this.decimal = decimal;
        this.date = date;
    }
}
