package com.wordOrigin.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wordOrigin.R;
import com.wordOrigin.constant.WordOriginConst;
import com.wordOrigin.dao.TestHistoryDao;
import com.wordOrigin.dao.WordDao;
import com.wordOrigin.dto.TestCommonDto;
import com.wordOrigin.entity.F01TestHistory;
import com.wordOrigin.entity.F01Word;
import com.wordOrigin.util.StringConvertUtil;

import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;


public class TestMainActivity extends BaseActivity {
    private Resources res;
    private TestCommonDto testCommonDto;

    /**
     * スレッドUI操作用ハンドラ
     */
    private Handler mHandler = new Handler();
    /**
     * テキストオブジェクト
     */
    private Runnable imageHide;
    Integer intentWordOriginId;
    ProgressBar progressBar;
    CountDownTimer timer;
    long nowTimer;
    int correctButtonId;
    TextView correctButton;
    int stopTime;

    public void onCreate(Bundle savedInstanceState) {
        stopTime = 0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_main);
        headerButtonSet(R.string.titleWordDetail);

        res = getResources();
        WordDao wordDao = getGlobals().getWordDao();

        //丸、バツ画像の非表示
        ((ImageView) findViewById(R.id.testCorrect)).setVisibility(View.GONE);
        ((ImageView) findViewById(R.id.testMistake)).setVisibility(View.GONE);
        ((ImageView) findViewById(R.id.test_bang_on)).setVisibility(View.GONE);

        //ボタンサイズがかわってしまうので。。。
        ((Button) findViewById(R.id.answer1)).setBackgroundColor(res.getColor(R.color.defaultButtonColor));
        ((Button) findViewById(R.id.answer2)).setBackgroundColor(res.getColor(R.color.defaultButtonColor));
        ((Button) findViewById(R.id.answer3)).setBackgroundColor(res.getColor(R.color.defaultButtonColor));
        ((Button) findViewById(R.id.answer4)).setBackgroundColor(res.getColor(R.color.defaultButtonColor));
        ((Button) findViewById(R.id.answerUnclear)).setBackgroundColor(res.getColor(R.color.defaultButtonColor));

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        // 水平プログレスバー
        progressBar.setMax(100);
        timer = new CountDownTimer(10000, 100) {
            // 指定した間隔で処理を実行
            public void onTick(long amount) {
                // プログレスバーの状態を更新

                if(stopTime != 0){
                    amount = amount - (stopTime * 100);
                }

                nowTimer = (10000 - amount) / 100;
                progressBar.setProgress((int) nowTimer);
            }

            // タイマー終了時に呼ばれる
            public void onFinish() {
                progressBar.setProgress(0);
                checkSelect(R.id.answerUnclear);
            }
        };
        timer.start();

