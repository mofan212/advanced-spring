package indi.mofan.a14;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author mofan
 * @date 2023/1/18 21:31
 */
public class A14 {
    public static void main(String[] args) {
        Target target = new Target();

        Proxy proxy = new Proxy(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                System.out.println("before");
                return method.invoke(target, args);
            }
        });

        proxy.save();
        proxy.save(1);
        proxy.save(2L);
    }
}
