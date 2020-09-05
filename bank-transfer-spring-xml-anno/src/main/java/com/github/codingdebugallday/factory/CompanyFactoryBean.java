package com.github.codingdebugallday.factory;

import com.github.codingdebugallday.pojo.Company;
import org.springframework.beans.factory.FactoryBean;

/**
 * <p>
 * BeanFactory与FactoryBean区别
 * <p>
 * BeanFactory接口是容器的顶级接口,定义了容器的一些基础行为,负责生产和管理Bean的一个工厂,
 * 具体使用它下面的子接口类型,比如ApplicationContext;
 * <p>
 * 此处我们重点分析FactoryBean
 * Spring中Bean有两种,一种是普通Bean,一种是工厂Bean (FactoryBean) ,
 * FactoryBean可以生成某一个类型的Bean实例(返回给我们),也就是说我们可以借助于它自定义Bean的创建过程。
 * <p>
 * 与Bean创建的三种方式中的静态方法和实例化方法和FactoryBean作用类似
 * <p>
 * FactoryBean使用较多,尤其在Spring框架一些组件中会使用,还有其他框架和Spring框架整合时使用,
 * 可以让我们自定义Bean的创建过程(完成复杂Bean的定义)
 * </p>
 *
 * @author isaac 2020/9/6 6:08
 * @since 1.0.0
 */
public class CompanyFactoryBean implements FactoryBean<Company> {

    /**
     * 格式：公司名称,地址,规模按逗号分割
     * name,address,scale
     */
    private String companyInfo;

    public void setCompanyInfo(String companyInfo) {
        this.companyInfo = companyInfo;
    }

    @Override
    public Company getObject() throws Exception {
        // 创建复杂对象Company
        Company company = new Company();
        String[] split = companyInfo.split(",");
        company.setName(split[0]);
        company.setAddress(split[1]);
        company.setScale(Integer.parseInt(split[2]));
        return company;
    }

    @Override
    public Class<?> getObjectType() {
        return Company.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
