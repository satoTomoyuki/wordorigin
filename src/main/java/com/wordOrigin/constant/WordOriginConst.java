package com.wordOrigin.constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 智之 on 2015/01/27.
 */
public class WordOriginConst {

    // privateコンストラクタでインスタンス生成を抑止
    private WordOriginConst() {
    }

    // 定数
    public static final String INTENT_KEY_WORD_ORIGIN_ID = "WORD_ORIGIN_ID";
    public static final String INTENT_KEY_WORD_ID = "WORD_ID";
    public static final String INTENT_KEY_TEST_COMMON_DTO = "TEST_COMMON_DTO";

    public static final List<String> COLOR_LIST;

    static {
        List<String> list = new ArrayList<String>();
        list.add("red");
        list.add("blue");
        list.add("#228b22");  //forestgreen
        list.add("#ff8c00");  //	darkorange
        list.add("#FF00FF");  //Magenta
        list.add("#8a2be2");  //blueviolet
        COLOR_LIST = Collections.unmodifiableList(list);
    }

    public static final Map<Integer, String> SVL_MAP;

    static {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "初級（レベル1）");
        map.put(2, "初級（レベル2）");
        map.put(3, "初級（レベル3）");
        map.put(4, "中級（レベル4）");
        map.put(5, "中級（レベル5）");
        map.put(6, "中級（レベル6）");
        map.put(7, "上級（レベル7）");
        map.put(8, "上級（レベル8）");
        map.put(9, "上級（レベル9）");
        SVL_MAP = Collections.unmodifiableMap(map);
    }

    //
    public static final String COMPLETE_DISPLAY_KEY = "COMPLETE_DISPLAY_KEY";
    public static final String COMPLETE_DISPLAY_TRUE = "TRUE";
    public static final String COMPLETE_DISPLAY_FALSE = "FALSE";

    public static final String SYSTEM_MODE = "SYSTEM_MODE";
    public static final String SYSTEM_MODE_MANAGER = "MANAGER";
    public static final String SYSTEM_MODE_NORMAL = "NORMAL";
}
