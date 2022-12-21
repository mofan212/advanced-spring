package indi.mofan.listener;

import indi.mofan.event.MyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/12/19 21:58
 */
@Slf4j
@Component
public class MyListener {

    @EventListener
    public void messageListener(MyEvent event) {
        log.info("{}", event.getName());
    }
}
