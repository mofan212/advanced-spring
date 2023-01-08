package indi.mofan.bean.a09;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author mofan
 * @date 2023/1/8 19:28
 */
@Slf4j
@ComponentScan("indi.mofan.bean.a09")
public class A09Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A09Application.class);

        E e = context.getBean(E.class);
//        log.info("{}", e.getF1().getClass());
//        log.info("{}", e.getF1());
//        log.info("{}", e.getF1());
//        log.info("{}", e.getF1());

//        log.info("{}", e.getF2().getClass());
//        log.info("{}", e.getF2());
//        log.info("{}", e.getF2());
//        log.info("{}", e.getF2());

//        log.info("{}", e.getF3());
//        log.info("{}", e.getF3());

        log.info("{}", e.getF4());
        log.info("{}", e.getF4());

        context.close();
    }
}
