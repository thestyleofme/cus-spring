package com.github.codingdebugallday.annotations;

import java.lang.annotation.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/10 11:34
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {

    String value() default "";
}
