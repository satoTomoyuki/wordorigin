package com.wordOrigin.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wordOrigin.R;
import com.wordOrigin.dao.TestHistoryDao;

import java.sql.SQLException;

import jp.adlantis.android.AdlantisView;


public class SettingActivity extends BaseActivity implements View.OnClickListener {
    Resources resource;

    public void onCreate(Bundle savedInstanceState) {
        resource = getResources();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        AdlantisView adView = (AdlantisView) findViewById(R.id.adView);
        adView.setPublisherID(resource.getString(R.string.publisherID));

        //ヘッダータイトルの設定
        headerButtonSet(R.string.titleSetting);

        //ボタンにイベントを追加
        TextView testHistoryDelete = (TextView) findViewById(R.id.testHistoryDelete);
        testHistoryDelete.setOnClickListener(this);

        //ボタンにイベントを追加
        TextView version = (TextView) findViewById(R.id.version);
        version.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.testHistoryDelete) {
            createDeleteDialog();
        } else if (view.getId() == R.id.version) {
            createVersionDialog();
        }

    }


    private void createDeleteDialog() {
        try {
            // 試験履歴
            AlertDialog.Builder deleteHistoryDlg = new AlertDialog.Builder(this);
            deleteHistoryDlg.setTitle("確認");
            StringBuilder sb = new StringBuilder();
            sb.append("試験実施履歴をクリアします。宜しいですか？");

            deleteHistoryDlg.setMessage(sb.toString());
            deleteHistoryDlg.setPositiveButton(
                    "実行",
                    new DialogInterface.OnClickListener() {
                        //クリック時の処理
                        public void onClick(DialogInterface dialog, int which) {
                            //データ削除
                            try {
                                TestHistoryDao testHistoryDao = getGlobals().getTestHistoryDao();
                                testHistoryDao.deleteAll();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            deleteHistoryDlg.setNegativeButton(
                    "中止",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancel ボタンクリック処理
                        }
                    });

            // 表示
            deleteHistoryDlg.create().show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createVersionDialog() {
        //バージョン表示
        AlertDialog.Builder version = new AlertDialog.Builder(this);
        //ダイアログタイトルをセット
        version.setTitle("バージョン");
        //ダイアログメッセージをセット
        version.setMessage("バージョン 1.0.0");
        //ダイアログ表示
        version.show();

    }
}




