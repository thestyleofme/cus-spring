package com.github.codingdebugallday.utils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/5 2:43
 * @since 1.0.0
 */
public class ConnectionUtils {

    /**
     * 存储当前线程的连接
     */
    private final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    /**
     * 从当前线程获取连接
     */
    public Connection getCurrentThreadConn() throws SQLException {
        // 判断当前线程中是否已经绑定连接，如果没有绑定，需要从连接池获取一个连接绑定到当前线程
        Connection connection = threadLocal.get();
        if (connection == null) {
            // 从连接池拿连接并绑定到线程
            connection = DruidUtils.getInstance().getConnection();
            connection.setAutoCommit(false);
            // 绑定到当前线程
            threadLocal.set(connection);
        }
        return connection;
    }

    public void remove(){
        threadLocal.remove();
    }
}
