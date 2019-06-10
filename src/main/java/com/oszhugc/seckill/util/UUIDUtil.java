package com.oszhugc.seckill.util;

import java.util.UUID;

/**
 * @author oszhugc
 * @Date 2019\6\8 0008 11:39
 **/
public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
