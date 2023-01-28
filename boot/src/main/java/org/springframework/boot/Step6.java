package org.springframework.boot;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePropertySource;

import java.lang.reflect.Field;

/**
 * @author mofan
 * @date 2023/1/28 17:18
 */
public class Step6 {
    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication();
        ApplicationEnvironment env = new ApplicationEnvironment();
        env.getPropertySources().addLast(
                new ResourcePropertySource("step4", new ClassPathResource("step4.properties"))
        );
        env.getPropertySources().addLast(
                new ResourcePropertySource("step6", new ClassPathResource("step6.properties"))
        );

        User user = Binder.get(env).bind("user", User.class).get();
        System.out.println(user);

        User existUser = new User();
        Binder.get(env).bind("user", Bindable.ofInstance(existUser));
        System.out.println(existUser);

        Class<? extends SpringApplication> clazz = app.getClass();
        Field bannerMode = clazz.getDeclaredField("bannerMode");
        bannerMode.setAccessible(true);
        Field lazyInitialization = clazz.getDeclaredField("lazyInitialization");
        lazyInitialization.setAccessible(true);
        System.out.println(bannerMode.get(app));
        System.out.println(lazyInitialization.get(app));
        Binder.get(env).bind("spring.main", Bindable.ofInstance(app));
        System.out.println(bannerMode.get(app));
        System.out.println(lazyInitialization.get(app));
    }

    @Getter
    @Setter
    @ToString
    static class User {
        private String firstName;
        private String middleName;
        private String lastName;
    }
}
