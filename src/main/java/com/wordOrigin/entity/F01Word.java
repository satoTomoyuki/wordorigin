package com.wordOrigin.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import lombok.Data;

@Data
@DatabaseTable(tableName = "f01_word")
public class F01Word implements Serializable {

    @DatabaseField(id = true, columnName = "word_id")
    private int wordId;

    @DatabaseField(columnName = "word_origin_id")
    private int wordOriginId;

    @DatabaseField(columnName = "svl")
    private int svl;

    @DatabaseField(columnName = "base_word")
    private String baseWord;

    @DatabaseField(columnName = "word")
    private String word;

    @DatabaseField(columnName = "pronunciation")
    private String pronunciation;

    @DatabaseField(columnName = "word_origin")
    private String wordOrigin;

    @DatabaseField(columnName = "association")
    private String association;

    @DatabaseField(columnName = "translation")
    private String translation;

    @DatabaseField(columnName = "remark")
    private String remark;
}
