package com.oszhugc.seckill.service;

import com.oszhugc.seckill.dao.GoodsDao;
import com.oszhugc.seckill.domain.SeckillGoods;
import com.oszhugc.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author oszhugc
 * @Date 2019\6\8 0008 11:09
 **/
@Service
public class GoodService {

    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId){
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goods){
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setGoodsId(goods.getId());
        int ret = goodsDao.reduceStock(seckillGoods);
        return ret > 0;
    }

    public void resetStock(List<GoodsVo> goodsVoList){
        goodsVoList.forEach(goods ->{
            SeckillGoods good = new SeckillGoods();
            good.setGoodsId(goods.getId());
            good.setStockCount(goods.getStockCount());
            goodsDao.resetStock(good);
        });

    }



}
