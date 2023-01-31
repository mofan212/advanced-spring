package indi.mofan.a46;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author mofan
 * @date 2023/1/31 21:33
 */
@Configuration
@SuppressWarnings("all")
public class A46 {
    @SneakyThrows
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(A46.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();

        ContextAnnotationAutowireCandidateResolver resolver =
                new ContextAnnotationAutowireCandidateResolver();
        resolver.setBeanFactory(beanFactory);

//        test1(context, resolver);
//        test2(context, resolver);
        test3(context, resolver, Bean2.class.getDeclaredField("bean3"));
        System.out.println(">>>>>>>>>>>>>>>>>>>");
        test3(context, resolver, Bean4.class.getDeclaredField("value"));
    }

    private static void test3(AnnotationConfigApplicationContext context,
                              ContextAnnotationAutowireCandidateResolver resolver,
                              Field field) {
        DependencyDescriptor dd1 = new DependencyDescriptor(field, false);
        // 获取 @Value 的内容
        String value = resolver.getSuggestedValue(dd1).toString();
        System.out.println("@Value 的 value 属性值: " + value);

        // 解析 ${}
        value = context.getEnvironment().resolvePlaceholders(value);
        System.out.println("解析得到的值: " + value);
        System.out.println("解析得到的值的类型: " + value.getClass());

        // 解析 #{}
        Object bean3 = context.getBeanFactory()
                .getBeanExpressionResolver()
                .evaluate(value, new BeanExpressionContext(context.getBeanFactory(), null));

        // 类型转换
        Object result = context.getBeanFactory()
                .getTypeConverter()
                .convertIfNecessary(bean3, dd1.getDependencyType());
        System.out.println("转换后的类型: " + result.getClass());
    }

    @SneakyThrows
    private static void test2(AnnotationConfigApplicationContext context,
                              ContextAnnotationAutowireCandidateResolver resolver) {
        DependencyDescriptor dd1 =
                new DependencyDescriptor(Bean1.class.getDeclaredField("age"), false);
        // 获取 @Value 的内容
        String value = resolver.getSuggestedValue(dd1).toString();
        System.out.println("@Value 的 value 属性值: " + value);

        // 解析 ${}
        value = context.getEnvironment().resolvePlaceholders(value);
        System.out.println("解析得到的值: " + value);
        System.out.println("解析得到的值的类型: " + value.getClass());
        // 转成字段的类型
        Object age = context.getBeanFactory()
                .getTypeConverter()
                .convertIfNecessary(value, dd1.getDependencyType());
        System.out.println("转换后的类型: " + age.getClass());
    }

    @SneakyThrows
    private static void test1(AnnotationConfigApplicationContext context,
                              ContextAnnotationAutowireCandidateResolver resolver) {
        DependencyDescriptor dd1 = new DependencyDescriptor(Bean1.class.getDeclaredField("home"), false);
        // 获取 @Value 的内容
        String value = resolver.getSuggestedValue(dd1).toString();
        System.out.println(value);

        // 解析 ${}
        value = context.getEnvironment().resolvePlaceholders(value);
        System.out.println(value);
    }

    public class Bean1 {
        @Value("${JAVA_HOME}")
        private String home;
        @Value("18")
        private int age;
    }

    public class Bean2 {
        @Value("#{@bean3}")
        private Bean3 bean3;
    }

    @Component("bean3")
    public class Bean3 {
    }

    static class Bean4 {
        @Value("#{'hello, ' + '${JAVA_HOME}'}")
        private String value;
    }
}
