package com.oszhugc.seckill.access;

import com.alibaba.fastjson.JSON;
import com.oszhugc.seckill.domain.SeckillUser;
import com.oszhugc.seckill.redis.AccessKey;
import com.oszhugc.seckill.redis.RedisService;
import com.oszhugc.seckill.result.CodeMsg;
import com.oszhugc.seckill.result.Result;
import com.oszhugc.seckill.service.SeckillUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author oszhugc
 * @Date 2019\6\7 0007 17:31
 **/
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    SeckillUserService seckillUserService;
    @Autowired
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            SeckillUser user = getUser(request,response);
            UserContext.setUser(user);
            HandlerMethod hm = (HandlerMethod)handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null){
                return true;
            }

            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if (needLogin){
                if (user == null){
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
                key += "_"+user.getId();
            }else {
                //do nothing
            }
            AccessKey ak = AccessKey.withExpire(seconds);
            Integer count = redisService.get(ak, key, Integer.class);
            if (count == null){
                redisService.set(ak,key,1);
            }else if (count < maxCount){
                redisService.incr(ak,key);
            }else {
                render(response,CodeMsg.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg sessionError)throws Exception {
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(sessionError));
        out.write(str.getBytes("utf-8"));
        out.flush();
        out.close();
    }

    private SeckillUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(SeckillUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request,SeckillUserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return seckillUserService.getByToken(response,token);

    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0){
            return null;
        }
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
