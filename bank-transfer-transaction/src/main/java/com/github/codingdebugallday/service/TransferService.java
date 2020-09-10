package com.github.codingdebugallday.service;

import java.math.BigDecimal;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/5 2:40
 * @since 1.0.0
 */
public interface TransferService {

    /**
     * 转账
     *
     * @param fromCardNo 转账账号
     * @param toCardNo   收账账号
     * @param money      转账金额
     * @throws Exception e
     */
    void transfer(String fromCardNo, String toCardNo, BigDecimal money);
}
