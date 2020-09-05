package com.github.codingdebugallday.factory;

import com.github.codingdebugallday.utils.ConnectionUtils;

/**
 * <p>
 * just test
 * </p>
 *
 * @author isaac 2020/9/6 3:37
 * @since 1.0.0
 */
public class CreateBeanFactory {

    public static ConnectionUtils getInstanceStatic() {
        return new ConnectionUtils();
    }

    public ConnectionUtils getInstance() {
        return new ConnectionUtils();
    }
}
