package com.oszhugc.seckill.domain;

import lombok.Data;

/**
 * @author oszhugc
 * @Date 2019\6\3 0003 22:14
 **/
@Data
public class Goods {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
}
