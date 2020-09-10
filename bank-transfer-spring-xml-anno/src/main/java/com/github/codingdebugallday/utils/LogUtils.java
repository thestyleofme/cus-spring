package com.github.codingdebugallday.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 测试aop
 * </p>
 *
 * @author isaac 2020/9/9 1:46
 * @since 1.0.0
 */
@Component
@Aspect
@EnableAspectJAutoProxy
public class LogUtils {

    private final Logger logger = LoggerFactory.getLogger(LogUtils.class);

    @Pointcut("execution(* com.github.codingdebugallday.service.impl.TransferServiceImpl.*(..))")
    public void pt1() {

    }

    @Before("pt1()")
    public void before(JoinPoint joinPoint) {
        joinPoint.getArgs();
        logger.debug("业务逻辑开始之前执行...");
    }

    @After("pt1()")
    public void after() {
        logger.debug("业务逻辑结束时执行（无论异常与否都执行）...");
    }

    @AfterThrowing(value = "pt1()", throwing = "e")
    public void exception(Throwable e) {
        logger.error("异常时执行...", e);
    }

    @AfterReturning(value = "pt1()", returning = "retVal")
    public void success(Object retVal) {
        logger.debug("业务逻辑正常时执行, 返回值：{}", retVal);
    }

    // @Around("pt1()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        logger.debug("环绕通知中的before...");
        Object result = null;
        try {
            // 控制原业务逻辑是否执行
            result = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } catch (Throwable throwable) {
            logger.debug("环绕通知中的exception...");
        } finally {
            logger.debug("环绕通知中的after...");
        }
        return result;
    }

}
