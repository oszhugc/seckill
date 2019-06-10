package com.oszhugc.seckill.validator;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author oszhugc
 * @Date 2019\6\3 0003 23:00
 * @Version 2.0
 **/
@Target({METHOD,FIELD,ANNOTATION_TYPE,CONSTRUCTOR,PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {IsMobileValidator.class})
public @interface IsMobile {

    boolean required() default true;

    String message() default "手机号码格式错误";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default {};
}
