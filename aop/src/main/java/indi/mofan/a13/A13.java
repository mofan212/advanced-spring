package indi.mofan.a13;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author mofan
 * @date 2023/1/11 21:25
 */
public class A13 {
    interface Foo {
        void foo();

        int bar();
    }

    static class Target implements Foo {
        @Override
        public void foo() {
            System.out.println("target foo");
        }

        @Override
        public int bar() {
            System.out.println("target bar");
            return 100;
        }
    }

//    interface InvocationHandler {
//        Object invoke(Object proxy, Method method, Object[] params) throws Throwable;
//    }

    public static void main(String[] args) {
        $Proxy0 proxy = new $Proxy0(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
                // 1. 功能增强
                System.out.println("before...");
                // 2. 调用目标
                return method.invoke(new Target(), params);
            }
        });
        proxy.foo();
        // 调用另一个方法
        System.out.println(proxy.bar());
    }
}
