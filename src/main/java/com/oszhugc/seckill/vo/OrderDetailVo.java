package com.oszhugc.seckill.vo;

import com.oszhugc.seckill.domain.OrderInfo;
import lombok.Data;

/**
 * @author oszhugc
 * @Date 2019\6\3 0003 23:14
 **/
@Data
public class OrderDetailVo {

    private GoodsVo goods;

    private OrderInfo order;
}
