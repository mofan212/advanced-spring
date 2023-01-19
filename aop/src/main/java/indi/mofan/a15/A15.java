package indi.mofan.a15;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * @author mofan
 * @date 2023/1/19 20:08
 */
public class A15 {

    public static void main(String[] args) {
        /*
         * 两个切面概念：
         *  aspect =
         *          通知 1 （advice） + 切点 1（pointcut）
         *          通知 2 （advice） + 切点 2（pointcut）
         *          通知 3 （advice） + 切点 3（pointcut）
         *          ...
         *
         * advisor = 更细粒度的切面，包含一个通知和切点
         * */

        // 1. 备好切点（根据 AspectJ 表达式进行匹配）
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* foo())");
        // 2. 备好通知
        MethodInterceptor advice = invocation -> {
            System.out.println("before...");
            Object result = invocation.proceed();
            System.out.println("after...");
            return result;
        };
        // 3. 备好切面
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
        // 4. 创建代理
        Target2 target = new Target2();
        ProxyFactory factory = new ProxyFactory();
        factory.setTarget(target);
        factory.addAdvisor(advisor);
        factory.setInterfaces(target.getClass().getInterfaces());
        factory.setProxyTargetClass(false);

        Target2 proxy = (Target2) factory.getProxy();
        System.out.println(proxy.getClass());
        proxy.foo();
        proxy.bar();
    }

    interface I1 {
        void foo();

        void bar();
    }

    static class Target1 implements I1 {

        @Override
        public void foo() {
            System.out.println("target1 foo");
        }

        @Override
        public void bar() {
            System.out.println("target1 bar");
        }
    }

    static class Target2 {
        public void foo() {
            System.out.println("target2 foo");
        }

        public void bar() {
            System.out.println("target2 bar");
        }
    }
}
