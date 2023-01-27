package indi.mofan.a39;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author mofan
 * @date 2023/1/27 20:10
 */
@Configuration
public class A39_1 {
    @SneakyThrows
    public static void main(String[] args) {
        System.out.println("1. 演示获取 Bean Definition 源");
        SpringApplication spring = new SpringApplication(A39_1.class);
        spring.setSources(Collections.singleton("classpath:b01.xml"));
        System.out.println("2. 演示推断应用类型");
        Method deduceFromClasspath = WebApplicationType.class.getDeclaredMethod("deduceFromClasspath");
        deduceFromClasspath.setAccessible(true);
        System.out.println("\t应用类型为: " + deduceFromClasspath.invoke(null));
        System.out.println("3. 演示 ApplicationContext 初始化容器");
        spring.addInitializers(applicationContext -> {
            if (applicationContext instanceof GenericApplicationContext) {
                GenericApplicationContext context = (GenericApplicationContext) applicationContext;
                context.registerBean("bean3", Bean3.class);
            }
        });
        System.out.println("4. 演示监听器与事件");
        spring.addListeners(event -> System.out.println("\t事件类型为: " + event.getClass()));
        System.out.println("5. 演示主类推断");
        Method deduceMainApplicationClass = SpringApplication.class.getDeclaredMethod("deduceMainApplicationClass");
        deduceMainApplicationClass.setAccessible(true);
        System.out.println("\t主类是: " + deduceMainApplicationClass.invoke(spring));

        // 创建并初始化 Spring 容器
        ConfigurableApplicationContext context = spring.run(args);
        Arrays.stream(context.getBeanDefinitionNames()).forEach(i -> {
            System.out.println("name: " + i +
                    " 来源: " + context.getBeanFactory().getBeanDefinition(i).getResourceDescription());
        });
        context.close();
    }

    static class Bean1 {
    }

    static class Bean2 {
    }

    static class Bean3 {
    }

    @Bean
    public Bean2 bean2() {
        return new Bean2();
    }

    @Bean
    public TomcatServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }
}
