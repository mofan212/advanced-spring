package indi.mofan.a13;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author mofan
 * @date 2023/1/16 22:36
 */
public class TestMethodProxy {

    public static void main(String[] args) throws Exception {
        Method foo = TestMethodProxy.class.getMethod("foo", int.class);
        for (int i = 1; i <= 17; i++) {
            show(i, foo);
            foo.invoke(null, i);
        }
        System.in.read();
    }

    // 方法反射调用时，底层使用了 MethodAccessor 的实现类
    private static void show(int i, Method foo) throws Exception {
        Method getMethodAccessor = Method.class.getDeclaredMethod("getMethodAccessor");
        getMethodAccessor.setAccessible(true);
        Object invoke = getMethodAccessor.invoke(foo);
        if (invoke == null) {
            System.out.println(i + ": " + null);
            return;
        }
        // DelegatingMethodAccessorImpl 的全限定类名（不同版本的 JDK 存在差异）
        Field delegate = Class.forName("sun.reflect.DelegatingMethodAccessorImpl").getDeclaredField("delegate");
        delegate.setAccessible(true);
        System.out.println(i + ": " + delegate.get(invoke));
    }

    public static void foo(int i) {
        System.out.println(i + ": foo");
    }
}
