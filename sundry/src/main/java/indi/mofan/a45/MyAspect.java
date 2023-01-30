package indi.mofan.a45;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2023/1/30 22:07
 */
@Aspect
@Component
public class MyAspect {
    /**
     * 对 Bean1 中所有的方法进行匹配
     */
    @Before("execution(* indi.mofan.a45.Bean1.*(..))")
    public void before() {
        System.out.println("before");
    }
}
