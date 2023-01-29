package indi.mofan.a41;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author mofan
 * @date 2023/1/29 16:06
 */
public class TestMvcAuto {
    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext();
        context.registerBean(Config.class);
        context.refresh();
        for (String name : context.getBeanDefinitionNames()) {
            String source = context.getBeanDefinition(name).getResourceDescription();
            if (source != null) {
                System.out.println(name + " 来源:" + source);
            }
        }
        context.close();
    }

    @Configuration
    @Import(MyImportSelector.class)
    static class Config {

    }

    static class MyImportSelector implements DeferredImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{
                    // 配置内嵌 Tomcat 服务器工厂
                    ServletWebServerFactoryAutoConfiguration.class.getName(),
                    // 配置 DispatcherServlet
                    DispatcherServletAutoConfiguration.class.getName(),
                    // 配置 WebMVC 各种组件
                    WebMvcAutoConfiguration.class.getName(),
                    // 配置 MVC 的错误处理
                    ErrorMvcAutoConfiguration.class.getName()
            };
        }
    }
}
