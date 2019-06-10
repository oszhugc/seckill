package com.oszhugc.seckill.redis;

/**
 * @author oszhugc
 * @Date 2019\6\7 0007 17:59
 **/
public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;

    private String prefix;

    public BasePrefix(String prefix){//0代表永不过期
        this(0,prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public int expireSeconds(){
        return expireSeconds;
    }

    public String getPrefix(){
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }

}
