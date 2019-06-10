package com.oszhugc.seckill.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author oszhugc
 * @Date 2019\6\3 0003 23:10
 **/
public class ValidatorUtil {

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src){
        if (StringUtils.isEmpty(src)){
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(src);
        return matcher.matches();
    }
}
