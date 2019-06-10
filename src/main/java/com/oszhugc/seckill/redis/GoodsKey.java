package com.oszhugc.seckill.redis;

/**
 * @author oszhugc
 * @Date 2019\6\7 0007 18:06
 **/
public class GoodsKey extends BasePrefix {

    private GoodsKey(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60,"gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60,"gd");
    public static GoodsKey getSeckillGoodsStock = new GoodsKey(0,"gs");
}
