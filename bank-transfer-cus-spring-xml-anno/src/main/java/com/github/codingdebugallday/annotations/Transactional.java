package com.github.codingdebugallday.annotations;

import java.lang.annotation.*;

/**
 * <p>
 * 简单点，抛异常事务就回滚
 * </p>
 *
 * @author isaac 2020/9/10 11:35
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Transactional {

    String value() default "transactionManager";

    Class<? extends Throwable>[] rollbackFor() default {Exception.class};
}
