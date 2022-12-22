package indi.mofan.bean.a02;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author mofan
 * @date 2022/12/20 20:57
 */
public class TestBeanFactory {

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // bean 的定义（class，scope，初始化，销毁）
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class)
                .setScope("singleton")
                .getBeanDefinition();
        beanFactory.registerBeanDefinition("config", beanDefinition);

        // 只有 config
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);
        System.out.println("---------------------------------------------");

        // 给 BeanFactory 添加一些常用的后置处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);

        // 使用后置处理器
        System.out.println("---------------------------------------------");
        // BeanFactoryPostProcessor 补充了一些 Bean 的定义
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().forEach(i -> i.postProcessBeanFactory(beanFactory));
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);

        System.out.println("---------------------------------------------");
        // 有 Bean1，但是 Bean2 没有注入到 Bean1 里
//        System.out.println(beanFactory.getBean(Bean1.class).getBean2());
        // Bean 后置处理器，对 Bean 的生命周期的各个阶段提供拓展，例如 @AutoWired...
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().stream()
                .sorted(Objects.requireNonNull(beanFactory.getDependencyComparator()))
                .forEach(i -> {
            System.out.println(">>>> " + i);
            beanFactory.addBeanPostProcessor(i);
        });
        // 预先初始化单例对象（完成依赖注入和初始化流程）
        beanFactory.preInstantiateSingletons();
        System.out.println("---------------------------------------------");
        System.out.println(beanFactory.getBean(Bean1.class).getBean2());
        System.out.println(beanFactory.getBean(Bean1.class).getInter());
    }

    @Configuration
    static class Config {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }

        @Bean
        public Bean3 bean3() {
            return new Bean3();
        }

        @Bean
        public Bean4 bean4() {
            return new Bean4();
        }
    }

    interface Inter {

    }

    @Slf4j
    static class Bean3 implements Inter {
        public Bean3() {
            log.debug("构造 Bean3()");
        }
    }

    @Slf4j
    static class Bean4 implements Inter {
        public Bean4() {
            log.debug("构造 Bean4()");
        }
    }

    @Slf4j
    static class Bean1 {
        public Bean1() {
            log.debug("构造 Bean1()");
        }

        @Autowired
        private Bean2 bean2;

        public Bean2 getBean2() {
            return bean2;
        }

        @Autowired
        @Resource(name = "bean4")
        private Inter bean3;

        private Inter getInter() {
            return bean3;
        }
    }

    @Slf4j
    static class Bean2 {
        public Bean2() {
            log.debug("构造 Bean2()");
        }
    }
}
