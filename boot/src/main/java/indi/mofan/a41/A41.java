package indi.mofan.a41;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mofan
 * @date 2023/1/28 22:05
 */
public class A41 {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        // 默认是 true，SpringBoot 修改为 false，使得无法进行覆盖
        context.getDefaultListableBeanFactory().setAllowBeanDefinitionOverriding(false);
        context.registerBean("config", Config.class);
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.refresh();

        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(context.getBean(Bean1.class));
    }

    @Configuration
//    @Import({AutoConfiguration1.class, AutoConfiguration2.class})
    @Import(MyImportSelector.class)
    static class Config {
        @Bean
        public Bean1 bean1() {
            return new Bean1("本项目");
        }
    }

    static class MyImportSelector implements DeferredImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
//            return new String[]{AutoConfiguration1.class.getName(), AutoConfiguration2.class.getName()};
            List<String> names = new ArrayList<>(SpringFactoriesLoader.loadFactoryNames(MyImportSelector.class, null));
            // 读取新版自动装配文件
            ImportCandidates.load(MyAutoConfiguration.class, null).forEach(names::add);
            return names.toArray(new String[0]);
        }
    }

    /**
     * 模拟第三方配置类
     */
    static class AutoConfiguration1 {
        @Bean
        @ConditionalOnMissingBean
        public Bean1 bean1() {
            return new Bean1("第三方");
        }
    }

    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class Bean1 {
        private String name;
    }

    /**
     * 模拟第三方配置类
     */
    static class AutoConfiguration2 {
        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }

    static class Bean2 {

    }

    static class Bean3 {

    }
}
