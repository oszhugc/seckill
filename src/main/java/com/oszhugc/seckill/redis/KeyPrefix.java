package com.oszhugc.seckill.redis;

/**
 * @author oszhugc
 * @Date 2019\6\7 0007 17:58
 **/
public interface KeyPrefix {

    int expireSeconds();

    String getPrefix();
}
