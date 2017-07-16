package com.wordOrigin.dao;

import com.j256.ormlite.dao.Dao;
import com.wordOrigin.common.DataBaseHelper;
import com.wordOrigin.entity.F01Word;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 智之 on 2015/01/31.
 */
public class WordDao extends BaseDao {

    Dao<F01Word, Long> dao;

    public WordDao(DataBaseHelper dbHelper) {
        super(dbHelper);
        init();
    }

    public void init() {
        try {
            dao = dbHelper.getDao(F01Word.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public F01Word getWordForWordId(int wordId) throws SQLException {
        F01Word word = dao.queryBuilder()
                .where()
                .eq("word_id", wordId)
                .queryForFirst();
        return word;
    }

    public List<F01Word> getWordList(int wordOriginId) throws SQLException {
        List<F01Word> wordList = dao.queryBuilder()
                .orderBy("word_id", true)
                .where()
                .eq("word_origin_id", wordOriginId)
                .query();
        return wordList;
    }

    public List<F01Word> getWordListAll() throws SQLException {
        List<F01Word> wordList = dao.queryBuilder()
                .orderBy("word_id", true)
                .query();
        return wordList;
    }

    public List<F01Word> getWordListWhereWord(String word) throws SQLException {
        List<F01Word> wordList = dao.queryBuilder()
                .orderBy("word_id", true)
                .where()
                .like("word", word + "%")
                .query();
        return wordList;
    }

}
