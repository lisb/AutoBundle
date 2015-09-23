package com.yatatsu.autobundle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Arg {
    boolean required() default true;
    String key() default "";
    Class<? extends Converter> converter() default DefaultConverter.class;
}