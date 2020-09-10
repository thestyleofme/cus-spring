package com.github.codingdebugallday.annotations;

import java.lang.annotation.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/10 11:32
 * @since 1.0.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoWired {

    String value() default "";

    boolean required() default true;
}
