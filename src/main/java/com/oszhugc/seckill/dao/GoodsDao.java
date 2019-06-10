package com.oszhugc.seckill.dao;

import com.oszhugc.seckill.domain.SeckillGoods;
import com.oszhugc.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author oszhugc
 * @Date 2019\6\7 0007 17:33
 **/
@Mapper
public interface GoodsDao {

    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,miaosha_price " +
            "from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    List<GoodsVo> listGoodsVo();

    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,miaosha_price " +
            "from miaosha_goods mg left join goods g on mg.goods_id = g.id " +
            "where g.id = #{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    @Update("update miaosha_goods set stock_count = stock_count -1 " +
            "where goods_id = #{goodsId} and stock_count > 0")
    int reduceStock(SeckillGoods seckillGoods);

    @Update("update miaosha_goods set stock_count = #{stockCount} " +
            "where goods_id = #{goodsId}")
    int resetStock(SeckillGoods seckillGoods);


}
