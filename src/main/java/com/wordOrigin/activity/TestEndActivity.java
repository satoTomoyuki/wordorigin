package com.wordOrigin.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.wordOrigin.R;
import com.wordOrigin.common.WordOriginCommonService;
import com.wordOrigin.constant.WordOriginConst;
import com.wordOrigin.dao.TestHistoryDao;
import com.wordOrigin.dao.WordOriginDao;
import com.wordOrigin.dto.TestResult;
import com.wordOrigin.entity.F01WordOrigin;
import com.wordOrigin.util.WordOriginUtil;

import java.util.List;

import jp.adlantis.android.AdlantisView;


public class TestEndActivity extends BaseActivity {

    private Resources resource;
    private WordOriginCommonService commonService;

    public void onCreate(Bundle savedInstanceState) {
        resource = getResources();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_end);

        //ヘッダの設定
        headerButtonSet(R.string.titleTestResult);

        try {
            AdlantisView adView = (AdlantisView) findViewById(R.id.adView);
            adView.setPublisherID(resource.getString(R.string.publisherID));

            Intent intent = getIntent();
            final int intentWordOriginId = intent.getIntExtra(WordOriginConst.INTENT_KEY_WORD_ORIGIN_ID, 0);
            commonService = new WordOriginCommonService();

            //語源ボタン
            TextView wordOriginLink = (TextView) findViewById(R.id.wordOrigin);
            wordOriginLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newIntent = new Intent(getApplicationContext(), WordListActivity.class);
                    newIntent.putExtra(WordOriginConst.INTENT_KEY_WORD_ORIGIN_ID, intentWordOriginId);
                    startActivity(newIntent);
                }
            });

            TestHistoryDao testHistoryDao = getGlobals().getTestHistoryDao();
            List<TestResult> testList = testHistoryDao.getTestResultList(intentWordOriginId);

            int correctCnt = 0;
            for (TestResult trCorrect : testList) {
                if (trCorrect.isResult()) {
                    correctCnt = correctCnt + 1;
                }
            }

            //語源情報の設定
            WordOriginDao wordOriginDao = getGlobals().getWordOriginDao();
            F01WordOrigin f01WordOrigin = wordOriginDao.getWordOriginForWordOriginId(intentWordOriginId);

            String achievementForView = WordOriginUtil.getAchievementForView(correctCnt, testList.size());
            TextView accuracyRate = (TextView) findViewById(R.id.accuracyRate);
            accuracyRate.setText(Html.fromHtml(achievementForView));

            CharSequence wordOriginTitle = commonService.getWordOriginTitle(f01WordOrigin, resource);
            TextView targetWordOrigin = (TextView) findViewById(R.id.wordOrigin);
            targetWordOrigin.setText(wordOriginTitle);

            //結果一覧
            TableLayout tableLayout = (TableLayout) findViewById(R.id.testResultList);
            Drawable simpleFrame = getResources().getDrawable(R.drawable.table_data);

            //語源の再テスト
            ImageView beforeData = (ImageView) findViewById(R.id.beforeData);
            beforeData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                startTest(intentWordOriginId, true);
                }
            });

            //次の語源のテスト
            ImageView nextData = (ImageView) findViewById(R.id.nextData);

            if(getGlobals().getWordOriginListAll().size() == intentWordOriginId) {
                //最後の問題
                nextData.setVisibility(View.GONE);
            }else{
                nextData.setVisibility(View.VISIBLE);
                nextData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startTest(intentWordOriginId + 1, false);
                    }
                });
            }

            for (TestResult tr : testList) {
                TableRow tableRow = new TableRow(this);
                tableLayout.addView(tableRow);

                TextView no = new TextView(this);
                String noStr = String.format("%1$03d", tr.getWordId());
                no.setText(noStr);
                no.setBackgroundColor(resource.getColor(R.color.white));
                no.setGravity(Gravity.CENTER);
                no.setTextSize(20);
                no.setPadding(10, 5, 5, 5);
                no.setBackground(simpleFrame);

                TextView testResult = new TextView(this);

                StringBuilder resultOut = new StringBuilder();
                resultOut.append("<font color='");
                if (tr.isResult()) {
                    resultOut.append("red'>○");
                } else {
                    resultOut.append("blue'>×");
                }
                resultOut.append("</font>");

                testResult.setText(Html.fromHtml(resultOut.toString()));
                testResult.setBackgroundColor(resource.getColor(R.color.white));
                testResult.setGravity(Gravity.CENTER);
                testResult.setTextSize(20);
                testResult.setPadding(5, 5, 5, 5);
                testResult.setBackground(simpleFrame);

                TextView word = new TextView(this);
                //word.setWidth(500);
                word.setText(tr.getWord());
                word.setBackgroundColor(resource.getColor(R.color.white));
                word.setTextSize(20);
                word.setPadding(20, 5, 5, 5);
                word.setBackground(simpleFrame);

                tableRow.addView(no);
                tableRow.addView(testResult);
                tableRow.addView(word);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent newIntent = new Intent(getApplicationContext(), TestTopActivity.class);
            startActivity(newIntent);
            return true;
        }
        return false;
    }

    public void startTest(int targetWordOrigin, boolean reTest) {
        Resources resource = getResources();

        String wordOriginTranslation = getGlobals().getWordOriginListAll().get(targetWordOrigin-1).getWordOriginTranslation();

        //ダイアログをだして、試験を実施する
        // 確認ダイアログの生成
        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
        alertDlg.setTitle("試験開始 確認");
        StringBuilder sb = new StringBuilder();
        sb.append("「");
        sb.append(resource.getString(R.string.no));
        sb.append(String.valueOf(targetWordOrigin));
        sb.append(" ");
        sb.append(wordOriginTranslation);
        sb.append("」の試験を");

        if(reTest) {
            sb.append("再実施します。");
        }else{
            sb.append("開始します。");
        }
        sb.append("宜しいですか？");

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
                }.setTargetWordOriginId(targetWordOrigin));

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

}

