package com.github.codingdebugallday.service.impl;

import java.math.BigDecimal;

import com.github.codingdebugallday.dao.AccountDao;
import com.github.codingdebugallday.pojo.Account;
import com.github.codingdebugallday.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/5 2:32
 * @since 1.0.0
 */
@Service("transferService")
@EnableTransactionManagement
public class TransferServiceImpl implements TransferService {

    private final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    private final AccountDao accountDao;

    public TransferServiceImpl(@Qualifier("accountDao") AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(String fromCardNo, String toCardNo, BigDecimal money) {
        logger.debug("执行转账业务逻辑");
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
