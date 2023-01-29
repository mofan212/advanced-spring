package indi.mofan.a44;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Arrays;

/**
 * @author mofan
 * @date 2023/1/29 22:52
 */
public class A44 {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 组件扫描核心类
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);
        scanner.scan(A44.class.getPackage().getName());

        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);
    }
}
