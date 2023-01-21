package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mofan
 * @date 2023/1/21 18:21
 */
public class A18_1 {

    static class Target {
        public void foo() {
            System.out.println("Target foo()");
        }
    }

    static class Advice1 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("Advice1.before()");
            Object result = invocation.proceed();
            System.out.println("Advice1.after()");
            return result;
        }
    }

    static class Advice2 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("Advice2.before()");
            Object result = invocation.proceed();
            System.out.println("Advice2.after()");
            return result;
        }
    }

    static class MyInvocation implements MethodInvocation {

        private final Object target;
        private final Method method;
        private final Object[] args;
        private final List<MethodInterceptor> methodInterceptorList;
        private int count = 1;

        public MyInvocation(Object target, Method method, Object[] args, List<MethodInterceptor> methodInterceptorList) {
            this.target = target;
            this.method = method;
            this.args = args;
            this.methodInterceptorList = methodInterceptorList;
        }

        @Override
        public Method getMethod() {
            return this.method;
        }

        @Override
        public Object[] getArguments() {
            return this.args;
        }

        @Override
        public Object proceed() throws Throwable {
            if (count > methodInterceptorList.size()) {
                // 调用目标，结束递归并返回
                return method.invoke(target, args);
            }
            // 逐一调用通知
            MethodInterceptor interceptor = methodInterceptorList.get(count++ - 1);
            // 递归操作交给通知类
            return interceptor.invoke(this);
        }

        @Override
        public Object getThis() {
            return this.target;
        }

        @Override
        public AccessibleObject getStaticPart() {
            return method;
        }
    }

    public static void main(String[] args) throws Throwable {
        Target target = new Target();
        List<MethodInterceptor> list = new ArrayList<>(Arrays.asList(
                new Advice1(),
                new Advice2()
        ));
        MyInvocation invocation = new MyInvocation(target, Target.class.getMethod("foo"), new Object[0], list);
        invocation.proceed();
    }

}
