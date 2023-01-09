package indi.mofan;

import indi.mofan.service.MyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mofan
 * @date 2023/1/9 21:15
 */
//@SpringBootApplication
public class A10Application {
    private static final Logger log = LoggerFactory.getLogger(A10Application.class);

    public static void main(String[] args) {
//        ConfigurableApplicationContext context = SpringApplication.run(A10Application.class, args);
//        MyService service = context.getBean(MyService.class);

//        MyService service = new MyService();
//        log.info("service class: {}", service.getClass());
        MyService.foo();

//        context.close();
    }
}
