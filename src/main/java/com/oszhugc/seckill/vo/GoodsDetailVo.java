package com.oszhugc.seckill.vo;

import com.oszhugc.seckill.domain.SeckillUser;
import lombok.Data;

/**
 * @author oszhugc
 * @Date 2019\6\3 0003 22:54
 **/
@Data
public class GoodsDetailVo {

    private int seckillStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods;
    private SeckillUser user;
}
