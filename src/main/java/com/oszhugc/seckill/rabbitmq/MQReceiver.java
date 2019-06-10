package com.oszhugc.seckill.rabbitmq;

import com.oszhugc.seckill.domain.SeckillOrder;
import com.oszhugc.seckill.domain.SeckillUser;
import com.oszhugc.seckill.redis.RedisService;
import com.oszhugc.seckill.service.GoodService;
import com.oszhugc.seckill.service.OrderService;
import com.oszhugc.seckill.service.SeckillService;
import com.oszhugc.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author oszhugc
 * @Date 2019\6\8 0008 17:20
 **/
@Service
@Slf4j
public class MQReceiver {

    @Autowired
    RedisService redisService;
    @Autowired
    GoodService goodService;
    @Autowired
    OrderService orderService;
    @Autowired
    SeckillService seckillService;

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receive(String msg){
        log.info("receive message: {}",msg);
        SeckillMessage sm = RedisService.stringToBean(msg, SeckillMessage.class);
        SeckillUser user = sm.getSeckillUser();
        long goodsId = sm.getGoodsId();

        GoodsVo goods = goodService.getGoodsVoByGoodsId(goodsId);
        if (goods.getStockCount() <= 0){
            return;
        }
        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null){
            return;
        }
        //减库存 下订单 写入秒杀订单
        seckillService.seckill(user,goods);
    }


    @RabbitListener(queues = MQConfig.QUEUE)
    public void receiveGeneral(String msg){
        log.info("receive msg: {}",msg);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopic1(String msg){
        log.info("receive queue1 msg: {}",msg);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopic2(String msg){
        log.info("receive queue2 msg: {}",msg);
    }

    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void receiveHeaderQueue(byte[] msg){
        log.info("header queue msg: {}",new String(msg));
    }

}
