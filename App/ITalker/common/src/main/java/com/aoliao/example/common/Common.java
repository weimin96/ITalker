package com.aoliao.example.common;

/**
 * Created by 你的奥利奥 on 2017/8/9.
 * 说明：
 */

public class Common {
    /**
     * 不变的参数
     */
    public interface Constance{
        //手机号正则，11位
        String REGEX_MOBILE="[1][3,4,5,7,8][0-9]{9}$";

        String API_URL="http://116.196.93.33:8080/api/";
    }
}
