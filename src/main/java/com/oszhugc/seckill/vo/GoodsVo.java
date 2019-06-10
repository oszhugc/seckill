package com.oszhugc.seckill.vo;

import com.oszhugc.seckill.domain.Goods;
import lombok.Data;

import java.util.Date;

/**
 * @author oszhugc
 * @Date 2019\6\3 0003 22:55
 **/
@Data
public class GoodsVo extends Goods {

    private Double seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;



}
