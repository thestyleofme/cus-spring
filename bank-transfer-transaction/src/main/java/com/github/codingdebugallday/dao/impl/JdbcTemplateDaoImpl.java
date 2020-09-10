package com.github.codingdebugallday.dao.impl;

import com.github.codingdebugallday.dao.AccountDao;
import com.github.codingdebugallday.pojo.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/10 0:51
 * @since 1.0.0
 */
@Repository("accountDao")
public class JdbcTemplateDaoImpl implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account queryAccountByCardNo(String cardNo) {
        String sql = "select * from account where cardNo=?";
        return jdbcTemplate.queryForObject(sql,
                (resultSet, i) -> {
                    Account account = new Account();
                    account.setName(resultSet.getString("name"));
                    account.setCardNo(resultSet.getString("cardNo"));
                    account.setMoney(resultSet.getBigDecimal("money"));
                    return account;
                },
                cardNo);
    }

    @Override
    public int updateAccountByCardNo(Account account) {
        String sql = "update account set money=? where cardNo=?";
        return jdbcTemplate.update(sql, account.getMoney(), account.getCardNo());
    }
}
