package indi.mofan.a47;

import lombok.SneakyThrows;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2023/2/1 23:02
 */
@Configuration
public class A47_3 {
    public static void main(String[] args) throws NoSuchFieldException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A47_3.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        testPrimary(beanFactory);
        testDefault(beanFactory);
    }

    @SneakyThrows
    private static void testDefault(DefaultListableBeanFactory beanFactory) {
        DependencyDescriptor dd = new DependencyDescriptor(Target2.class.getDeclaredField("service3"), false);
        Class<?> type = dd.getDependencyType();
        for (String name : BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, type)) {
            if (name.equals(dd.getDependencyName())) {
                System.out.println("default: " + name);
            }
        }
    }

    @SneakyThrows
    private static void testPrimary(DefaultListableBeanFactory beanFactory) {
        DependencyDescriptor dd = new DependencyDescriptor(Target1.class.getDeclaredField("service"), false);
        Class<?> type = dd.getDependencyType();
        for (String name : BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, type)) {
            if (beanFactory.getMergedBeanDefinition(name).isPrimary()) {
                System.out.println("primary: " + name);
            }
        }
    }

    static class Target1 {
        @Autowired
        private Service service;
    }

    static class Target2 {
        @Autowired
        private Service service3;
    }

    interface Service {
    }

    @Component("service1")
    static class Service1 implements Service {
    }

    @Primary
    @Component("service2")
    static class Service2 implements Service {
    }

    @Component("service3")
    static class Service3 implements Service {
    }
}
