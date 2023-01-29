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
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.Optional;

/**
 * @author mofan
 * @date 2023/1/29 16:50
 */
public class A42_2 {
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

    static class MyCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnClass.class.getName());
            Optional<Map<String, Object>> optional = Optional.ofNullable(attributes);
            String className = optional.map(i -> String.valueOf(i.get("className"))).orElse("");
            boolean exists = optional.map(i -> i.get("exists"))
                    .map(String::valueOf)
                    .map(Boolean::parseBoolean).orElse(false);
            boolean present = ClassUtils.isPresent(className, null);
            return exists == present;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Conditional(MyCondition.class)
    private @interface ConditionalOnClass {
        /**
         * true 判断存在 false 判断不存在
         */
        boolean exists();

        /**
         * 要判断的类名
         */
        String className();
    }

    @Configuration
    @ConditionalOnClass(className = "com.alibaba.druid.pool.DruidDataSource", exists = true)
    static class AutoConfiguration1 {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }
    }

    @Configuration
    @ConditionalOnClass(className = "com.alibaba.druid.pool.DruidDataSource", exists = false)
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
