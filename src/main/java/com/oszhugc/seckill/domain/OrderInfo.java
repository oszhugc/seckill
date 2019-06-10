package com.oszhugc.seckill.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author oszhugc
 * @Date 2019\6\3 0003 22:21
 **/
@Data
public class OrderInfo {

    private Long id;
    private Long userId;
    private Long goodsId;
    private Long deliveryAddrId;
    private String goodsname;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;
    private Date createDate;
    private Date payDate;
}
