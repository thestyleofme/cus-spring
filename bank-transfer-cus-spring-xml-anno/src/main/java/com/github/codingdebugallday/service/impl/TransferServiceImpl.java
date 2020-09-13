package com.github.codingdebugallday.service.impl;

import java.math.BigDecimal;

import com.github.codingdebugallday.annotations.AutoWired;
import com.github.codingdebugallday.annotations.Service;
import com.github.codingdebugallday.annotations.Transactional;
import com.github.codingdebugallday.dao.AccountDao;
import com.github.codingdebugallday.pojo.Account;
import com.github.codingdebugallday.service.TransferService;

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

    @AutoWired
    private AccountDao accountDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(String fromCardNo, String toCardNo, BigDecimal money) throws Exception {
        Account from = accountDao.queryAccountByCardNo(fromCardNo);
        Account to = accountDao.queryAccountByCardNo(toCardNo);

        from.setMoney(from.getMoney().subtract(money));
        to.setMoney(to.getMoney().add(money));

        accountDao.updateAccountByCardNo(to);
        // 抛异常 测试事务
        int c = 1 / 0;
        accountDao.updateAccountByCardNo(from);
    }
}
