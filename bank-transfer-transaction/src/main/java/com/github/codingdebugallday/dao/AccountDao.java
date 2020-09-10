package com.github.codingdebugallday.dao;

import com.github.codingdebugallday.pojo.Account;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/5 2:16
 * @since 1.0.0
 */
public interface AccountDao {

    /**
     * 通过账号查询账户
     *
     * @param cardNo 账号
     * @return Account
     */
    Account queryAccountByCardNo(String cardNo);

    /**
     * 更新账户
     *
     * @param account Account
     * @return int
     */
    int updateAccountByCardNo(Account account);
}
