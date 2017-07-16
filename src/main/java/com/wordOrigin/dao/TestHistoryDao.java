package com.wordOrigin.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.wordOrigin.common.DataBaseHelper;
import com.wordOrigin.dto.TestResult;
import com.wordOrigin.dto.TestTop;
import com.wordOrigin.entity.F01TestHistory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 智之 on 2015/01/31.
 */
public class TestHistoryDao extends BaseDao {

    public TestHistoryDao(DataBaseHelper dbHelper) {
        super(dbHelper);
        init();
    }

    Dao<F01TestHistory, Long> dao;

    public void init() {
        try {
            dao = dbHelper.getDao(F01TestHistory.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertOrUpdate(F01TestHistory f01TestHistory) throws SQLException {
        dao.createOrUpdate(f01TestHistory);
    }

    public void deleteAll() throws SQLException {
        DeleteBuilder<F01TestHistory, Long> builder = dao.deleteBuilder();
        builder.delete();
    }


    /**
     * テスト対象一覧の表示
     */
    public List<TestTop> getTestList(boolean completeDisplayFlag) throws SQLException {
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT");
        sql.append("   word_ori.word_origin_id,");
        sql.append("   word_ori.word_origin_translation,");
        sql.append("   COUNT(test.word_id) AS now_count,");
        sql.append("   COUNT(word.word_id) AS max_count,");
        sql.append("   MAX(last_test_time) AS max_last_test_time");
        sql.append(" FROM");
        sql.append("   f01_word_origin word_ori");
        sql.append("   INNER JOIN f01_word word");
        sql.append("     ON word_ori.word_origin_id = word.word_origin_id");
        sql.append("   LEFT OUTER JOIN f01_test_history test");
        sql.append("     ON test.word_id = word.word_id");
        sql.append("        AND test.fault_count = 0");
        sql.append("        AND test.fault_count = 0");
        sql.append(" GROUP BY");
        sql.append("   word_ori.word_origin_id");
        if(!completeDisplayFlag){
            sql.append(" HAVING");
            sql.append("   COUNT(test.word_id) != COUNT(word.word_id)");
        }
        sql.append(" ORDER BY ");
        sql.append("   word_ori.word_origin_id");
        Cursor cursor = sdb.rawQuery(sql.toString(), null);

        List<TestTop> testTopList = new ArrayList<>();
        for (boolean next = cursor.moveToFirst(); next; next = cursor.moveToNext()) {
            TestTop testTop = new TestTop();
            testTop.setWordOriginId(cursor.getInt(cursor.getColumnIndex("word_origin_id")));
            testTop.setWordOriginTranslation(cursor.getString(cursor.getColumnIndex("word_origin_translation")));
            testTop.setNowCount(cursor.getInt(cursor.getColumnIndex("now_count")));
            testTop.setMaxCount(cursor.getInt(cursor.getColumnIndex("max_count")));
            testTop.setMaxLastTestTime(cursor.getString(cursor.getColumnIndex("max_last_test_time")));

            testTopList.add(testTop);
        }
        return testTopList;
    }

    /**
     * テスト全体の達成度
     */
    public int[] getTotalAchievement() throws SQLException {
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        String sql =
                "SELECT" +
                        " COUNT(test.word_id) AS now_count," +
                        " COUNT(word.word_id) AS max_count" +
                        " FROM" +
                        " f01_word word" +
                        " LEFT OUTER JOIN f01_test_history test" +
                        " ON test.word_id = word.word_id" +
                        " AND test.fault_count = 0";

        Cursor cursor = sdb.rawQuery(sql, null);

        //1件でもカーソルを動かす
        cursor.moveToFirst();
        int nowCount = cursor.getInt(cursor.getColumnIndex("now_count"));
        int maxCount = cursor.getInt(cursor.getColumnIndex("max_count"));

        return new int[]{nowCount, maxCount};
    }

    /**
     * テスト結果一覧の表示
     */
    public List<TestResult> getTestResultList(int wordOriginId) throws SQLException {
        SQLiteDatabase sdb = dbHelper.getReadableDatabase();
        String sql =
                "SELECT" +
                        " word.word_id," +
                        " word.word," +
                        " test.fault_count" +
                        " FROM" +
                        " f01_word word" +
                        " INNER JOIN f01_test_history test" +
                        " ON word.word_id = test.word_id" +
                        " WHERE" +
                        " word.word_origin_id = ?" +
                        " ORDER BY" +
                        " word.word_id";
        Cursor cursor = sdb.rawQuery(sql, new String[]{String.valueOf(wordOriginId)});

        List<TestResult> testResultList = new ArrayList<>();
        for (boolean next = cursor.moveToFirst(); next; next = cursor.moveToNext()) {
            TestResult testResult = new TestResult();
            testResult.setWordId(cursor.getInt(cursor.getColumnIndex("word_id")));
            testResult.setWord(cursor.getString(cursor.getColumnIndex("word")));

            int faultCount = cursor.getInt(cursor.getColumnIndex("fault_count"));
            if (faultCount == 0) {
                //正解
                testResult.setResult(true);
            } else {
                testResult.setResult(false);
            }

            testResultList.add(testResult);
        }
        return testResultList;
    }
}
