package com.wordOrigin.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;

@Data
@DatabaseTable(tableName = "f01_test_history")
public class F01TestHistory {

    @DatabaseField(id = true, columnName = "word_id")
    private int wordId;

    @DatabaseField(columnName = "fault_count")
    private int faultCount;

    @DatabaseField(columnName = "last_test_time")
    private String lastTestTime;
}
