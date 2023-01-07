package indi.mofan.bean.a05.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

/**
 * @author mofan
 * @date 2023/1/7 18:54
 */
@Slf4j
@Controller
public class Bean3 {
    public Bean3() {
        log.info("我被 Spring 管理啦");
    }
}
