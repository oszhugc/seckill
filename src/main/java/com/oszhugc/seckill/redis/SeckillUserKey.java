package com.oszhugc.seckill.redis;

/**
 * @author oszhugc
 * @Date 2019\6\7 0007 18:11
 **/
public class SeckillUserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 3600*24*2;

    private SeckillUserKey(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }

    public static SeckillUserKey token = new SeckillUserKey(TOKEN_EXPIRE,"tk");

    public static SeckillUserKey getById = new SeckillUserKey(0,"id");
}
