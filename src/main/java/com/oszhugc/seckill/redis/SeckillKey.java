package com.oszhugc.seckill.redis;

/**
 * @author oszhugc
 * @Date 2019\6\7 0007 18:09
 **/
public class SeckillKey extends BasePrefix {

    private SeckillKey(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }

    public static SeckillKey isGoodsOver = new SeckillKey(0,"go");

    public static SeckillKey getSeckillPath = new SeckillKey(60,"mp");

    public static SeckillKey getSeckillVerifyCode = new SeckillKey(300,"vc");
}
