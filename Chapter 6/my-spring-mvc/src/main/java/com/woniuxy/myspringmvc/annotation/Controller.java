package com.woniuxy.myspringmvc.annotation;

/**
 * 放在类上，用于标注该类是否需要实例化
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {

}
