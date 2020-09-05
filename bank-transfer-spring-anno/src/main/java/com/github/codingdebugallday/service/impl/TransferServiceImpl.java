package com.github.codingdebugallday.service.impl;

import java.math.BigDecimal;

import com.github.codingdebugallday.dao.AccountDao;
import com.github.codingdebugallday.pojo.Account;
import com.github.codingdebugallday.service.TransferService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/5 2:32
 * @since 1.0.0
 */
@Service("transferService")
public class TransferServiceImpl implements TransferService {

    private final AccountDao accountDao;

    public TransferServiceImpl(@Qualifier("accountDao") AccountDao accountDao) {
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
