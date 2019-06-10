package com.oszhugc.seckill.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author oszhugc
 * @Date 2019\6\3 0003 22:26
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CodeMsg {

    private int code;
    private String msg;

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }


    //通用的错误码
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101,"参数校验异常,%s");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102,"请求非法");
    public static CodeMsg ACCESS_LIMIT_REACHED = new CodeMsg(500104,"访问太频繁");
    //登录模块 5002xx
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210,"session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211,"登录密码不能为空");
    public static CodeMsg MOBILE_EMPPTY = new CodeMsg(500212,"手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213,"手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214,"手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215,"密码错误");
    //商品模块 5003xx

    //订单模块
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400,"订单不存在");

    //秒杀模块 5005xx
    public static CodeMsg SECKILL_OVER = new CodeMsg(500500,"商品已经秒杀完毕");
    public static CodeMsg REPEATE_SECKILL = new CodeMsg(500501,"不能重复秒杀");
    public static CodeMsg SEKILL_FAIL = new CodeMsg(500502,"秒杀失败");







}
