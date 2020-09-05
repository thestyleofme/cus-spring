package test;

import java.sql.SQLException;

import com.github.codingdebugallday.SpringConfiguration;
import com.github.codingdebugallday.dao.AccountDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/6 2:59
 * @since 1.0.0
 */
public class IocTest {

    private AnnotationConfigApplicationContext applicationContext;

    @Before
    public void before() {
        applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        // 不推荐
        // new FileSystemXmlApplicationContext("文件系统绝对路径")
    }

    @Test
    public void testIoc() throws SQLException {
        // ConnectionUtils connectionUtils = (ConnectionUtils) applicationContext.getBean("connectionUtils");
        // Assert.assertNotNull(connectionUtils);
        AccountDao accountDao = (AccountDao) applicationContext.getBean("accountDao");
        Assert.assertNotNull(accountDao);
        // Account account = accountDao.queryAccountByCardNo("6029621011000");
        applicationContext.close();
    }
}
