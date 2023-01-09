package indi.mofan.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author mofan
 * @date 2023/1/9 22:22
 */
@Slf4j
@Service
public class MyService {
    final public void foo() {
        log.info("foo()");
        bar();
    }

    public void bar(){
        log.info("bar()");
    }
}
