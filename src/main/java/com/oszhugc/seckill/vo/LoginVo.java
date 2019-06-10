package com.oszhugc.seckill.vo;

import com.oszhugc.seckill.validator.IsMobile;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author oszhugc
 * @Date 2019\6\3 0003 22:57
 **/
@Data
@ToString
public class LoginVo {

    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min=32)
    private String password;
}
