package com.oszhugc.seckill.controller;

import com.oszhugc.seckill.redis.RedisService;
import com.oszhugc.seckill.result.Result;
import com.oszhugc.seckill.service.SeckillService;
import com.oszhugc.seckill.service.SeckillUserService;
import com.oszhugc.seckill.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author oszhugc
 * @Date 2019\6\8 0008 17:59
 **/
@Slf4j
@RequestMapping("/login")
public class LoginController {

    @Autowired
    SeckillUserService seckillUserService;
    @Autowired
    RedisService redisService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
        log.info(loginVo.toString());
        //登录
        String token = seckillUserService.login(response, loginVo);
        return Result.success(token);
    }
}
