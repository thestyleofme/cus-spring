package com.github.codingdebugallday.factory;

import java.lang.reflect.Proxy;

import com.github.codingdebugallday.annotations.AutoWired;
import com.github.codingdebugallday.annotations.Service;
import com.github.codingdebugallday.utils.TransactionManager;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * <p>
 * 代理对象工厂：生成代理对象的
 * </p>
 *
 * @author isaac 2020/9/5 2:27
 * @since 1.0.0
 */
@SuppressWarnings("DuplicatedCode")
@Service("proxyFactory")
public class ProxyFactory {

    @AutoWired
    private TransactionManager transactionManager;

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * Jdk动态代理
     *
     * @param obj 委托对象
     * @return 代理对象
     */
    public Object getJdkProxy(Object obj) {
        // 获取代理对象
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    try {
                        // 开启事务(关闭事务的自动提交)
                        transactionManager.beginTransaction();
                        Object result = method.invoke(obj, args);
                        // 提交事务
                        transactionManager.commit();
                        return result;
                    } catch (Exception e) {
                        // 回滚事务
                        transactionManager.rollback();
                        // 抛出异常便于上层servlet捕获
                        throw e;
                    } finally {
                        transactionManager.clear();
                    }
                });
    }


    /**
     * 使用cglib动态代理生成代理对象
     *
     * @param obj 委托对象
     * @return 代理对象
     */
    public Object getCglibProxy(Object obj) {
        return Enhancer.create(obj.getClass(),
                (MethodInterceptor) (o, method, objects, methodProxy) -> {
                    try {
                        // 开启事务(关闭事务的自动提交)
                        transactionManager.beginTransaction();
                        Object result = method.invoke(obj, objects);
                        // 提交事务
                        transactionManager.commit();
                        return result;
                    } catch (Exception e) {
                        // 回滚事务
                        transactionManager.rollback();
                        // 抛出异常便于上层servlet捕获
                        throw e;
                    } finally {
                        transactionManager.clear();
                    }
                });
    }
}
