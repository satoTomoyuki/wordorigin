package com.wordOrigin.common;

import android.app.Application;

import com.wordOrigin.dao.SystemParamDao;
import com.wordOrigin.dao.TestHistoryDao;
import com.wordOrigin.dao.WordDao;
import com.wordOrigin.dao.WordOriginDao;
import com.wordOrigin.entity.F01Word;
import com.wordOrigin.entity.F01WordOrigin;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Globals extends Application {
    private DataBaseHelper dbHelper;
    private TestHistoryDao testHistoryDao;
    private WordDao wordDao;
    private WordOriginDao wordOriginDao;
    private SystemParamDao systemParamDao;
    private List<F01Word> wordListAll;
    private List<F01WordOrigin> wordOriginListAll;

    //ぜんぶ初期化するメソッド
    public void GlobalsAllInit() {
        try {
            dbHelper = new DataBaseHelper(this);
            dbHelper.getWritableDatabase();

            testHistoryDao = new TestHistoryDao(dbHelper);
            wordDao = new WordDao(dbHelper);
            wordOriginDao = new WordOriginDao(dbHelper);
            systemParamDao = new SystemParamDao(dbHelper);

            //全単語情報の取得
            wordListAll = wordDao.getWordListAll();
            wordOriginListAll = wordOriginDao.getAllList();

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}