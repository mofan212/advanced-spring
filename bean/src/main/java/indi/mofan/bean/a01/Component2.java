package indi.mofan.bean.a01;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/12/18 16:24
 */
@Slf4j
@Component
public class Component2 {
    @EventListener
    public void aaa(UserRegisteredEvent event) {
        log.debug("{}", event);
        log.debug("发送短信");
    }
}
