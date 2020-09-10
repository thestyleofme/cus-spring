package com.github.codingdebugallday.factory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import com.alibaba.druid.util.StringUtils;
import com.github.codingdebugallday.annotations.AutoWired;
import com.github.codingdebugallday.annotations.Service;
import com.github.codingdebugallday.annotations.Transactional;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 工厂类，生产对象（使用反射技术）
 * 任务一：读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
 * 任务二：对外提供获取实例对象的接口（根据id获取）
 * </p>
 *
 * @author isaac 2020/9/5 2:18
 * @since 1.0.0
 */
public class BeanFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanFactory.class);

    /**
     * 存储对象
     */
    private static final Map<String, Object> MAP = new HashMap<>();

    static {
        // 任务一：读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
        // 加载xml
        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
        // 解析xml
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            // 解析xml实例化bean
            doXmlBean(rootElement);
            // 实例化完成之后维护对象的依赖关系，检查哪些对象需要传值进入，根据它的配置，我们传入相应的值
            doXmlBeanDependency(rootElement);
            // @Service注解标识的类实例化
            doAnnoBean(rootElement);
            // @AutoWired 依赖注入
            doAnnoDependency();
        } catch (DocumentException | ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            LOGGER.error("parse beans.xml error", e);
        }
    }

    private static void doAnnoBean(Element rootElement) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Element scanElement = rootElement.element("component-scan");
        String basePackage = scanElement.attributeValue("base-package");
        Reflections reflections = new Reflections(basePackage);
        // 获取被@Service标识的所有类
        Set<Class<?>> serviceAnnoSet = reflections.getTypesAnnotatedWith(Service.class);
        for (Class<?> clazz : serviceAnnoSet) {
            // 获取beanName 配置了value 以value为准
            Service annotation = clazz.getAnnotation(Service.class);
            String beanName = annotation.value();
            if (StringUtils.isEmpty(beanName)) {
                // getName返回全限定类名 需要去掉包名
                String[] names = clazz.getName().split("\\.");
                // beanName 首字母小写
                beanName = toLowerFirstCase(names[names.length - 1]);
            }
            // 这里以xml优先级高 xml定义了 注解定义无效
            if (MAP.containsKey(beanName)) {
                continue;
            }
            // 反射生成bean
            Object bean = Thread.currentThread()
                    .getContextClassLoader()
                    .loadClass(clazz.getName())
                    .getDeclaredConstructor()
                    .newInstance();
            MAP.put(beanName, bean);
        }
    }

    private static void doAnnoDependency() throws InvocationTargetException, IllegalAccessException {
        for (Map.Entry<String, Object> entry : MAP.entrySet()) {
            Object bean = entry.getValue();
            Class<?> clazz = bean.getClass();
            // 判断是否被@AutoWired修饰 使用set方法依赖注入
            doAutoWired(clazz, bean);
            // 判断该类或者方法是否有@Transactional注解修饰 若注释了 需生成代理对象
            bean = doTx(clazz, bean);
            // 把处理之后的bean重新放到map中
            MAP.put(entry.getKey(), bean);
        }
    }

    private static void doAutoWired(Class<?> clazz, Object bean) throws InvocationTargetException, IllegalAccessException {
        // 获取该类所有属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoWired.class) &&
                    field.getAnnotation(AutoWired.class).required()) {
                String[] names = field.getName().split("\\.");
                String name = names[names.length - 1];
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (method.getName().equalsIgnoreCase("set" + name)) {
                        // 调用set方法注入 如 setAccountDao
                        method.invoke(bean, MAP.get(toLowerFirstCase(name)));
                    }
                }
            }
        }
    }

    private static Object doTx(Class<?> clazz, Object bean) {
        boolean isExistTxAnno = isExistTxAnno(clazz);
        if (isExistTxAnno) {
            // 首先获取代理工厂类
            ProxyFactory proxyFactory = (ProxyFactory) getBean("proxyFactory");
            // 该对象是否有实现接口
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length > 0) {
                // jdk代理
                bean = proxyFactory.getJdkProxy(bean);
            } else {
                // cglib代理
                bean = proxyFactory.getCglibProxy(bean);
            }
        }
        return bean;
    }

    private static boolean isExistTxAnno(Class<?> clazz) {
        // 该类有直接返回true
        if (clazz.isAnnotationPresent(Transactional.class)) {
            return true;
        }
        // 判断方法有没有
        return Arrays.stream(clazz.getMethods())
                .anyMatch(method -> method.isAnnotationPresent(Transactional.class));
    }

    private static String toLowerFirstCase(String simpleName) {
        final char[] chars = simpleName.toCharArray();
        if (chars[0] >= 'A' && chars[0] <= 'Z') {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }

    private static void doXmlBeanDependency(Element rootElement) throws InvocationTargetException, IllegalAccessException {
        // 有property子元素的bean就有传值需求
        List<Node> propertyNodeList = rootElement.selectNodes("//property");
        // 解析property，获取父元素
        //<property name="AccountDao" ref="accountDao"></property>
        for (Node node : propertyNodeList) {
            DefaultElement element = (DefaultElement) node;
            String name = element.attributeValue("name");
            String ref = element.attributeValue("ref");
            // 找到当前需要被处理依赖关系的bean
            Element parent = element.getParent();
            // 调用父元素对象的反射功能
            String parentId = parent.attributeValue("id");
            Object parentObject = MAP.get(parentId);
            // 遍历父对象中的所有方法，找到"set" + name
            Method[] methods = parentObject.getClass().getMethods();
            for (Method method : methods) {
                // 该方法就是 setAccountDao(AccountDao accountDao)
                if (method.getName().equalsIgnoreCase("set" + name)) {
                    method.invoke(parentObject, MAP.get(ref));
                }
            }
            // 把处理之后的parentObject重新放到map中
            MAP.put(parentId, parentObject);
        }
    }

    private static void doXmlBean(Element rootElement) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Element> beanList = rootElement.elements("bean");
        for (Element element : beanList) {
            // 处理每个bean元素，获取到该元素的id 和 class 属性
            String id = element.attributeValue("id");
            String clazz = element.attributeValue("class");
            // 通过反射技术实例化对象
            Class<?> aClass = Class.forName(clazz);
            // 实例化之后的对象
            Object o = aClass.getClassLoader()
                    .loadClass(clazz)
                    .getDeclaredConstructor()
                    .newInstance();
            // 存储到map中待用
            MAP.put(id, o);
        }
    }

    /**
     * 任务二：对外提供获取实例对象的接口（根据id获取）
     *
     * @param id id
     * @return Object
     */
    public static Object getBean(String id) {
        return MAP.get(id);
    }

}
