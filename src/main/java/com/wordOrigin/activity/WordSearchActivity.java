package com.wordOrigin.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wordOrigin.R;
import com.wordOrigin.constant.WordOriginConst;
import com.wordOrigin.dao.WordDao;
import com.wordOrigin.entity.F01Word;

import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.adlantis.android.AdlantisView;


public class WordSearchActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    List<F01Word> dataList;
    Resources resource;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_search);
        headerButtonSet(R.string.titleSearch);
        resource = getResources();

        try {
            AdlantisView adView = (AdlantisView) findViewById(R.id.adView);
            adView.setPublisherID(resource.getString(R.string.publisherID));

            EditText searchWord = (EditText) findViewById(R.id.searchWord);

            TextView searchTitleCell = (TextView) findViewById(R.id.searchTitle);
            searchTitleCell.setVisibility(View.GONE);

            searchWord.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (StringUtils.isNotBlank(s.toString())) {
                        searchWordData(s.toString());
                    } else {
                        ListView varListView = (ListView) findViewById(R.id.searchWordList);
                        varListView.setAdapter(null);
                    }
                }
            });

        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    private void searchWordData(String searchWord) {
        try {
            WordDao wordDao = getGlobals().getWordDao();
            dataList = wordDao.getWordListWhereWord(searchWord);

            TextView searchTitleCell = (TextView) findViewById(R.id.searchTitle);
            if (dataList == null || dataList.size() == 0) {
                searchTitleCell.setVisibility(View.GONE);
            } else {
                searchTitleCell.setVisibility(View.VISIBLE);
            }

            ArrayList<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
            for (F01Word entity : dataList) {
                HashMap<String, String> map = new HashMap<String, String>();

                String noFormat = StringUtils.leftPad(String.valueOf(entity.getWordId()), 4, "0");
                map.put("wordId", "No." + noFormat);

                map.put("word", entity.getWord());
                mapList.add(map);
            }

            SimpleAdapter adapter
                    = new SimpleAdapter(this, mapList, R.layout.word_search_list_item,
                    new String[]{"wordId", "word"},
                    new int[]{R.id.searchListNo, R.id.searchListWord}
            );

            ListView varListView = (ListView) findViewById(R.id.searchWordList);
            varListView.setAdapter(adapter);
            varListView.setOnItemClickListener(this);

        } catch (SQLException e1) {
            throw new RuntimeException(e1);

        }
    }

    public void onItemClick(
            AdapterView<?> parent,
            View view, int position, long id) {

        //疎結合にするためにも、wordOriginIdだけで動作できるように
        int targetWordId = dataList.get(position).getWordId();

        Intent newIntent = new Intent(getApplicationContext(), WordDetailActivity.class);
        newIntent.putExtra(WordOriginConst.INTENT_KEY_WORD_ID, targetWordId);
        startActivity(newIntent);

    }

}

