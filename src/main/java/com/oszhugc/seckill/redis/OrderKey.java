package com.oszhugc.seckill.redis;

/**
 * @author oszhugc
 * @Date 2019\6\7 0007 18:15
 **/
public class OrderKey extends BasePrefix {

    private OrderKey(String prefix){
        super(prefix);
    }

    public static OrderKey getSeckillOrderByUidGid = new OrderKey("moug");
}
