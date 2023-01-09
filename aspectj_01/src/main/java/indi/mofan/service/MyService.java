package indi.mofan.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author mofan
 * @date 2023/1/9 21:17
 */
@Service
public class MyService {
    private static final Logger log = LoggerFactory.getLogger(MyService.class);
    public static void foo() {
        log.info("foo()");
    }
}
