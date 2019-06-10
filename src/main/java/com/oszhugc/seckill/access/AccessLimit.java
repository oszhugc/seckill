package com.oszhugc.seckill.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author oszhugc
 * @Date 2019\6\7 0007 17:29
 * @Version 2.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {

    int seconds();
    int maxCount();
    boolean needLogin() default true;
}
