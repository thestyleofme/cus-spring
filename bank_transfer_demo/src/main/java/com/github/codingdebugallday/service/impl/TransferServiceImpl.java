package com.github.codingdebugallday.service.impl;

import java.math.BigDecimal;

import com.github.codingdebugallday.pojo.Account;
import com.github.codingdebugallday.service.TransferService;
import com.github.codingdebugallday.dao.AccountDao;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/5 2:32
 * @since 1.0.0
 */
public class TransferServiceImpl implements TransferService {

    private AccountDao accountDao;

    /**
     * 构造函数传值/set方法传值
     *
     * @param accountDao AccountDao
     */
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void transfer(String fromCardNo, String toCardNo, BigDecimal money) throws Exception {
        Account from = accountDao.queryAccountByCardNo(fromCardNo);
        Account to = accountDao.queryAccountByCardNo(toCardNo);

        from.setMoney(from.getMoney().subtract(money));
        to.setMoney(to.getMoney().add(money));

        accountDao.updateAccountByCardNo(to);
        // 抛异常 测试事务
        // int c = 1 / 0;
        accountDao.updateAccountByCardNo(from);
    }
}
