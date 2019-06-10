package com.oszhugc.seckill.dao;

import com.oszhugc.seckill.domain.SeckillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author oszhugc
 * @Date 2019\6\7 0007 17:42
 **/
@Mapper
public interface SeckillUserDao {

    @Select("select * from miaosha_user where id = #{id}")
    SeckillUser getById(@Param("id") long id);

    @Update("update miaosha_user set password = #{password} where id = #{id}")
    void update(SeckillUser seckillUser);
}
