package org.springframework.boot;

import lombok.SneakyThrows;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

/**
 * @author mofan
 * @date 2023/1/28 12:47
 */
public class Step4 {
    @SneakyThrows
    public static void main(String[] args) {
        ApplicationEnvironment env = new ApplicationEnvironment();
        env.getPropertySources().addLast(
                new ResourcePropertySource("step4", new ClassPathResource("step4.properties"))
        );
        ConfigurationPropertySources.attach(env);
        env.getPropertySources().forEach(System.out::println);
        System.out.println(env.getProperty("user.first-name"));
        System.out.println(env.getProperty("user.middle-name"));
        System.out.println(env.getProperty("user.last-name"));
    }
}
