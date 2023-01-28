package org.springframework.boot;

import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessorApplicationListener;
import org.springframework.boot.env.RandomValuePropertySourceEnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.boot.logging.DeferredLogs;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * @author mofan
 * @date 2023/1/28 13:06
 */
public class Step5 {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication();
        app.addListeners(new EnvironmentPostProcessorApplicationListener());
        ApplicationEnvironment env = new ApplicationEnvironment();

        List<String> names = SpringFactoriesLoader.loadFactoryNames(EnvironmentPostProcessor.class, Step5.class.getClassLoader());
        names.forEach(System.out::println);

        EventPublishingRunListener publisher = new EventPublishingRunListener(app, args);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 增强前");
        env.getPropertySources().forEach(System.out::println);
        publisher.environmentPrepared(new DefaultBootstrapContext(), env);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 增强后");
        env.getPropertySources().forEach(System.out::println);
    }

    public void test(SpringApplication app, ApplicationEnvironment env) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 增强前");
        env.getPropertySources().forEach(System.out::println);
        ConfigDataEnvironmentPostProcessor processor1 = new ConfigDataEnvironmentPostProcessor(
                new DeferredLogs(), new DefaultBootstrapContext()
        );
        processor1.postProcessEnvironment(env, app);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 增强后");
        env.getPropertySources().forEach(System.out::println);
        System.out.println(env.getProperty("author.name"));

        RandomValuePropertySourceEnvironmentPostProcessor processor2 =
                new RandomValuePropertySourceEnvironmentPostProcessor(new DeferredLog());
        processor2.postProcessEnvironment(env, app);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> 再次增强后");
        env.getPropertySources().forEach(System.out::println);
        System.out.println(env.getProperty("random.string"));
        System.out.println(env.getProperty("random.int"));
        System.out.println(env.getProperty("random.uuid"));
    }
}
