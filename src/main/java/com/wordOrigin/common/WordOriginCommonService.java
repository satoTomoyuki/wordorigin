package com.wordOrigin.common;


import android.content.res.Resources;
import android.text.Html;

import com.wordOrigin.R;
import com.wordOrigin.constant.WordOriginConst;
import com.wordOrigin.entity.F01WordOrigin;

import org.apache.commons.lang3.StringUtils;

public class WordOriginCommonService {

    public CharSequence getWordOriginTitle(F01WordOrigin wordOrigin, Resources res) {
        int wordOriginId = wordOrigin.getWordOriginId();
        String wordOriginTranslation = wordOrigin.getWordOriginTranslation();
        String wordOriginWord = wordOrigin.getWordOriginWord();

        //語源タイトルの設定(文字色の部分設定)
        StringBuilder sb = new StringBuilder();
        sb.append(res.getString(R.string.no));
        sb.append(wordOriginId);
        sb.append(" ");
        sb.append(wordOriginTranslation);
        sb.append(" = ");

        String[] wordOriginWordArr = StringUtils.split(wordOriginWord, "、");

        int index = 0;
        for (String wo : wordOriginWordArr) {
            sb.append("<font color='");
            sb.append(WordOriginConst.COLOR_LIST.get(index));
            sb.append("'>");
            sb.append(wo);
            sb.append("</font>");
            index++;

            if (wordOriginWordArr.length != index) {
                sb.append("、");
            }
        }
        return Html.fromHtml(sb.toString());
    }

    public String getWordColor(String wordOriginWord, String word) {
        String wordColor = null;
        String[] wordOriginWordArr = StringUtils.split(wordOriginWord, "、");

        //英単語
        Integer targetIndex = 0;
        boolean hitFlag = false;
        for (String wo : wordOriginWordArr) {
            if (StringUtils.contains(word, wo)) {
                hitFlag = true;
                break;
            }
            targetIndex++;
        }

        if (hitFlag) {
            StringBuilder workSb = new StringBuilder();
            workSb.append("<font color='");
            workSb.append(WordOriginConst.COLOR_LIST.get(targetIndex));
            workSb.append("'>");
            workSb.append(wordOriginWordArr[targetIndex]);
            workSb.append("</font>");

            wordColor = StringUtils.replaceOnce(word, wordOriginWordArr[targetIndex], workSb.toString());
        } else {
            wordColor = word;
        }
        return wordColor;
    }

    public String getPronunciationHtml(String pronunciation) {
        String ret = pronunciation;

        if(StringUtils.startsWith(pronunciation, "★")){
            StringBuilder sb = new StringBuilder();
            sb.append("<b>");
            sb.append(StringUtils.stripStart(pronunciation,  "★"));
            sb.append("</b>");
            ret = sb.toString();
        }
        return ret;
    }

}
