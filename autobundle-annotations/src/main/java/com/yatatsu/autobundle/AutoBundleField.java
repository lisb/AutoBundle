package com.yatatsu.autobundle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface AutoBundleField {
    boolean required() default true;
    String key() default "";
    Class<? extends AutoBundleConverter> converter() default DefaultAutoBundleConverter.class;
    int flags() default 0;

    int FLAG_NO_BUILDER_PARAMETER = 1;
    int FLAG_GENERATE_GETTER = 2;
}
