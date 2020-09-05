package com.github.codingdebugallday.pojo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 拦截实例化之后的对象（实例化了并且属性注入了）
 * </p>
 *
 * @author isaac 2020/9/6 6:39
 * @since 1.0.0
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if("lazyResult".equalsIgnoreCase(beanName)){
            System.out.println("MyBeanPostProcessor before方法拦截处理lazyResult");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if("lazyResult".equalsIgnoreCase(beanName)){
            System.out.println("MyBeanPostProcessor after方法拦截处理lazyResult");
        }
        return bean;
    }
}
