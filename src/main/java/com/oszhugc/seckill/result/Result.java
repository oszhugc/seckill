package com.oszhugc.seckill.result;

import lombok.Data;

/**
 * @author oszhugc
 * @Date 2019\6\3 0003 22:40
 **/
@Data
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    private Result(T data) {
        this.data = data;
    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(CodeMsg codeMsg) {
        if (codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }

    /**
     * 成功时调用
     *
     */
    public static <T> Result<T> success(T data){
        return new Result(data);
    }

    public static <T> Result<T> error(CodeMsg codeMsg){
        return new Result<T>(codeMsg);
    }



}
