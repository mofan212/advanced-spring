package indi.mofan.a48;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.EventListener;

/**
 * @author mofan
 * @date 2023/2/3 14:11
 */
public class EventPropagationTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext parent = new AnnotationConfigApplicationContext();
        parent.registerBean(MyListener.class);
        parent.refresh();

        AnnotationConfigApplicationContext child = new AnnotationConfigApplicationContext();
        child.setParent(parent);
        child.refresh();

        // 通过子容器发布事件，能够在父容器监听到
        child.publishEvent(new MyEvent("子容器发送的事件..."));
    }

    static class MyEvent extends ApplicationEvent {
        private static final long serialVersionUID = -7002403082731659626L;

        public MyEvent(String source) {
            super(source);
        }
    }

    @Slf4j
    static class MyListener {
        @EventListener
        public void listener(MyEvent myEvent) {
            log.debug(String.valueOf(myEvent.getSource()));
        }
    }
}
