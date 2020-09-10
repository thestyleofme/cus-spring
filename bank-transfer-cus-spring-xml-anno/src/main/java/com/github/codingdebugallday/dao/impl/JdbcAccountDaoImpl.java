package com.github.codingdebugallday.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.github.codingdebugallday.annotations.AutoWired;
import com.github.codingdebugallday.annotations.Service;
import com.github.codingdebugallday.dao.AccountDao;
import com.github.codingdebugallday.pojo.Account;
import com.github.codingdebugallday.utils.CloseUtil;
import com.github.codingdebugallday.utils.ConnectionUtils;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/5 2:14
 * @since 1.0.0
 */
@Service("accountDao")
public class JdbcAccountDaoImpl implements AccountDao {

    @AutoWired
    private ConnectionUtils connectionUtils;

    public void setConnectionUtils(ConnectionUtils connectionUtils) {
        this.connectionUtils = connectionUtils;
    }

    @Override
    public Account queryAccountByCardNo(String cardNo) throws SQLException {
        // 从当前线程当中获取绑定的connection连接
        Connection con = connectionUtils.getCurrentThreadConn();
        String sql = "select * from account where cardNo=?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, cardNo);
            resultSet = preparedStatement.executeQuery();
            Account account = new Account();
            while (resultSet.next()) {
                account.setCardNo(resultSet.getString("cardNo"));
                account.setName(resultSet.getString("name"));
                account.setMoney(resultSet.getBigDecimal("money"));
            }
            return account;
        } finally {
            CloseUtil.close(resultSet, preparedStatement);
        }
    }

    @Override
    public void updateAccountByCardNo(Account account) throws SQLException {
        // 从当前线程当中获取绑定的connection连接
        Connection con = connectionUtils.getCurrentThreadConn();
        String sql = "update account set money=? where cardNo=?";
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, account.getMoney());
            preparedStatement.setString(2, account.getCardNo());
            preparedStatement.executeUpdate();
        }
    }
}
