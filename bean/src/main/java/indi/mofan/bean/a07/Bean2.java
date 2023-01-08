package indi.mofan.bean.a07;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

import javax.annotation.PreDestroy;

/**
 * @author mofan
 * @date 2023/1/8 17:35
 */
@Slf4j
public class Bean2 implements DisposableBean {
    @PreDestroy
    public void destroy1() {
        log.info("销毁1");
    }

    @Override
    public void destroy() throws Exception {
        log.info("销毁2");
    }

    public void destroy3() {
        log.info("销毁3");
    }
}
