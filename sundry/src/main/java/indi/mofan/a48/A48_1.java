package indi.mofan.a48;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2023/2/2 21:08
 */
@Slf4j
@Configuration
public class A48_1 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A48_1.class);
        context.getBean(MyService.class).doBusiness();
        context.close();
    }

    static class MyEvent extends ApplicationEvent {
        private static final long serialVersionUID = -1541319641201302606L;

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
}
