package com.oszhugc.seckill.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author oszhugc
 * @Date 2019\6\3 0003 22:16
 **/
@Data
public class SeckillGoods {

    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
