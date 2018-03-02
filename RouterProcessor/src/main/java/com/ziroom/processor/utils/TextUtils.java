package com.ziroom.processor.utils;

/**
 * Created by lvhl on 2017/1/11.
 */

public class TextUtils {

    public static boolean isEmpty(final CharSequence cs){
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(final CharSequence cs){
        return !isEmpty(cs);
    }

}
