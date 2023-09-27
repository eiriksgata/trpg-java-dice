package com.github.eiriksgata.trpg.dice.injection;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface InstructReflex {
    String[] value();

    //初始优先级为0 数值越大优先级越高 会越先执行
    int priority() default 0;
}
