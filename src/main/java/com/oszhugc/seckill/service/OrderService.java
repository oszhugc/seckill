package com.oszhugc.seckill.service;

import com.oszhugc.seckill.dao.OrderDao;
import com.oszhugc.seckill.domain.OrderInfo;
import com.oszhugc.seckill.domain.SeckillOrder;
import com.oszhugc.seckill.domain.SeckillUser;
import com.oszhugc.seckill.redis.OrderKey;
import com.oszhugc.seckill.redis.RedisService;
import com.oszhugc.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author oszhugc
 * @Date 2019\6\8 0008 11:40
 **/
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RedisService redisService;

    public SeckillOrder getSeckillOrderByUserIdGoodsId(long userId,long goodsId){
        return redisService.get(OrderKey.getSeckillOrderByUidGid,userId+"_"+goodsId,SeckillOrder.class);
    }

    public OrderInfo getOrderById(long orderId){
        return orderDao.getOrderById(orderId);
    }

    @Transactional
    public OrderInfo createOrder(SeckillUser user, GoodsVo goods){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsname(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId(user.getId());
        orderDao.insertSeckillOrder(seckillOrder);

        redisService.set(OrderKey.getSeckillOrderByUidGid,user.getId()+"_"+goods.getId(),seckillOrder);

        return orderInfo;
    }

    public void deleteOrders(){
        orderDao.deleteOrders();
        orderDao.deleteSeckillOrders();
    }


}
