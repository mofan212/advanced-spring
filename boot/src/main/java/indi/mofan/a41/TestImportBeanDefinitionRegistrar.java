package indi.mofan.a41;

import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;

/**
 * @author mofan
 * @date 2023/2/1 10:43
 */
public class TestImportBeanDefinitionRegistrar {
    public static void main(String[] args) {
        // AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        GenericApplicationContext context = new GenericApplicationContext();
        // AnnotationConfigUtils.registerAnnotationConfigProcessors(context.getDefaultListableBeanFactory());

        context.registerBean(ConfigurationClassPostProcessor.class);
        context.registerBean("config", Config.class);
        context.refresh();

        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
        System.out.println(context.getBean(User.class));
    }

    @Configuration
    @Import({MyImportBeanDefinitionRegistrar.class})
    static class Config {

    }

    static class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            // 构建 BeanDefinition
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(User.class)
                    .addPropertyValue("name", "mofan")
                    .addPropertyValue("age", 20)
                    .getBeanDefinition();
            // 注册构建好的 BeanDefinition
            registry.registerBeanDefinition("user", beanDefinition);
        }
    }

    @Setter
    @ToString
    static class User {
        private String name;
        private int age;
    }
}
