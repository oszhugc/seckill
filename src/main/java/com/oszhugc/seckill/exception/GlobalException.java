package com.oszhugc.seckill.exception;

import com.oszhugc.seckill.result.CodeMsg;

/**
 * 全局异常
 *
 * @author oszhugc
 * @Date 2019\6\3 0003 22:25
 **/

public class GlobalException extends RuntimeException{

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg){
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg(){
        return codeMsg;
    }
}
