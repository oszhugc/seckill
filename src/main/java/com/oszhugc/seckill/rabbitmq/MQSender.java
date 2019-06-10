package com.oszhugc.seckill.rabbitmq;

import com.oszhugc.seckill.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author oszhugc
 * @Date 2019\6\8 0008 10:58
 **/
@Service
@Slf4j
public class MQSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendSeckillMessage(SeckillMessage seckillMessage){
        String msg = RedisService.beanToString(seckillMessage);
        log.info("send message: {}",msg);
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE,msg);
    }



    public void send(Object message){
        String msg = RedisService.beanToString(message);
        log.info("send message: {}",msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);

    }


    public void sendTopic(Object message){
        String msg = RedisService.beanToString(message);
        log.info("send topic message: {}",msg);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key1",msg+"1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key2",msg+"2");
    }


    public void sendFanout(Object message){
        String msg = RedisService.beanToString(message);
        log.info("send fanout message: {}", msg);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg);
    }


    public void sendHeader(Object message){
        String msg = RedisService.beanToString(message);
        log.info("send fanout message: {}", msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("header1","value1");
        properties.setHeader("header2","value2");
        Message obj = new Message(msg.getBytes(), properties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,"",obj);
    }

}
