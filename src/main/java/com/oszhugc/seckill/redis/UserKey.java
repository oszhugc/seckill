package com.oszhugc.seckill.redis;

/**
 * @author oszhugc
 * @Date 2019\6\7 0007 18:16
 **/
public class UserKey extends BasePrefix {

    private UserKey(String prefix){
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");

    public static UserKey getByName = new UserKey("name");

}
