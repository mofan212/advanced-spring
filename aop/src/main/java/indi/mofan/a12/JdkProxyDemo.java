package indi.mofan.a12;

import java.lang.reflect.Proxy;

/**
 * @author mofan
 * @date 2023/1/10 21:35
 */
public class JdkProxyDemo {
    interface Foo {
        void foo();
    }

    static final class Target implements Foo {
        @Override
        public void foo() {
            System.out.println("target foo");
        }
    }

    public static void main(String[] args) {
        // 将动态代理生成的 class 保存到磁盘
        // System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        // 原始对象
        Target target = new Target();

        // 用来加载在运行期间动态生成的字节码
        ClassLoader classLoader = JdkProxyDemo.class.getClassLoader();
        Foo proxy = (Foo) Proxy.newProxyInstance(classLoader, new Class[]{Foo.class}, (p, method, params) -> {
            System.out.println("before...");
            // 目标.方法(参数) --> 方法.invoke(目标, 参数)
            Object result = method.invoke(target, params);
            System.out.println("after...");
            // 也返回目标方法执行的结果
            return result;
        });

        proxy.foo();
        System.out.println("-------");
        proxy.toString();
        System.out.println("-------");
        proxy.hashCode();
        System.out.println("-------");
        proxy.equals(proxy);
    }
}
