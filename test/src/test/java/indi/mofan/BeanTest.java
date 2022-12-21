package indi.mofan;

import indi.mofan.event.MyEvent;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

/**
 * @author mofan
 * @date 2022/12/18 16:24
 */
@SpringBootTest(classes = TestApplication.class)
public class BeanTest {
    @Autowired
    private ConfigurableApplicationContext context;

    @Test
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void testSingletonObjects() {
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjects.setAccessible(true);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(beanFactory);
        long componentCount = map.entrySet().stream()
                .filter(i -> i.getKey().startsWith("component"))
                .count();
        Assertions.assertEquals(2L, componentCount);
    }

    @Test
    public void testMessageSource() {
        String en = context.getMessage("thanks", null, Locale.ENGLISH);
        Assertions.assertEquals("Thank you", en);

        String zhCN = context.getMessage("thanks", null, Locale.SIMPLIFIED_CHINESE);
        Assertions.assertEquals("谢谢", zhCN);

        String zhTW = context.getMessage("thanks", null, Locale.TRADITIONAL_CHINESE);
        Assertions.assertEquals("謝謝", zhTW);
    }

    @Test
    @SneakyThrows
    public void testResourcePatternResolver() {
        Resource[] resources = context.getResources("classpath:application.properties");
        Assertions.assertTrue(resources.length > 0);

        // 使用 classpath* 可以加载 jar 里类路径下的 resource
        resources = context.getResources("classpath*:META-INF/spring.factories");
        Assertions.assertTrue(resources.length > 0);
    }

    @Test
    public void testEnvironmentCapable() {
        // 读取系统环境变量
        String javaHome = context.getEnvironment().getProperty("java_home");
        Assertions.assertTrue(StringUtils.isNotEmpty(javaHome));

        // 读取配置文件信息
        String name = context.getEnvironment().getProperty("properties.name");
        Assertions.assertEquals("mofan", name);
    }
    
    @Test
    public void testApplicationEventPublisher() {
        context.publishEvent(new MyEvent("mofan"));
    }

}
