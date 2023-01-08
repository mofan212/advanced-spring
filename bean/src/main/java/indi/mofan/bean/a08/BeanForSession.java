package indi.mofan.bean.a08;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PreDestroy;

/**
 * @author mofan
 * @date 2023/1/8 18:05
 */
@Slf4j
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BeanForSession {
    @PreDestroy
    public void destroy() {
        log.info("destroy");
    }
}
