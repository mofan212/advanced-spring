package indi.mofan.a42;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.io.IOException;

/**
 * @author mofan
 * @date 2023/1/29 16:32
 */
public class A42 {
    public static void main(String[] args) throws IOException {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.refresh();

        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }
    }

    @Configuration
    @Import(MyImportSelector.class)
    static class Config {
    }

    static class MyImportSelector implements DeferredImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{AutoConfiguration1.class.getName(), AutoConfiguration2.class.getName()};
        }
    }

    static class MyCondition1 implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            // 存在 Druid 依赖
            return ClassUtils.isPresent("com.alibaba.druid.pool.DruidDataSource", null);
        }
    }

    static class MyCondition2 implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            // 不存在 Druid 依赖
            return !ClassUtils.isPresent("com.alibaba.druid.pool.DruidDataSource", null);
        }
    }

    /**
     * 模拟第三方的配置类
     */
    @Configuration
    @Conditional(MyCondition1.class)
    static class AutoConfiguration1 {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }
    }

    /**
     * 模拟第三方的配置类
     */
    @Configuration
    @Conditional(MyCondition2.class)
    static class AutoConfiguration2 {
        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }

    static class Bean1 {
    }

    static class Bean2 {
    }
}
