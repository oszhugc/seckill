package com.oszhugc.seckill.controller;

import com.oszhugc.seckill.domain.SeckillUser;
import com.oszhugc.seckill.redis.RedisService;
import com.oszhugc.seckill.result.Result;
import com.oszhugc.seckill.service.SeckillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author oszhugc
 * @Date 2019\6\8 0008 19:13
 **/
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    SeckillUserService seckillUserService;
    @Autowired
    RedisService redisService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<SeckillUser> info(Model model, SeckillUser user ){
        return Result.success(user);
    }
}
