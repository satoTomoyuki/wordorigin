package com.wordOrigin.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wordOrigin.R;
import com.wordOrigin.constant.WordOriginConst;
import com.wordOrigin.entity.F01WordOrigin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.adlantis.android.AdlantisView;


public class WordOriginListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    List<F01WordOrigin> dataList;
    Resources resource;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_origin_list);
        headerButtonSet(R.string.titleList);
        resource = getResources();

        try {
            AdlantisView adView = (AdlantisView) findViewById(R.id.adView);
            adView.setPublisherID(resource.getString(R.string.publisherID));

            dataList = getGlobals().getWordOriginListAll();

            ArrayList<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
            for (F01WordOrigin entity : dataList) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("wordOriginTranslation", "No." + String.valueOf(entity.getWordOriginId() + " " + entity.getWordOriginTranslation()));
                map.put("wordOriginWord", entity.getWordOriginWord());
                mapList.add(map);
            }

            SimpleAdapter adapter
                    = new SimpleAdapter(WordOriginListActivity.this, mapList, R.layout.word_origin_list_item,
                    new String[]{"wordOriginTranslation", "wordOriginWord"},
                    new int[]{R.id.wordOriginTranslation, R.id.wordOriginWord}
            );

            ListView varListView = (ListView) findViewById(R.id.wordOriginList);
            varListView.setAdapter(adapter);
            varListView.setOnItemClickListener(WordOriginListActivity.this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onItemClick(
            AdapterView<?> parent,
            View view, int position, long id) {

        //疎結合にするためにも、wordOriginIdだけで動作できるように
        int targetWordOriginId = dataList.get(position).getWordOriginId();

        Intent newIntent = new Intent(getApplicationContext(), WordListActivity.class);
        newIntent.putExtra(WordOriginConst.INTENT_KEY_WORD_ORIGIN_ID, targetWordOriginId);
        startActivity(newIntent);

    }

}

