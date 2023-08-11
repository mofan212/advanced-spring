package indi.mofan.bean.a02;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;

/**
 * @author mofan
 * @date 2023/8/11 23:39
 */
public class DefaultListableBeanFactoryTest {
    static class MyBean {
    }

    public static void main(String[] args) {
        // 既实现了 BeanFactory，又实现了 BeanDefinitionRegistry
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // ClassPathBeanDefinitionScanner 的一种替代，编程式显式注册 bean
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
        reader.registerBean(MyBean.class);
        MyBean bean = beanFactory.getBean(MyBean.class);
        System.out.println(bean);
    }
}
