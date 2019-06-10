package com.oszhugc.seckill.redis;

/**
 * @author oszhugc
 * @Date 2019\6\7 0007 17:58
 **/
public class AccessKey extends BasePrefix {

    private AccessKey(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }

    public static AccessKey withExpire(int expireSeconds){
        return new AccessKey(expireSeconds,"access");
    }
}
