package com.wordOrigin.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;

@Data
@DatabaseTable(tableName = "m01_system_param")
public class M01SystemParam {

    @DatabaseField(id = true, columnName = "param_key")
    private String paramKey;

    @DatabaseField(columnName = "param_value")
    private String paramValue;

}
