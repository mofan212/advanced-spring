package indi.mofan.a48;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/**
 * @author mofan
 * @date 2023/2/2 21:45
 */
@Configuration
public class A48_3 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A48_3.class);
        context.getBean(MyService.class).doBusiness();
        context.close();
    }

    @Bean
    public SmartInitializingSingleton smartInitializingSingleton(ConfigurableApplicationContext context) {
        // Spring 中所有单例 Bean 初始化完成后调用此处理器
        return () -> {
            for (String name : context.getBeanDefinitionNames()) {
                Object bean = context.getBean(name);
                for (Method method : bean.getClass().getMethods()) {
                    if (method.isAnnotationPresent(MyListener.class)) {
                        // 添加事件监听器
                        context.addApplicationListener((event) -> {
                            // System.out.println(event);
                            // 监听器方法的事件类型
                            Class<?> eventType = method.getParameterTypes()[0];
                            if (eventType.isAssignableFrom(event.getClass())) {
                                try {
                                    method.invoke(bean, event);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                }
            }
        };
    }

    @Slf4j
    @Component
    static class MyService {
        @Autowired
        private ApplicationEventPublisher publisher;

        public void doBusiness() {
            log.debug("主线业务");
            publisher.publishEvent(new MyEvent("MyService.doBusiness()"));
        }
    }

    @Slf4j
    @Component
    static class SmsService {
        @MyListener
        public void listener(MyEvent myEvent) {
            log.debug("发送短信");
        }
    }

    @Slf4j
    @Component
    static class EmailService {
        @MyListener
        public void listener(MyEvent myEvent) {
            log.debug("发送邮件");
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface MyListener {
    }

    static class MyEvent extends ApplicationEvent {
        private static final long serialVersionUID = -6388410688691384516L;

        public MyEvent(Object source) {
            super(source);
        }
    }
}
