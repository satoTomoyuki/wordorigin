package com.wordOrigin.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.wordOrigin.MainActivity;
import com.wordOrigin.R;
import com.wordOrigin.constant.WordOriginConst;
import com.wordOrigin.dao.TestHistoryDao;
import com.wordOrigin.dto.TestTop;
import com.wordOrigin.entity.M01SystemParam;
import com.wordOrigin.util.WordOriginUtil;

import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.adlantis.android.AdlantisView;


public class TestTopActivity extends BaseActivity implements AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {
    List<TestTop> testList;
    TestHistoryDao testHistoryDao;
    Resources resource;

    public void onCreate(Bundle savedInstanceState) {
        //ヘッダータイトルの設定
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_top);
        resource = getResources();
        //ヘッダの設定
        headerButtonSet(R.string.titleTest);

        try {
            AdlantisView adView = (AdlantisView) findViewById(R.id.adView);
            adView.setPublisherID(resource.getString(R.string.publisherID));

            testHistoryDao = getGlobals().getTestHistoryDao();

            //全体進捗
            int[] totalAchievement = testHistoryDao.getTotalAchievement();
            TextView achievementRateText = (TextView) findViewById(R.id.achievementRate);
            achievementRateText.setText(Html.fromHtml(WordOriginUtil.getAchievementForView(totalAchievement[0], totalAchievement[1])));

            //トグル
            Switch completeWordOriginSwitch = (Switch) findViewById(R.id.completeWordOriginSwitch);
            completeWordOriginSwitch.setOnCheckedChangeListener(this);
            boolean completeDisplay = Boolean.valueOf(getGlobals().getSystemParamDao().getParamValue(WordOriginConst.COMPLETE_DISPLAY_KEY));
            completeWordOriginSwitch.setChecked(completeDisplay);
            setData(testHistoryDao.getTestList(completeDisplay));

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setData(List<TestTop> testList) throws SQLException{
        this.testList = testList;

        ArrayList<CharSequence> dataList = new ArrayList<CharSequence>();
        for (TestTop entity : testList) {
            int nowCount = entity.getNowCount();
            int maxCount = entity.getMaxCount();

            StringBuilder testListTitle = new StringBuilder();
            testListTitle.append(resource.getString(R.string.no));
            testListTitle.append(String.valueOf(entity.getWordOriginId()));
            testListTitle.append(" ");
            testListTitle.append(entity.getWordOriginTranslation());
            testListTitle.append("<BR>");

            testListTitle.append("  ");
            testListTitle.append(resource.getString(R.string.achievementRate));
            testListTitle.append(WordOriginUtil.getAchievementForView(nowCount, maxCount));

            if (StringUtils.isNotBlank(entity.getMaxLastTestTime())) {
                testListTitle.append("<BR>");
                testListTitle.append("  ");
                testListTitle.append(resource.getString(R.string.lastTestTime));
                testListTitle.append(entity.getMaxLastTestTime());
            }
            dataList.add(Html.fromHtml(testListTitle.toString()));
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.test_top_list_item, dataList);

        ListView varListView = (ListView) findViewById(R.id.testList);
        varListView.setAdapter(adapter);
        varListView.setOnItemClickListener(this);
    }


    public void onItemClick(
            AdapterView<?> parent,
            View view, int position, long id) {

        Resources resource = getResources();
        TestTop target = testList.get(position);

        // 確認ダイアログの生成
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setTitle("確認");
        StringBuilder sb = new StringBuilder();
        sb.append("「");
        sb.append(resource.getString(R.string.no));
        sb.append(String.valueOf(target.getWordOriginId()));
        sb.append(" ");
        sb.append(target.getWordOriginTranslation());
        sb.append("」の試験を開始します。宜しいですか？");

        alertDlg.setMessage(sb.toString());
        alertDlg.setPositiveButton(
                "開始",
                new DialogInterface.OnClickListener() {
                    private Integer targetWordOriginId = null;
                    //無名クラスが外側から変数を受け取るためのsetter
                    public DialogInterface.OnClickListener setTargetWordOriginId(Integer targetWordOriginId) {
                        this.targetWordOriginId = targetWordOriginId;
                        return this;
                    }

                    //クリック時の処理
                    public void onClick(DialogInterface dialog, int which) {
                        Intent newIntent = new Intent(getApplicationContext(), TestMainActivity.class);
                        newIntent.putExtra(WordOriginConst.INTENT_KEY_WORD_ORIGIN_ID, targetWordOriginId);
                         startActivity(newIntent);
                    }
                }.setTargetWordOriginId(target.getWordOriginId()));

        alertDlg.setNegativeButton(
                "中止",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel ボタンクリック処理
                    }
                });
        // 表示
        alertDlg.create().show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(newIntent);
            return true;
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        M01SystemParam systemParam = new M01SystemParam();
        try {
            if (isChecked == true) {
                systemParam.setParamValue("true");
                setData(testHistoryDao.getTestList(true));
            } else {
                systemParam.setParamValue("false");
                setData(testHistoryDao.getTestList(false));
            }

            systemParam.setParamKey(WordOriginConst.COMPLETE_DISPLAY_KEY);
            getGlobals().getSystemParamDao().insertOrUpdate(systemParam);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

