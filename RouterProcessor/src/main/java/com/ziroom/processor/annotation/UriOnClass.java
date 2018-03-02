package com.ziroom.processor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lvhl on 2016/12/22.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface UriOnClass {
    String value() default "";
}
