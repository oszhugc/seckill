package com.oszhugc.seckill.service;

import com.oszhugc.seckill.domain.OrderInfo;
import com.oszhugc.seckill.domain.SeckillOrder;
import com.oszhugc.seckill.domain.SeckillUser;
import com.oszhugc.seckill.redis.RedisService;
import com.oszhugc.seckill.redis.SeckillKey;
import com.oszhugc.seckill.util.MD5Util;
import com.oszhugc.seckill.util.UUIDUtil;
import com.oszhugc.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

/**
 * @author oszhugc
 * @Date 2019\6\8 0008 11:18
 **/
@Service
public class SeckillService {

    @Autowired
    private GoodService goodService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo seckill(SeckillUser user, GoodsVo goods){
        //减库存,下订单,写入秒杀订单
        boolean success = goodService.reduceStock(goods);
        if (success){
            return orderService.createOrder(user,goods);
        }else {
            setGoodsOver(goods.getId());
            return null;
        }

    }
    
    
    public long getSeckillResult(Long userId,long goodsId){
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(userId, goodsId);
        if (order != null){//秒杀成功
            return order.getOrderId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver){
                return -1;
            }else {
                return 0;
            }
        }
            
    }

    private boolean getGoodsOver(Long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver,goodsId.toString());
    }


    private void setGoodsOver(Long id) {
        redisService.set(SeckillKey.isGoodsOver,id.toString(),true);
    }


    public void reset(List<GoodsVo> goodsVoList){
        goodService.resetStock(goodsVoList);
        orderService.deleteOrders();
    }

    public boolean checkPath(SeckillUser user,Long goodsId,String path){
        if (user == null || path == null){
            return false;
        }

        String pathOld = redisService.get(SeckillKey.getSeckillPath, user.getId() + "_" + goodsId, String.class);
        return path.equals(pathOld);
    }

    public String createSeckillPath(SeckillUser user,long goodsId){
        if (user == null || goodsId <= 0){
            return null;
        }
        String s = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(SeckillKey.getSeckillPath,user.getId()+"_"+goodsId,s);
        return s;
    }

    public BufferedImage createVerifyCode(SeckillUser user,long goodsId){
        if (user == null || goodsId <= 0){
            return null;
        }

        int width = 80;
        int height = 32;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0,0,width,height);
        g.setColor(Color.BLACK);
        g.drawRect(0,0,width-1,height-1);
        Random rdm = new Random();
        for (int i = 0; i < 50; i++){
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x,y,0,0);
        }
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0,100,0));
        g.setFont(new Font("Candara",Font.BOLD,24));
        g.drawString(verifyCode,8,24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(SeckillKey.getSeckillVerifyCode,user.getId()+","+goodsId,rnd);
        //输出图片
        return image;
    }

    private int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (int)engine.eval(exp);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


    public boolean checkVerifyCode(SeckillUser user,long goodsId,int verifyCode){
        if (user == null || goodsId <= 0){
            return false;
        }
        Integer oldCode = redisService.get(SeckillKey.getSeckillVerifyCode, user.getId() + "," + goodsId, Integer.class);
        if (oldCode == null || oldCode - verifyCode != 0){
            return false;
        }
        redisService.delete(SeckillKey.getSeckillVerifyCode,user.getId()+","+goodsId);
        return true;
    }



    private static char[] ops = new char[]{'+','-','*'};

    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+num1+op1+num2+op2+num3;
        return exp;

    }

}