        //ストップボタン
        ImageView stopButton = (ImageView) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTime = progressBar.getProgress();
                timer.cancel();
                Intent intent = new Intent(getApplicationContext(), TemporaryStopActivity.class);
                startActivity(intent);
            }
        });

        //画面もJavaだけで作る
        try {
            Intent intent = getIntent();

            //引数の受取
            intentWordOriginId = intent.getIntExtra(WordOriginConst.INTENT_KEY_WORD_ORIGIN_ID, 0);

            //最終問題
            testCommonDto = new TestCommonDto();
            testCommonDto.setWordList(wordDao.getWordList(intentWordOriginId));

            F01Word targetData = testCommonDto.getWordList().get(testCommonDto.getTestIndex());

            StringBuilder nowNowSb = new StringBuilder();
            nowNowSb.append(testCommonDto.getTestIndex() + 1);
            nowNowSb.append("/");
            nowNowSb.append(testCommonDto.getWordList().size());
            TextView nowNo = (TextView) findViewById(R.id.nowNo);
            nowNo.setText(nowNowSb.toString());

            //問題のセット
            TextView question = (TextView) findViewById(R.id.question);
            question.setText(targetData.getTranslation());

            //回答のセット
            createAnswerButton(targetData.getWord());

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onClick(View view) {
        //super.onClick(view);
        //スリープ
        checkSelect(view.getId());
    }


    private void createAnswerButton(String answer) {
        //1～4までのランダム値を取得
        Random r = new Random();
        correctButtonId = r.nextInt(4) + 1;

        //初期値セット
        Button answer1 = (Button) findViewById(R.id.answer1);
        answer1.setText(getRandomWord());
        answer1.setOnClickListener(this);

        Button answer2 = (Button) findViewById(R.id.answer2);
        answer2.setText(getRandomWord());
        answer2.setOnClickListener(this);

        Button answer3 = (Button) findViewById(R.id.answer3);
        answer3.setText(getRandomWord());
        answer3.setOnClickListener(this);

        Button answer4 = (Button) findViewById(R.id.answer4);
        answer4.setText(getRandomWord());
        answer4.setOnClickListener(this);

        Button answerUnclear = (Button) findViewById(R.id.answerUnclear);
        answerUnclear.setOnClickListener(this);

        //回答を設定
        switch (correctButtonId) {
            case 1:
                answer1.setText(answer);
                break;
            case 2:
                answer2.setText(answer);
                break;
            case 3:
                answer3.setText(answer);
                break;
            case 4:
                answer4.setText(answer);
                break;
        }
    }

    private int getCorrectButton(int correctButtonId) {
        int ret = 0;
        switch (correctButtonId) {
            case 1:
                ret = R.id.answer1;
                break;
            case 2:
                ret = R.id.answer2;
                break;
            case 3:
                ret = R.id.answer3;
                break;
            case 4:
                ret = R.id.answer4;
                break;
        }
        return ret;
    }


    private String getRandomWord() {
        List<F01Word> wordListAll = getGlobals().getWordListAll();

        Random r = new Random();
        int randomNo = r.nextInt(wordListAll.size());
        String ret = wordListAll.get(randomNo).getWord();

        return ret;
    }


    private void checkSelect(int buttonId) {
        try {
            //Thread.sleep(1000);

            stopTime = 0;

            timer.cancel();

            Button selectAnswerView = (Button) findViewById(buttonId);
            String selectAnswer = (String) selectAnswerView.getText();

            correctButton = (TextView) findViewById(getCorrectButton(correctButtonId));
            correctButton.setBackgroundColor(res.getColor(R.color.orange));
            correctButton.setAlpha((float) 0.5);

            F01Word word = testCommonDto.getWordList().get(testCommonDto.getTestIndex());

            int faultCount = 0;
            if (StringUtils.equals(selectAnswer, word.getWord())) {
                //正解

                int progress = progressBar.getProgress();
                if(progress < 20){
                    ((ImageView) findViewById(R.id.test_bang_on)).setVisibility(View.VISIBLE);
                }else{
                    ((ImageView) findViewById(R.id.testCorrect)).setVisibility(View.VISIBLE);
                }

            } else {
                //間違い
                faultCount = faultCount + 1;
                ((ImageView) findViewById(R.id.testMistake)).setVisibility(View.VISIBLE);
            }

            F01TestHistory f01TestHistory = new F01TestHistory();
            f01TestHistory.setFaultCount(faultCount);
            f01TestHistory.setLastTestTime(StringConvertUtil.getNowTime());
            f01TestHistory.setWordId(word.getWordId());

            //テスト履歴の登録
            TestHistoryDao testHistoryDao = getGlobals().getTestHistoryDao();
            testHistoryDao.insertOrUpdate(f01TestHistory);

            imageHide = new Runnable() {
                public void run() {

                    progressBar.setProgress(0);
                    timer.start();

                    testCommonDto.setTestIndex(testCommonDto.getTestIndex() + 1);

                    try {
                        if (testCommonDto.getTestIndex() == testCommonDto.getWordList().size()) {
                            //終了
                            Intent newIntent = new Intent(getApplicationContext(), TestEndActivity.class);
                            newIntent.putExtra(WordOriginConst.INTENT_KEY_WORD_ORIGIN_ID, intentWordOriginId);
                            startActivity(newIntent);

                        } else {
                            F01Word targetData = testCommonDto.getWordList().get(testCommonDto.getTestIndex());

                            StringBuilder nowNowSb = new StringBuilder();
                            nowNowSb.append(testCommonDto.getTestIndex() + 1);
                            nowNowSb.append("/");
                            nowNowSb.append(testCommonDto.getWordList().size());
                            TextView nowNo = (TextView) findViewById(R.id.nowNo);
                            nowNo.setText(nowNowSb.toString());

                            //問題のセット
                            TextView question = (TextView) findViewById(R.id.question);
                            question.setText(targetData.getTranslation());

                            //回答のセット
                            createAnswerButton(targetData.getWord());

                            ((ImageView) findViewById(R.id.testCorrect)).setVisibility(View.GONE);
                            ((ImageView) findViewById(R.id.testMistake)).setVisibility(View.GONE);
                            ((ImageView) findViewById(R.id.test_bang_on)).setVisibility(View.GONE);

                            //元に戻す
                            correctButton.setBackgroundColor(res.getColor(R.color.defaultButtonColor));
                            correctButton.setAlpha(1);

                            mHandler.removeCallbacks(imageHide);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
            mHandler.postDelayed(imageHide, 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        timer.start();
        super.onResume();
    }

    @Override
    public void onPause() {
        stopTime = progressBar.getProgress();
        timer.cancel();
        super.onPause();
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

}

