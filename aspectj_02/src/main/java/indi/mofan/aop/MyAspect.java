package indi.mofan.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author mofan
 * @date 2023/1/9 22:26
 */
@Slf4j
@Aspect
public class MyAspect {

    @Before("execution(* indi.mofan.service.MyService.*())")
    public void before() {
        log.info("before()");
    }
}
