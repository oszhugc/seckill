package com.oszhugc.seckill.access;

import com.oszhugc.seckill.domain.SeckillUser;

/**
 * @author oszhugc
 * @Date 2019\6\7 0007 17:27
 **/
public class UserContext {

    private static ThreadLocal<SeckillUser> userHolder = new ThreadLocal<SeckillUser>();

    public static void setUser(SeckillUser seckillUser){
        userHolder.set(seckillUser);
    }

    public static SeckillUser getUser(){
        return userHolder.get();
    }

}
