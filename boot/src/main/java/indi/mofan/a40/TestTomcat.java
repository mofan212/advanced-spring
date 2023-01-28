package indi.mofan.a40;

import lombok.SneakyThrows;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.io.File;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;

/**
 * @author mofan
 * @date 2023/1/28 21:18
 */
public class TestTomcat {
    @SneakyThrows
    public static void main(String[] args) {
        // 1. 创建 Tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("tomcat");
        // 2. 创建项目文件夹，即 docBase 文件夹
        File docBase = Files.createTempDirectory("boot.").toFile();
        docBase.deleteOnExit();
        // 3. 创建 tomcat 项目，在 tomcat 中称为 Context
        Context context = tomcat.addContext("", docBase.getAbsolutePath());

        WebApplicationContext springContext = getApplicationContext();

        // 4. 编程添加 Servlet
        context.addServletContainerInitializer((set, servletContext) -> {
            HelloServlet servlet = new HelloServlet();
            // 还要设置访问 Servlet 的路径
            servletContext.addServlet("hello", servlet).addMapping("/hello");

//            DispatcherServlet dispatcherServlet = springContext.getBean(DispatcherServlet.class);
//            servletContext.addServlet("dispatcherServlet", dispatcherServlet).addMapping("/");

            // Spring 容器中可能存在多个 Servlet
            for (ServletRegistrationBean registrationBean : springContext.getBeansOfType(ServletRegistrationBean.class).values()) {
                registrationBean.onStartup(servletContext);
            }
        }, Collections.emptySet());
        // 5. 启动 tomcat
        tomcat.start();
        // 6. 创建连接器，设置监听端口
        Connector connector = new Connector(new Http11Nio2Protocol());
        connector.setPort(8080);
        tomcat.setConnector(connector);
    }

    public static WebApplicationContext getApplicationContext() {
        // 使用不支持内嵌 Tomcat 的 Spring 容器
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(Config.class);
        context.refresh();
        return context;
    }

    @Configuration
    static class Config {
        @Bean
        public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet) {
            return new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        }

        @Bean
        public DispatcherServlet dispatcherServlet(WebApplicationContext applicationContext) {
            /*
             * 必须为 DispatcherServlet 提供 AnnotationConfigWebApplicationContext，
             * 否则会选择 XmlWebApplicationContext 实现
             */
            return new DispatcherServlet(applicationContext);
        }

        @Bean
        public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
            RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
            handlerAdapter.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
            return handlerAdapter;
        }

        @RestController
        static class MyController {
            @GetMapping("hello2")
            public Map<String,Object> hello() {
                return Collections.singletonMap("hello2", "hello2, spring!");
            }
        }
    }
}
