package indi.mofan;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;

/**
 * @author mofan
 * @date 2023/1/16 22:07
 */
public class TestProxy {
    public static void main(String[] args) throws Exception {
        byte[] dump = $Proxy0Dump.dump();

//        FileOutputStream os = new FileOutputStream("$Proxy0.class");
//        os.write(dump, 0, dump.length);
//        os.close();

        ClassLoader classLoader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                return super.defineClass(name, dump, 0, dump.length);
            }
        };

        Class<?> proxyClass = classLoader.loadClass("indi.mofan.$Proxy0");
        Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
        Foo fooProxy = (Foo) constructor.newInstance((InvocationHandler) (proxy, method, args1) -> {
            System.out.println("before...");
            System.out.println("模拟调用目标");
            return null;
        });

        fooProxy.foo();
    }
}
