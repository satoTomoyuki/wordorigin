package com.wordOrigin.util;


public class WordOriginUtil {

    public static String getAchievementForView(int nowCount, int maxCount) {
        StringBuilder sb = new StringBuilder();

        float achievementRate = ((float) nowCount / (float) maxCount) * 100;

        if(nowCount == maxCount){
            sb.append("<b><font color='red'>");
            sb.append((int) achievementRate);
            sb.append("%</font></b>");
        }else{
            sb.append((int) achievementRate);
            sb.append("%");
        }

        sb.append("（");
        sb.append(nowCount);
        sb.append("/");
        sb.append(maxCount);
        sb.append("）");

        return sb.toString();
    }


}
