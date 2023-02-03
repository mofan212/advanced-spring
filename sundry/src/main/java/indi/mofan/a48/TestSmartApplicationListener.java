package indi.mofan.a48;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2023/2/3 10:07
 */
@Slf4j
@Configuration
public class TestSmartApplicationListener {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(TestSmartApplicationListener.class);
        context.getBean(MyService.class).doBusiness();

        context.close();
    }

    static class MyEvent extends ApplicationEvent {
        private static final long serialVersionUID = 6781310159442033314L;

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
    static class SmsService1 {
        @Order(1)
        @EventListener
        public void listener(MyEvent myEvent) {
            log.debug("发送短信-1");
        }
    }

    @Slf4j
    @Component
    static class EmailService1 {
        @Order(2)
        @EventListener
        public void listener(MyEvent myEvent) {
            log.debug("发送邮件-1");
        }
    }

    @Slf4j
    @Component
    static class SmsService2 implements ApplicationListener<MyEvent>, Ordered {

        @Override
        public int getOrder() {
            return 3;
        }

        @Override
        public void onApplicationEvent(MyEvent event) {
            log.debug("发送短信-2");
        }
    }

    @Slf4j
    @Component
    static class EmailService2 implements SmartApplicationListener {

        @Override
        public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
            return MyEvent.class.equals(eventType);
        }

        @Override
        public int getOrder() {
            return 4;
        }

        @Override
        public void onApplicationEvent(ApplicationEvent event) {
            log.debug("发送邮件-2");
        }
    }
}
