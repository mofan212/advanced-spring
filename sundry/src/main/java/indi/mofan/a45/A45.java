package indi.mofan.a45;

import lombok.SneakyThrows;
import org.springframework.aop.framework.Advised;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Method;

/**
 * @author mofan
 * @date 2023/1/30 22:01
 */
@SpringBootApplication
public class A45 {

    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A45.class, args);
        Bean1 proxy = context.getBean(Bean1.class);

//        proxy.setBean2(new Bean2());
//        proxy.init();
        showProxyAndTarget(proxy);

        System.out.println(">>>>>>>>>>>>>>>>>>>");
        System.out.println(proxy.getBean2());
        System.out.println(proxy.isInitialized());

        // static、final、private 修饰的方法不会被增强
        proxy.m1();
        proxy.m2();
        Bean1.m3();
        Method m4 = Bean1.class.getDeclaredMethod("m4");
        m4.setAccessible(true);
        m4.invoke(proxy);

        context.close();
    }

    @SneakyThrows
    public static void showProxyAndTarget(Bean1 proxy) {
        System.out.println(">>>>> 代理中的成员变量");
        System.out.println("\tinitialized = " + proxy.initialized);
        System.out.println("\tbean2 = " + proxy.bean2);

        if (proxy instanceof Advised) {
            Advised advised = (Advised) proxy;
            System.out.println(">>>>> 目标中的成员变量");
            Bean1 target = (Bean1) advised.getTargetSource().getTarget();
            System.out.println("\tinitialized = " + target.initialized);
            System.out.println("\tbean2 = " + target.bean2);
        }
    }
}
