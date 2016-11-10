package com.yatatsu.autobundle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If {@link AbstractIntentBuilder} is set,
 * generated IntentBuilder is not final but also abstract.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface AbstractIntentBuilder {
}
