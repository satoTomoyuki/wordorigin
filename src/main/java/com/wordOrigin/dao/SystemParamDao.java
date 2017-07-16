package com.wordOrigin.dao;

import com.j256.ormlite.dao.Dao;
import com.wordOrigin.common.DataBaseHelper;
import com.wordOrigin.entity.M01SystemParam;

import java.sql.SQLException;

/**
 * Created by 智之 on 2015/01/31.
 */
public class SystemParamDao extends BaseDao {

    Dao<M01SystemParam, Long> dao;

    public SystemParamDao(DataBaseHelper dbHelper) {
        super(dbHelper);
        init();
    }

    public void init() {
        try {
            dao = dbHelper.getDao(M01SystemParam.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertOrUpdate(M01SystemParam m01SystemParam) throws SQLException {
        dao.createOrUpdate(m01SystemParam);
    }

    public String getParamValue(String paramKey) throws SQLException {
        M01SystemParam systemParam = dao.queryBuilder()
                .where()
                .eq("param_key", paramKey)
                .queryForFirst();
        return systemParam.getParamValue();
    }
}
