package com.oszhugc.seckill.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author oszhugc
 * @Date 2019\6\3 0003 22:19
 **/
@Data
public class SeckillUser {

    private Long id;
    private String nickName;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
