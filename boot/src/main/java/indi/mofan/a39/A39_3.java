package indi.mofan.a39;

import lombok.SneakyThrows;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;

/**
 * @author mofan
 * @date 2023/1/28 11:18
 */
public class A39_3 {
    @SneakyThrows
    @SuppressWarnings("all")
    public static void main(String[] args) { // 运行时添加程序参数 --server.port=8080 debug
        SpringApplication app = new SpringApplication();
        app.addInitializers(applicationContext -> System.out.println("执行初始化器增强..."));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 2. 封装启动 args");
        DefaultApplicationArguments arguments = new DefaultApplicationArguments(args);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 8. 创建容器");
        GenericApplicationContext context = createApplicationContext(WebApplicationType.SERVLET);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 9. 准备容器");
        for (ApplicationContextInitializer initializer : app.getInitializers()) {
            initializer.initialize(context);
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 10. 加载 bean 定义");
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        AnnotatedBeanDefinitionReader reader1 = new AnnotatedBeanDefinitionReader(beanFactory);
        XmlBeanDefinitionReader reader2 = new XmlBeanDefinitionReader(beanFactory);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);

        reader1.register(Config.class);
        reader2.loadBeanDefinitions(new ClassPathResource("b03.xml"));
        scanner.scan("indi.mofan.a39.sub");

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 11. refresh 容器");
        context.refresh();

        for (String name : context.getBeanDefinitionNames()) {
            System.out.println("name: " + name + " 来源: " + beanFactory.getBeanDefinition(name).getResourceDescription());
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 12. 执行 runner");
        for (CommandLineRunner runner : context.getBeansOfType(CommandLineRunner.class).values()) {
            runner.run(args);
        }

        for (ApplicationRunner runner : context.getBeansOfType(ApplicationRunner.class).values()) {
            runner.run(arguments);
        }
    }

    private static GenericApplicationContext createApplicationContext(WebApplicationType type) {
        GenericApplicationContext context = null;
        switch (type) {
            case SERVLET:
                context = new AnnotationConfigServletWebServerApplicationContext();
                break;
            case REACTIVE:
                context = new AnnotationConfigReactiveWebServerApplicationContext();
                break;
            case NONE:
                context = new AnnotationConfigApplicationContext();
                break;
        }
        return context;
    }

    static class Bean4 {

    }

    static class Bean5 {

    }

    static class Bean6 {

    }

    @Configuration
    static class Config {
        @Bean
        public Bean5 bean5() {
            return new Bean5();
        }

        @Bean
        public ServletWebServerFactory servletWebServerFactory() {
            return new TomcatServletWebServerFactory();
        }

        @Bean
        public CommandLineRunner commandLineRunner() {
            return args -> System.out.println("commandLineRunner()..." + Arrays.toString(args));
        }

        @Bean
        public ApplicationRunner applicationRunner() {
            return args -> {
                // 获取原始参数
                System.out.println("applicationRunner()..."
                        + Arrays.toString(args.getSourceArgs()));
                // 获取选项名称，参数中带有 `--` 的参数
                System.out.println(args.getOptionNames());
                // 获取选项值
                System.out.println(args.getOptionValues("server.port"));
                // 获取非选项参数
                System.out.println(args.getNonOptionArgs());
            };
        }
    }
}
