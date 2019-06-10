package com.oszhugc.seckill.dao;

import com.oszhugc.seckill.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author oszhugc
 * @Date 2019\6\7 0007 17:55
 * @Version 2.0
 **/
@Mapper
public interface UserDao {

    @Select("select * from user where id = {id}")
    User getById(@Param("id") int id);

    @Insert("insert into user(id,name) values (#{id}, #{name})")
    int insert(User user);
}
