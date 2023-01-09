package indi.mofan.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mofan
 * @date 2023/1/9 21:18
 *
 * 注意此切面并未被 Spring 管理
 */
@Aspect
public class MyAspect {
    private static final Logger log = LoggerFactory.getLogger(MyAspect.class);

    @Before("execution(* indi.mofan.service.MyService.foo())")
    public void before() {
        log.info("before()");
    }
}
