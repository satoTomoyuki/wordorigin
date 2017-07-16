package com.wordOrigin.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class TestResult implements Serializable {
    private int wordId;
    private String word;
    private boolean result;
}
