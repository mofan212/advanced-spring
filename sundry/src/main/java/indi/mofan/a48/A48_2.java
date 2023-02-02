package indi.mofan.a48;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2023/2/2 21:34
 */
@Configuration
public class A48_2 {

    @SneakyThrows
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A48_2.class);
        context.getBean(MyService.class).doBusiness();

        // 睡两秒，消息监听成功后才关闭容器
        TimeUnit.SECONDS.sleep(2);
        context.close();
    }

    static class MyEvent extends ApplicationEvent {
        private static final long serialVersionUID = 3716640016383074610L;

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
    static class SmsService {
        @EventListener
        public void listener(MyEvent myEvent) {
            log.debug("发送短信");
        }
    }

    @Slf4j
    @Component
    static class EmailService {
        @EventListener
        public void listener(MyEvent myEvent) {
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

    /**
     * 方法名称必须是 applicationEventMulticaster，才能对 Bean 进行覆盖
     */
    @Bean
    public SimpleApplicationEventMulticaster applicationEventMulticaster(ThreadPoolTaskExecutor executor) {
        SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        // 使用线程池异步发送事件
        multicaster.setTaskExecutor(executor);
        return multicaster;
    }
}
