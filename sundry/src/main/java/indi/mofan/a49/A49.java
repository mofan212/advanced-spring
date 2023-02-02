package indi.mofan.a49;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author mofan
 * @date 2023/2/2 23:42
 */
@Configuration
public class A49 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A49.class);
        context.getBean(MyService.class).doBusiness();
        context.close();
    }

    static class MyEvent extends ApplicationEvent {
        private static final long serialVersionUID = -1582242330022631950L;

        public MyEvent(Object source) {
            super(source);
        }
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
    static class SmsApplicationListener implements ApplicationListener<MyEvent> {
        @Override
        public void onApplicationEvent(MyEvent event) {
            log.debug("发送短信");
        }
    }

    @Slf4j
    @Component
    static class EmailApplicationListener implements ApplicationListener<MyEvent> {
        @Override
        public void onApplicationEvent(MyEvent event) {
            log.debug("发送邮件");
        }
    }

    @Bean
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        return executor;
    }

    @Bean
    @SuppressWarnings("all")
    public ApplicationEventMulticaster applicationEventMulticaster(ConfigurableApplicationContext context, ThreadPoolTaskExecutor executor) {
        return new AbstractApplicationEventMulticaster() {
            private List<GenericApplicationListener> listeners = new ArrayList<>();

            // 收集监听器
            public void addApplicationListenerBean(String name) {
                ApplicationListener listener = context.getBean(name, ApplicationListener.class);
                // System.out.println(listener);
                // 获取该监听器支持的事件类型
                ResolvableType type = ResolvableType.forClass(listener.getClass()).getInterfaces()[0].getGeneric();
                // System.out.println(type);

                // 将原始的 listener 封装为支持事件类型检查的 listener
                GenericApplicationListener genericApplicationListener = new GenericApplicationListener() {
                    /**
                     * 是否支持某种事件类型
                     * @param eventType 真实的事件类型
                     * @return 是否支持某事件类型
                     */
                    public boolean supportsEventType(ResolvableType eventType) {
                        return type.isAssignableFrom(eventType);
                    }

                    public void onApplicationEvent(ApplicationEvent event) {
                        executor.submit(() -> listener.onApplicationEvent(event));
                    }
                };

                listeners.add(genericApplicationListener);
            }

            // 发布事件
            public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {
                for (GenericApplicationListener listener : listeners) {
                    if (listener.supportsEventType(ResolvableType.forClass(event.getClass()))) {
                        listener.onApplicationEvent(event);
                    }
                }
            }
        };
    }

    abstract static class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster {

        @Override
        public void addApplicationListener(ApplicationListener<?> listener) {
        }

        @Override
        public void addApplicationListenerBean(String listenerBeanName) {
        }

        @Override
        public void removeApplicationListener(ApplicationListener<?> listener) {
        }

        @Override
        public void removeApplicationListenerBean(String listenerBeanName) {
        }

        @Override
        public void removeApplicationListeners(Predicate<ApplicationListener<?>> predicate) {
        }

        @Override
        public void removeApplicationListenerBeans(Predicate<String> predicate) {
        }

        @Override
        public void removeAllListeners() {
        }

        @Override
        public void multicastEvent(ApplicationEvent event) {
        }

        @Override
        public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {
        }
    }
}
