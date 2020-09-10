package test;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.github.codingdebugallday.dao.AccountDao;
import com.github.codingdebugallday.service.TransferService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/6 2:59
 * @since 1.0.0
 */
public class IocTest {

    private ClassPathXmlApplicationContext applicationContext;

    @Before
    public void before() {
        applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
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

    /**
     * 可测试bean懒加载
     * 可测试spring生命周期
     */
    @Test
    public void testLazy() {
        // 打断点在这一行 debug看初始化applicationContext.beanFactory.singletonObjects里有没有，没有则是懒加载
        Object lazyResult = applicationContext.getBean("lazyResult");
        Assert.assertNotNull(lazyResult);
        applicationContext.close();
    }

    @Test
    public void testFactoryBean() {
        Object companyBean = applicationContext.getBean("companyBean");
        Assert.assertNotNull(companyBean);
        // id前加& 获取创建这个bean的factoryBean
        Object companyFactoryBean = applicationContext.getBean("&companyBean");
        Assert.assertNotNull(companyFactoryBean);
    }

    @Test
    public void testAop() throws Exception {
        TransferService transferService = applicationContext.getBean(TransferService.class);
        Assert.assertNotNull(transferService);
        // 数据库不会更新 因为datasource设置的是手动提交
        // 页面方式使用代理会去提交回滚事务，这里只是单纯做测试
        transferService.transfer("6029621011000",
                "6029621011001",
                new BigDecimal(100));
    }
}
