package com.oszhugc.seckill.controller;

import com.oszhugc.seckill.domain.SeckillUser;
import com.oszhugc.seckill.redis.GoodsKey;
import com.oszhugc.seckill.redis.RedisService;
import com.oszhugc.seckill.result.Result;
import com.oszhugc.seckill.service.GoodService;
import com.oszhugc.seckill.service.SeckillService;
import com.oszhugc.seckill.vo.GoodsDetailVo;
import com.oszhugc.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author oszhugc
 * @Date 2019\6\8 0008 17:34
 **/
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    SeckillService seckillService;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodService goodService;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/to_list",produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, SeckillUser user){
        model.addAttribute("user",user);
        //去缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)){
            return html;
        }

        List<GoodsVo> goodsVoList = goodService.listGoodsVo();
        model.addAttribute("goodsList",goodsVoList);
        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(), request.getLocale(),
                model.asMap(), applicationContext);
        //手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }
        return html;
    }


    @RequestMapping(value = "/to_detail2/{goodsId}",produces = "text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response,
                          Model model, SeckillUser user, @PathVariable("goodsId")long goodsId){
        model.addAttribute("user",user);

        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
        if (StringUtils.isNotEmpty(html)){
            return html;
        }

        //手动渲染
        GoodsVo goods = goodService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);

        long starAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0 ;
        int remainSecondes = 0;
        if (now < starAt){//秒杀还未开始,倒计时
            miaoshaStatus = 0;
            remainSecondes= (int)((starAt - now)/1000);
        }else if (now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSecondes = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSecondes = 0;
        }
        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSecondes);

        SpringWebContext ctx = new SpringWebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",ctx);
        if (StringUtils.isNotEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail,""+goodsId,html);
        }

        return html;

    }


    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request,HttpServletResponse response,
                                        Model model,SeckillUser user,@PathVariable("goodsId")long goodsId){
        GoodsVo goods = goodService.getGoodsVoByGoodsId(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setSeckillStatus(miaoshaStatus);
        return Result.success(vo);
    }


}
