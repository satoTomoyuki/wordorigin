package com.wordOrigin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wordOrigin.R;


public class TemporaryStopActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temporary_stop);

        try {
            //スタートボタン
            ImageView startButton = (ImageView) findViewById(R.id.startButton);
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TemporaryStopActivity.this.finish();
                    //Intent intent = new Intent(getApplicationContext(), TestMainActivity.class);
                    //startActivity(intent);
                }
            });


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

