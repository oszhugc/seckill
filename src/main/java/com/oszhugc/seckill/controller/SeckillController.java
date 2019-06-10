package com.oszhugc.seckill.controller;

import com.oszhugc.seckill.access.AccessLimit;
import com.oszhugc.seckill.domain.SeckillOrder;
import com.oszhugc.seckill.domain.SeckillUser;
import com.oszhugc.seckill.rabbitmq.MQSender;
import com.oszhugc.seckill.rabbitmq.SeckillMessage;
import com.oszhugc.seckill.redis.GoodsKey;
import com.oszhugc.seckill.redis.OrderKey;
import com.oszhugc.seckill.redis.RedisService;
import com.oszhugc.seckill.redis.SeckillKey;
import com.oszhugc.seckill.result.CodeMsg;
import com.oszhugc.seckill.result.Result;
import com.oszhugc.seckill.service.GoodService;
import com.oszhugc.seckill.service.OrderService;
import com.oszhugc.seckill.service.SeckillService;
import com.oszhugc.seckill.service.SeckillUserService;
import com.oszhugc.seckill.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.tree.ExpandVetoException;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

/**
 * @author oszhugc
 * @Date 2019\6\8 0008 18:04
 **/
public class SeckillController implements InitializingBean {

    @Autowired
    SeckillUserService seckillUserService;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodService goodService;
    @Autowired
    OrderService orderService;
    @Autowired
    SeckillService seckillService;
    @Autowired
    MQSender mqSender;

    private HashMap<Long,Boolean> localOverMap = new HashMap<>();

    /**
     * 系统初始化
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodService.listGoodsVo();
        if (goodsVoList == null){
            return;
        }

        goodsVoList.forEach(goodsVo -> {
            redisService.set(GoodsKey.getSeckillGoodsStock,""+goodsVo.getId(),goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(),false);
        });
    }


    @RequestMapping(value = "reset",method = RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model){
        List<GoodsVo> goodsVos = goodService.listGoodsVo();
        goodsVos.forEach(goodsVo -> {
            goodsVo.setStockCount(10);
            redisService.set(GoodsKey.getSeckillGoodsStock,""+goodsVo.getId(),10);
            localOverMap.put(goodsVo.getId(),false);
        });
        redisService.delete(OrderKey.getSeckillOrderByUidGid);
        redisService.delete(SeckillKey.isGoodsOver);
        seckillService.reset(goodsVos);
        return Result.success(true);
    }


    @RequestMapping(value = "/{path}/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, SeckillUser user, @RequestParam("goodsId")long goodsId,
                                   @PathVariable("path")String path){
        model.addAttribute("user",user);
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //验证path
        boolean check = seckillService.checkPath(user, goodsId, path);
        if (!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存标记,减少redis访问
        Boolean over = localOverMap.get(goodsId);
        if (over){
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //预减内存
        Long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);//10
        if (stock < 10){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //判断是否已经秒杀到了,
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null){
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }
        //入队
        SeckillMessage sm = new SeckillMessage();
        sm.setSeckillUser(user);
        sm.setGoodsId(goodsId);
        mqSender.send(sm);
        return Result.success(0);//排队中...
    }

    /**
     * orderId: 成功
     * -1: 秒杀失败
     * 0: 排队中..
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model,SeckillUser user,@RequestParam("goodsId")long goodsId){
        model.addAttribute("user",user);
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = seckillService.getSeckillResult(user.getId(), goodsId);
        return Result.success(result);
    }

    @AccessLimit(seconds = 5,maxCount = 5,needLogin = true)
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    public Result<String> getSeckillPath(HttpServletRequest request,SeckillUser user,
                                         @RequestParam("goodsId")long goodsId,
                                         @RequestParam(value = "verifyCode",defaultValue = "0")int verifyCode){
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean check = seckillService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = seckillService.createSeckillPath(user, goodsId);
        return Result.success(path);
    }

    @RequestMapping(value = "/verifyCode",method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getSeckillVerifyCode(HttpServletResponse response,SeckillUser user,
                                               @RequestParam("goodsId")long goodsId){
        if (user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image = seckillService.createVerifyCode(user, goodsId);
            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(image,"JPEG",out);
            out.flush();
            out.close();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(CodeMsg.SEKILL_FAIL);
        }
    }

}
