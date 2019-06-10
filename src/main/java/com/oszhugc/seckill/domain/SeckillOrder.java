package com.oszhugc.seckill.domain;

import lombok.Data;

/**
 * @author oszhugc
 * @Date 2019\6\3 0003 22:18
 **/
@Data
public class SeckillOrder {

    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
