package com.wordOrigin.dao;

import com.j256.ormlite.dao.Dao;
import com.wordOrigin.common.DataBaseHelper;
import com.wordOrigin.entity.F01WordOrigin;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 智之 on 2015/01/31.
 */
public class WordOriginDao extends BaseDao {

    Dao<F01WordOrigin, Long> dao;

    public WordOriginDao(DataBaseHelper dbHelper) {
        super(dbHelper);
        init();
    }

    public void init() {
        try {
            dao = dbHelper.getDao(F01WordOrigin.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public F01WordOrigin getWordOriginForWordOriginId(int wordOriginId) throws SQLException {
        F01WordOrigin wordOrigin = dao.queryBuilder()
                .where()
                .eq("word_origin_id", wordOriginId)
                .queryForFirst();
        return wordOrigin;
    }

    public List<F01WordOrigin> getAllList() throws SQLException {
        return dao.queryForAll();
    }
}
