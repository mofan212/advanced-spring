package org.springframework.boot;

import lombok.SneakyThrows;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

/**
 * @author mofan
 * @date 2023/1/28 12:12
 */
public class Step3 {
    @SneakyThrows
    public static void main(String[] args) {
        ApplicationEnvironment env = new ApplicationEnvironment();
        env.getPropertySources().addLast(new ResourcePropertySource(new ClassPathResource("application.properties")));
        env.getPropertySources().addFirst(new SimpleCommandLinePropertySource(args));
        env.getPropertySources().forEach(System.out::println);

//        System.out.println(env.getProperty("JAVA_HOME"));
        System.out.println(env.getProperty("author.name"));
    }
}
