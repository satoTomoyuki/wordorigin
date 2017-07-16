package com.wordOrigin.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;

@Data
@DatabaseTable(tableName = "f01_word_origin")
public class F01WordOrigin {

    @DatabaseField(id = true, columnName = "word_origin_id")
    private int wordOriginId;

    @DatabaseField(columnName = "word_origin_word")
    private String wordOriginWord;

    @DatabaseField(columnName = "word_origin_translation")
    private String wordOriginTranslation;

    @DatabaseField(columnName = "sequence")
    private int sequence;

}
