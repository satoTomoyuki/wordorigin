package com.wordOrigin.util;


import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StringConvertUtil {

    public static String convertBreak(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return StringUtils.replace(str, "\n", "。");
    }

    public static String getNowTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String strDate = sdf.format(cal.getTime());
        return strDate;
    }


    //TODO
    //Andoriodでは、CSSがきかない？
    //名や動を囲みたいが、なかなか難しい
    //<b style="background-color:#eee; border:#999 solid 1px; font-weight:normal; padding:1px;">動</b>
    //置換文字を元データから変えたほうが良さそう
    public static String changeStyleSurroundWord(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }

        str = StringUtils.replace(str, "[", "<b style='background-color:red; border:red solid 1px; font-weight:normal; padding:1px;'>");
        str = StringUtils.replace(str, "]", "</b>");
        return str;
    }


}
