package indi.mofan.a12;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

/**
 * @author mofan
 * @date 2023/1/10 21:55
 */
public class CglibProxyDemo {
    static class Target {
        public void foo() {
            System.out.println("target foo");
        }
    }

    public static void main(String[] args) {
        Target target = new Target();

        Target proxy = (Target) Enhancer.create(Target.class, (MethodInterceptor) (obj, method, params, methodProxy) -> {
            System.out.println("before...");
            // 用方法反射调用目标
//            Object result = method.invoke(target, params);
            // methodProxy 可以避免使用反射
            // Object result = methodProxy.invoke(target, args); // 内部没使用反射，需要目标（spring 的选择）
            Object result = methodProxy.invokeSuper(obj, args); // 内部没使用反射，需要代理
            System.out.println("after...");
            return result;
        });

        proxy.foo();
    }
}
