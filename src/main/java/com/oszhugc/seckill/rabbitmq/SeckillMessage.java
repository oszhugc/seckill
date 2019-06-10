package com.oszhugc.seckill.rabbitmq;

import com.oszhugc.seckill.domain.SeckillUser;
import lombok.Data;

/**
 * @author oszhugc
 * @Date 2019\6\8 0008 10:40
 **/
@Data
public class SeckillMessage {

    private SeckillUser seckillUser;

    private long goodsId;

}
