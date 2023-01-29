package indi.mofan.a43;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author mofan
 * @date 2023/1/29 21:55
 */
@ComponentScan
public class A43 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A43.class);
        Bean1 bean1 = context.getBean("bean1", Bean1.class);
        System.out.println(bean1);

        System.out.println(context.getBean(Bean1FactoryBean.class));
        System.out.println(context.getBean("&bean1"));

        context.close();
    }
}
