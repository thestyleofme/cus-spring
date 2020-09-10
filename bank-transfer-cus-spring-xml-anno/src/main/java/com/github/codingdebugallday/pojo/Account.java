package com.github.codingdebugallday.pojo;

import java.math.BigDecimal;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/5 2:30
 * @since 1.0.0
 */
public class Account {

    private String cardNo;
    private String name;
    private BigDecimal money;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @Override
    public String toString() {
        return "Account{" +
                "cardNo='" + cardNo + '\'' +
                ", name='" + name + '\'' +
                ", money=" + money +
                '}';
    }
}
