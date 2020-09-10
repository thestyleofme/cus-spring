package com.github.codingdebugallday.utils;

import java.sql.SQLException;

import com.github.codingdebugallday.annotations.AutoWired;
import com.github.codingdebugallday.annotations.Service;

/**
 * <p>
 * 事务管理器类：负责手动事务的开启、提交、回滚
 * </p>
 *
 * @author isaac 2020/9/5 2:50
 * @since 1.0.0
 */
@Service("transactionManager")
public class TransactionManager {

    @AutoWired
    private ConnectionUtils connectionUtils;

    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    /**
     * 开启手动事务控制
     */
    public void beginTransaction() throws SQLException {
        connectionUtils.getCurrentThreadConn().setAutoCommit(false);
    }

    /**
     * 提交事务
     */
    public void commit() throws SQLException {
        connectionUtils.getCurrentThreadConn().commit();
    }

    /**
     * 回滚事务
     */
    public void rollback() throws SQLException {
        connectionUtils.getCurrentThreadConn().rollback();
    }

    /**
     * 清楚当前connection
     */
    public void clear() throws SQLException {
        connectionUtils.getCurrentThreadConn().close();
        connectionUtils.remove();
    }
}
