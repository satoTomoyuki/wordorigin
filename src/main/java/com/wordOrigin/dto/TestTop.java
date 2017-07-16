package com.wordOrigin.dto;

import lombok.Data;

@Data
public class TestTop {
    private int wordOriginId;
    private String wordOriginTranslation;
    private int nowCount;
    private int maxCount;
    private String maxLastTestTime;
}
