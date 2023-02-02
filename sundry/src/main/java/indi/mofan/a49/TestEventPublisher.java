package indi.mofan.a49;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mofan
 * @date 2023/2/2 23:45
 */
@Configuration
public class TestEventPublisher {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestEventPublisher.class);

        context.publishEvent(new Object());
        context.publishEvent("aaaa");
        context.publishEvent(new Bean1());
    }

    interface Inter {
    }

    static class Bean1 implements Inter {
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
        return new A49.AbstractApplicationEventMulticaster() {
            private List<GenericApplicationListener> listeners = new ArrayList<>();

            {
                listeners.add(new GenericApplicationListener() {
                    @Override
                    public void onApplicationEvent(ApplicationEvent event) {
                        if (event instanceof PayloadApplicationEvent) {
                            PayloadApplicationEvent<?> payloadApplicationEvent = (PayloadApplicationEvent<?>) event;
                            System.out.println(payloadApplicationEvent.getPayload());
                        }
                    }

                    @Override
                    public boolean supportsEventType(ResolvableType eventType) {
                        System.out.println(eventType);
                        // eventType --> PayloadApplicationEvent<Object>
                        // eventType --> PayloadApplicationEvent<String>
                        return (Inter.class.isAssignableFrom(eventType.getGeneric().toClass()));
                    }
                });
            }

            @Override
            public void multicastEvent(ApplicationEvent event) {
                multicastEvent(event, null);
            }

            @SuppressWarnings("all")
            @Override
            public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {
                listeners.stream().filter(applicationListener -> {
                    if (eventType == null) {
                        return false;
                    }
                    if (applicationListener instanceof GenericApplicationListener) {
                        GenericApplicationListener listener = (GenericApplicationListener) applicationListener;
                        return listener.supportsEventType(eventType);
                    }
                    return false;
                }).forEach(listener -> listener.onApplicationEvent(event));
            }
        };
    }
}
