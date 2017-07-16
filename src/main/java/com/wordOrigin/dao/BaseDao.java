package com.wordOrigin.dao;

import com.wordOrigin.common.DataBaseHelper;

/**
 * Created by 智之 on 2015/01/31.
 */
public class BaseDao {
    public DataBaseHelper dbHelper;

    public BaseDao(DataBaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

}
