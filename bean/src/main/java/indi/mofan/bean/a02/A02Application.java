package indi.mofan.bean.a02;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.Controller;

import java.util.Arrays;

/**
 * @author mofan
 * @date 2022/12/23 23:32
 */
@Slf4j
public class A02Application {
    public static void main(String[] args) {
//        testClassPathXmlApplicationContext();
//        testFileSystemXmlApplicationContext();
//        testXmlBeanDefinitionReader();
//        testAnnotationConfigApplicationContext();
        testAnnotationConfigServletWebServerApplicationContext();
    }

    /**
     * 较为经典的容器，基于 classpath 下的 xml 格式的配置文件来创建
     */
    private static void testClassPathXmlApplicationContext() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("b01.xml");
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    /**
     * 基于磁盘路径下 xml 格式的配置文件来创建
     */
    private static void testFileSystemXmlApplicationContext() {
        // 使用相对路径时，以模块为起点（IDEA 中需要设置 Working directory），也支持绝对路径
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("bean\\src\\main\\resources\\b01.xml");
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    private static void testXmlBeanDefinitionReader() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        System.out.println("读取之前...");
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);
        System.out.println("读取之后...");
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
//        reader.loadBeanDefinitions(new ClassPathResource("b01.xml"));
        reader.loadBeanDefinitions(new FileSystemResource("bean\\src\\main\\resources\\b01.xml"));
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);
    }

    /**
     * 基于 Java 配置类来创建
     */
    private static void testAnnotationConfigApplicationContext() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    /**
     * 基于 Java 配置类来创建，用于 web 环境
     */
    private static void testAnnotationConfigServletWebServerApplicationContext() {
        AnnotationConfigServletWebServerApplicationContext context =
                new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
    }

    @Configuration
    static class WebConfig {
        @Bean
        public ServletWebServerFactory servletWebServerFactory() {
            // 提供内嵌的 Web 容器
            return new TomcatServletWebServerFactory();
        }

        @Bean
        public DispatcherServlet dispatcherServlet() {
            // 添加前控制器
            return new DispatcherServlet();
        }

        @Bean
        public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet) {
            // 将 DispatcherServlet 注册到 Tomcat 服务器
            return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        }

        // 如果 bean 以 '/' 开头，将 '/' 后的 bean 的名称作为访问路径
        @Bean("/hello")
        public Controller controller1() {
            // 添加控制器，用于展示
            return (request, response) -> {
                response.getWriter().println("hello");
                return null;
            };
        }
    }

    @Configuration
    static class Config {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2(Bean1 bean1) {
            Bean2 bean2 = new Bean2();
            bean2.setBean1(bean1);
            return bean2;
        }
    }


    static class Bean1 {

    }

    static class Bean2 {
        @Getter
        @Setter
        private Bean1 bean1;
    }
}
