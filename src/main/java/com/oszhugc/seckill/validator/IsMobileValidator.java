package com.oszhugc.seckill.validator;

import com.oszhugc.seckill.util.ValidatorUtil;
import com.oszhugc.seckill.validator.IsMobile;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author oszhugc
 * @Date 2019\6\3 0003 23:04
 **/
public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {

   private boolean required = false;

    @Override
    public void initialize(IsMobile isMobile) {
        required = isMobile.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (required){
            return ValidatorUtil.isMobile(s);
        }else {
            if (StringUtils.isEmpty(s)){
                return true;
            }else {
                return ValidatorUtil.isMobile(s);
            }
        }
    }
}
