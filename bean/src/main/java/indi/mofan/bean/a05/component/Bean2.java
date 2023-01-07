package indi.mofan.bean.a05.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2023/1/7 18:54
 */
@Slf4j
@Component
public class Bean2 {
    public Bean2() {
        log.info("我被 Spring 管理啦");
    }
}
