package com.wordOrigin.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amoad.AMoAdView;
import com.amoad.AdCallback;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.wordOrigin.MainActivity;
import com.wordOrigin.R;
import com.wordOrigin.common.Globals;
import com.wordOrigin.constant.WordOriginConst;

import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;

import jp.adlantis.android.AdlantisView;


public class BaseActivity extends Activity implements View.OnClickListener {
    //グローバル変数
    private Globals globals;

    public Globals getGlobals(){
        if(globals == null){
            globals = (Globals) this.getApplication();
            globals.GlobalsAllInit();
        }
        return globals;
    }

    public void headerButtonSet(int headerTitle) {
        try {
            Resources resource = getResources();

            TextView headerTitleView = (TextView) findViewById(R.id.headerTitle);
            headerTitleView.setText(resource.getString(headerTitle));

            //各種ボタンにイベントをつける
            ImageView homeButton = (ImageView) findViewById(R.id.homeButton);
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(homeIntent);
                }
            });

            ImageView testButton = (ImageView) findViewById(R.id.testButton);
            testButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent homeIntent = new Intent(getApplicationContext(), TestTopActivity.class);
                    startActivity(homeIntent);
                }
            });

            ImageView wordOriginButton = (ImageView) findViewById(R.id.wordOriginButton);
            wordOriginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent homeIntent = new Intent(getApplicationContext(), WordOriginListActivity.class);
                    startActivity(homeIntent);
                }
            });

            ImageView wordSearchButton = (ImageView) findViewById(R.id.wordSearchButton);
            wordSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent homeIntent = new Intent(getApplicationContext(), WordSearchActivity.class);
                    startActivity(homeIntent);
                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homeButton:
                Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(homeIntent);
                break;

            case R.id.testButton:
                Intent testIntent = new Intent(getApplicationContext(), TestTopActivity.class);
                startActivity(testIntent);
                break;

            case R.id.wordOriginButton:
                Intent wordOriginIntent = new Intent(getApplicationContext(), WordOriginListActivity.class);
                startActivity(wordOriginIntent);
                break;

            case R.id.wordSearchButton:
                Intent wordSearchIntent = new Intent(getApplicationContext(), WordSearchActivity.class);
                startActivity(wordSearchIntent);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {

       super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

