package com.wordOrigin.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.wordOrigin.R;
import com.wordOrigin.common.WordOriginCommonService;
import com.wordOrigin.constant.WordOriginConst;
import com.wordOrigin.dao.WordDao;
import com.wordOrigin.dao.WordOriginDao;
import com.wordOrigin.entity.F01Word;
import com.wordOrigin.entity.F01WordOrigin;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

import jp.adlantis.android.AdlantisView;


public class WordDetailActivity extends BaseActivity implements TextToSpeech.OnInitListener {
    private WordOriginCommonService commonService;
    private int displayWordId;
    private TextToSpeech tts;
    String word;
    Resources resource;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_detail);
        headerButtonSet(R.string.titleWordDetail);
        resource = getResources();
        tts = new TextToSpeech(this, this);

        //画面もJavaだけで作る
        try {
            AdlantisView adView = (AdlantisView) findViewById(R.id.adView);
            adView.setPublisherID(resource.getString(R.string.publisherID));

            TableLayout linearLayout = (TableLayout) findViewById(R.id.wordDetail);
            commonService = new WordOriginCommonService();

            Intent intent = getIntent();
            Integer intentWordId = intent.getIntExtra(WordOriginConst.INTENT_KEY_WORD_ID, 0);

            setWordDetailData(intentWordId);

            displayWordId = intentWordId;

            //次へボタンの制御
            ImageView nextData = (ImageView) findViewById(R.id.nextData);
            if (getGlobals().getWordListAll().size() == displayWordId) {
                //最後の問題
                nextData.setVisibility(View.GONE);
            }
            nextData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayWordId = displayWordId + 1;
                    setWordDetailData(displayWordId);
                }
            });


            //前へボタンの制御
            ImageView beforeData = (ImageView) findViewById(R.id.beforeData);
            if (displayWordId == 1) {
                //最初の問題
                beforeData.setVisibility(View.GONE);
            }

            //音声ボタン
            TextView speak = (TextView) findViewById(R.id.pronunciation);

            speak.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (0 < word.length()) {
                        if (tts.isSpeaking()) {
                            // 読み上げ中なら止める
                            tts.stop();
                        }
                        // 読み上げ開始
                        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            });


            beforeData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayWordId = displayWordId - 1;
                    setWordDetailData(displayWordId);
                }
            });


            View view = findViewById(R.id.wordDetail);
            view.setOnTouchListener(new FlickTouchListener());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setWordDetailData(int intentWordId) {
        ImageView beforeData = (ImageView) findViewById(R.id.beforeData);
        ImageView nextData = (ImageView) findViewById(R.id.nextData);
        //ボタン制御
        if (getGlobals().getWordListAll().size() == intentWordId) {
            //最後の問題
            nextData.setVisibility(View.GONE);
        } else {
            nextData.setVisibility(View.VISIBLE);
        }
        if (intentWordId == 1) {
            //最初の問題
            beforeData.setVisibility(View.GONE);
        } else {
            beforeData.setVisibility(View.VISIBLE);
        }


        try {
            Resources res = getResources();

            //単語情報の取得
            WordDao wordDao = getGlobals().getWordDao();
            F01Word f01Word = wordDao.getWordForWordId(intentWordId);

            if (f01Word == null) {
                return;
            }

            Integer wordId = f01Word.getWordId();
            Integer wordOriginId = f01Word.getWordOriginId();
            word = f01Word.getWord();
            Integer svl = f01Word.getSvl();
            String pronunciation = f01Word.getPronunciation();
            String wordOriginDetail = f01Word.getWordOrigin();
            String association = f01Word.getAssociation();
            String translation = f01Word.getTranslation();
            String remark = f01Word.getRemark();

            //語源情報の取得
            WordOriginDao wordOriginDao = getGlobals().getWordOriginDao();
            F01WordOrigin f01WordOrigin = wordOriginDao.getWordOriginForWordOriginId(wordOriginId);

            String wordOriginWord = f01WordOrigin.getWordOriginWord();

            //グループ
            TextView wordOriginCell = (TextView) findViewById(R.id.wordOriginTitle);
            CharSequence wordOriginTitle = commonService.getWordOriginTitle(f01WordOrigin, res);
            wordOriginCell.setText(wordOriginTitle);
            wordOriginCell.setOnClickListener(new View.OnClickListener() {
                private Integer targetWordOriginId = null;

                //無名クラスが外側から変数を受け取るためのsetter
                public View.OnClickListener setTargetWordOriginId(Integer targetWordOriginId) {
                    this.targetWordOriginId = targetWordOriginId;
                    return this;
                }

                @Override
                public void onClick(View v) {
                    Intent newIntent = new Intent(getApplicationContext(), WordListActivity.class);
                    newIntent.putExtra(WordOriginConst.INTENT_KEY_WORD_ORIGIN_ID, targetWordOriginId);
                    startActivity(newIntent);
                }
            }.setTargetWordOriginId(wordOriginId));

            //no
            TextView noCell = (TextView) findViewById(R.id.no);
            noCell.setText(Integer.toString(wordId));

            //svl
            TextView svlCell = (TextView) findViewById(R.id.svl);
            String svlName = WordOriginConst.SVL_MAP.get(svl);
            if (StringUtils.isBlank(svlName)) {
                svlName = "その他";
            }
            svlCell.setText(svlName);

            //単語
            CharSequence wordColor = commonService.getWordColor(wordOriginWord, word);
            TextView wordColorCell = (TextView) findViewById(R.id.word);
            wordColorCell.setText(Html.fromHtml(wordColor.toString()));

            //カタカナ
            CharSequence pronunciationHtml = commonService.getPronunciationHtml(pronunciation);
            TextView pronunciationCell = (TextView) findViewById(R.id.pronunciation);
            pronunciationCell.setText(Html.fromHtml(pronunciationHtml.toString()));

            //語源
            TableRow wordOriginDetailTr = (TableRow) findViewById(R.id.wordOriginDetailTr);
            if (StringUtils.isNotBlank(wordOriginDetail)) {
                TextView wordOriginDetailCell = (TextView) findViewById(R.id.wordOriginDetail);
                wordOriginDetailCell.setText(wordOriginDetail);
                wordOriginDetailTr.setVisibility(View.VISIBLE);
            } else {
                wordOriginDetailTr.setVisibility(View.GONE);
            }

            //連想
            TableRow associationTr = (TableRow) findViewById(R.id.associationTr);
            if (StringUtils.isNotBlank(association)) {
                TextView associationCell = (TextView) findViewById(R.id.association);
                associationCell.setText(association);
                associationTr.setVisibility(View.VISIBLE);
            } else {
                associationTr.setVisibility(View.GONE);
            }

            //意味
            TableRow translationTr = (TableRow) findViewById(R.id.translationTr);
            if (StringUtils.isNotBlank(translation)) {
                TextView translationCell = (TextView) findViewById(R.id.translation);
                translationCell.setText(translation);
                translationTr.setVisibility(View.VISIBLE);
            } else {
                translationTr.setVisibility(View.GONE);
            }

            //その他
            TableRow remarkTr = (TableRow) findViewById(R.id.remarkTr);
            if (StringUtils.isNotBlank(remark)) {
                TextView remarkCell = (TextView) findViewById(R.id.remark);
                remarkCell.setText(remark);
                remarkTr.setVisibility(View.VISIBLE);
            } else {
                remarkTr.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // 言語をUSに設定
            Locale locale = Locale.US;
            if (tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                tts.setLanguage(locale);
            } else {
                Log.e("TTS", "Not support locale.");
            }
        } else {
            Log.e("TTS", "Init error.");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(tts != null) {
            tts.shutdown();
        }
    }



    private float lastTouchX;
    private float currentX;
    private class FlickTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    lastTouchX = event.getX();
                    break;

                case MotionEvent.ACTION_UP:
                    currentX = event.getX();
                    if (lastTouchX < currentX) {
                        //前に戻る動作
                        displayWordId = displayWordId - 1;
                        setWordDetailData(displayWordId);
                    }
                    if (lastTouchX > currentX) {
                        //次に移動する動作
                        displayWordId = displayWordId + 1;
                        setWordDetailData(displayWordId);
                    }
                    break;

                case MotionEvent.ACTION_CANCEL:
                    currentX = event.getX();
                    if (lastTouchX < currentX) {
                        //前に戻る動作
                        //displayWordId = displayWordId - 1;
                        //setWordDetailData(displayWordId);
                    }
                    if (lastTouchX > currentX) {
                        //次に移動する動作
                        //displayWordId = displayWordId + 1;
                        //setWordDetailData(displayWordId);
                    }
                    break;
            }
            return true;
        }
    }

}
