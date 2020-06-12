package com.example.module.model;

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
}
