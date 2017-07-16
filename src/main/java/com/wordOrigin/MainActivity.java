package com.wordOrigin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wordOrigin.activity.BaseActivity;
import com.wordOrigin.activity.SettingActivity;
import com.wordOrigin.activity.TestTopActivity;
import com.wordOrigin.activity.WordOriginListActivity;
import com.wordOrigin.activity.WordSearchActivity;
import com.wordOrigin.constant.WordOriginConst;
import com.wordOrigin.dao.SystemParamDao;
import com.wordOrigin.entity.M01SystemParam;

import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    int clickCount = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView titleCell = (TextView) findViewById(R.id.titleCell);
        titleCell.setOnClickListener(MainActivity.this);

        TextView testCell = (TextView) findViewById(R.id.testCell);
        testCell.setOnClickListener(MainActivity.this);

        TextView wordOriginCell = (TextView) findViewById(R.id.wordOriginCell);
        wordOriginCell.setOnClickListener(MainActivity.this);

        TextView wordSearchCell = (TextView) findViewById(R.id.wordSearchCell);
        wordSearchCell.setOnClickListener(MainActivity.this);

        TextView settingCell = (TextView) findViewById(R.id.settingCell);
        settingCell.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleCell:
                clickCount = clickCount +1;

                if(clickCount == 10){
                    try {
                        clickCount = 0;

                        SystemParamDao systemParamDao = getGlobals().getSystemParamDao();
                        String nowSystemMode = systemParamDao.getParamValue(WordOriginConst.SYSTEM_MODE);
                        String paramValue = null;
                        String toastMessage= null;

                        if(StringUtils.equals(nowSystemMode,WordOriginConst.SYSTEM_MODE_MANAGER)){
                            paramValue = WordOriginConst.SYSTEM_MODE_NORMAL;
                            toastMessage = "通常ユーザモードに変更します";
                        }else if(StringUtils.equals(nowSystemMode,WordOriginConst.SYSTEM_MODE_NORMAL)){
                            paramValue = WordOriginConst.SYSTEM_MODE_MANAGER;
                            toastMessage = "管理者モードに変更します";
                        }

                        M01SystemParam systemParam = new M01SystemParam();
                        systemParam.setParamKey(WordOriginConst.SYSTEM_MODE);
                        systemParam.setParamValue(paramValue);
                        systemParamDao.insertOrUpdate(systemParam);
                        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();

                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }

                break;

            case R.id.testCell:
                Intent testTopIntent = new Intent(getApplicationContext(), TestTopActivity.class);
                startActivity(testTopIntent);
                break;

            case R.id.wordOriginCell:
                Intent wordOriginListIntent = new Intent(getApplicationContext(), WordOriginListActivity.class);
                startActivity(wordOriginListIntent);
                break;

            case R.id.wordSearchCell:
                Intent wordSearchIntent = new Intent(getApplicationContext(), WordSearchActivity.class);
                startActivity(wordSearchIntent);
                break;

            case R.id.settingCell:
                Intent settingIntent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(settingIntent);
                break;
        }
    }
}

