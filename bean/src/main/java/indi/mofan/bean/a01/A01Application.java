package indi.mofan.bean.a01;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

/**
 * @author mofan
 * @date 2023/1/27 13:20
 */
@Slf4j
@SpringBootApplication
public class A01Application {
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A01Application.class, args);
        System.out.println(context);

        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjects.setAccessible(true);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(beanFactory);
        map.entrySet().stream().filter(e -> e.getKey().startsWith("component"))
                .forEach(e -> System.out.println(e.getKey() + "=" + e.getValue()));

        System.out.println(context.getMessage("thanks", null, Locale.ENGLISH));
        System.out.println(context.getMessage("thanks", null, Locale.SIMPLIFIED_CHINESE));
        System.out.println(context.getMessage("thanks", null, Locale.TRADITIONAL_CHINESE));

        Resource[] resources = context.getResources("classpath:application.properties");
        Assert.isTrue(resources.length > 0, "加载类路径下的 application.properties 文件失败");

        // 使用 classpath* 可以加载 jar 里类路径下的 resource
        resources = context.getResources("classpath*:META-INF/spring.factories");
        Assert.isTrue(resources.length > 0, "加载类路径下的 META-INF/spring.factories 文件失败");

        System.out.println(context.getEnvironment().getProperty("java_home"));
        System.out.println(context.getEnvironment().getProperty("properties.name"));

        context.getBean(Component1.class).register();

        context.close();
    }
}
