package com.wordOrigin.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wordOrigin.R;
import com.wordOrigin.common.WordOriginCommonService;
import com.wordOrigin.constant.WordOriginConst;
import com.wordOrigin.dao.WordDao;
import com.wordOrigin.dao.WordOriginDao;
import com.wordOrigin.entity.F01Word;
import com.wordOrigin.entity.F01WordOrigin;
import com.wordOrigin.util.StringConvertUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import jp.adlantis.android.AdlantisView;


public class WordListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private WordOriginCommonService commonService;

    List<F01Word> wordList;
    private int displayWordOriginId;
    Resources resource;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        headerButtonSet(R.string.titleWordOriginDetail);
        resource = getResources();

        try {
            AdlantisView adView = (AdlantisView) findViewById(R.id.adView);
            adView.setPublisherID(resource.getString(R.string.publisherID));

            Intent intent = getIntent();
            Integer intentWordOriginId = intent.getIntExtra(WordOriginConst.INTENT_KEY_WORD_ORIGIN_ID, 0);
            commonService = new WordOriginCommonService();

            displayWordOriginId = intentWordOriginId;

            setWordOriginData(displayWordOriginId);

            //次へボタンの制御
            ImageView nextData = (ImageView) findViewById(R.id.nextData);
            nextData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayWordOriginId = displayWordOriginId + 1;
                    setWordOriginData(displayWordOriginId);
                }
            });


            //前へボタンの制御
            ImageView beforeData = (ImageView) findViewById(R.id.beforeData);
            beforeData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayWordOriginId = displayWordOriginId - 1;
                    setWordOriginData(displayWordOriginId);
                }
            });

            //ボタン制御
            if(getGlobals().getWordOriginListAll().size() == intentWordOriginId) {
                //最後の問題
                nextData.setVisibility(View.GONE);
            }
            if(intentWordOriginId == 1) {
                //最初の問題
                beforeData.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setWordOriginData(int intentWordOriginId) {
        ImageView beforeData = (ImageView) findViewById(R.id.beforeData);
        ImageView nextData = (ImageView) findViewById(R.id.nextData);
        //ボタン制御
        if(getGlobals().getWordOriginListAll().size() == intentWordOriginId) {
            //最後の問題
            nextData.setVisibility(View.GONE);
        }else{
            nextData.setVisibility(View.VISIBLE);
        }
        if(intentWordOriginId == 1) {
            //最初の問題
            beforeData.setVisibility(View.GONE);
        }else{
            beforeData.setVisibility(View.VISIBLE);
        }


        try {
            Resources res = getResources();

            WordOriginDao wordOriginDao = getGlobals().getWordOriginDao();
            F01WordOrigin f01WordOrigin = wordOriginDao.getWordOriginForWordOriginId(intentWordOriginId);

            if (f01WordOrigin == null) {
                return;
            }

            //グループ
            CharSequence wordOriginTitle = commonService.getWordOriginTitle(f01WordOrigin, res);
            TextView targetWordOrigin = (TextView) findViewById(R.id.targetWordOrigin);
            targetWordOrigin.setText(wordOriginTitle);

            String[] wordOriginWordArr = StringUtils.split(f01WordOrigin.getWordOriginWord(), "、");

            //■ここから一覧
            //StringBuilderに単語、語源、訳などを突っ込んでいって、最後にHTML化してセット
            //対象とした語源の単語一覧
            WordDao wordDao = getGlobals().getWordDao();
            wordList = wordDao.getWordList(intentWordOriginId);

            ArrayList<CharSequence> dataList = new ArrayList<CharSequence>();
            for (F01Word entity : wordList) {
                //データ定義
                String word = entity.getWord();
                String pronunciation = commonService.getPronunciationHtml(entity.getPronunciation());
                String wordOriginE = entity.getWordOrigin();
                String association = entity.getAssociation();
                String translation = entity.getTranslation();
                String wordColor = commonService.getWordColor(f01WordOrigin.getWordOriginWord(), word);

                StringBuilder listItem = new StringBuilder();
                listItem.append(wordColor);
                //発音
                listItem.append("[");
                listItem.append(StringConvertUtil.convertBreak(pronunciation));
                listItem.append("]");
                listItem.append("<BR>");

                if (StringUtils.isNotBlank(wordOriginE)) {
                    listItem.append(res.getString(R.string.wordOriginForList) + wordOriginE + "<BR>");
                }
                if (StringUtils.isNotBlank(association)) {
                    listItem.append(res.getString(R.string.associationForList) + association + "<BR>");
                }
                //listItem.append(res.getString(R.string.translationForList) + StringConvertUtil.changeStyleSurroundWord(translation));
                listItem.append(res.getString(R.string.translationForList) + translation);

                dataList.add(Html.fromHtml(listItem.toString()));
            }

            //アダプタ定義
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.word_list_item, dataList);

            //リスト設定
            ListView varListView = (ListView) findViewById(R.id.wordList);
            varListView.setAdapter(adapter);
            varListView.setOnItemClickListener(WordListActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onItemClick(
            AdapterView<?> parent,
            View view, int position, long id) {

        int targetWordId = wordList.get(position).getWordId();

        Intent newIntent = new Intent(getApplicationContext(), WordDetailActivity.class);
        newIntent.putExtra(WordOriginConst.INTENT_KEY_WORD_ID, targetWordId);
        startActivity(newIntent);

    }

}

