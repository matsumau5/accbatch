package com.product.springbatch.batch.job1;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DocumentInfo {
    private String id;
    private int year;
    private int month;
    private String documentId;
    private BigDecimal amount;
}
