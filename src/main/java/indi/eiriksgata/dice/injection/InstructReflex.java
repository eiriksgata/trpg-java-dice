package indi.eiriksgata.dice.injection;

import java.lang.annotation.*;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.dice.injection
 * date:2020/10/12
 **/
@Target({ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface InstructReflex {
    String[] value();

    //初始优先级为0 数值越大优先级越高 会越先执行
    int priority() default 0;
}
