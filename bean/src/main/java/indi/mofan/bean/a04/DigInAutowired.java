package indi.mofan.bean.a04;

import lombok.SneakyThrows;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author mofan
 * @date 2022/12/27 21:29
 */
public class DigInAutowired {
    @SneakyThrows
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 注册成品 Bean，不再进行 Bean 的创建、依赖注入、初始化等操作
        beanFactory.registerSingleton("bean2", new Bean2());
        beanFactory.registerSingleton("bean3", new Bean3());
        // @Value
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        // ${} 的解析器
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);

        // 查看哪些属性、方法加了 @Autowired，这称之为 InjectionMetadata
        AutowiredAnnotationBeanPostProcessor postProcessor = new AutowiredAnnotationBeanPostProcessor();
        postProcessor.setBeanFactory(beanFactory);

        Bean1 bean1 = new Bean1();
//        System.out.println(bean1);
//        // 执行依赖注入，@Autowired、@Value
//        postProcessor.postProcessProperties(null, bean1, "bean1");
//        System.out.println(bean1);

        Method method = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        method.setAccessible(true);
        // 获取 Bean1 上加了 @Value、@Autowired 注解的成员变量、方法参数信息
        InjectionMetadata metadata = (InjectionMetadata) method.invoke(postProcessor, "bean1", Bean1.class, null);
        // 此处断点
        System.out.println(metadata);

        // 调用 InjectionMetadata 来进行依赖注入，注入时按类型查找值
        metadata.inject(bean1, "bean1", null);
        System.out.println(bean1);

        // 如何按类型查找值
        Field bean3 = Bean1.class.getDeclaredField("bean3");
        DependencyDescriptor dd1 = new DependencyDescriptor(bean3, false);
        Object o1 = beanFactory.doResolveDependency(dd1, null, null, null);
        System.out.println(o1);

        Method setBean2 = Bean1.class.getDeclaredMethod("setBean2", Bean2.class);
        // MethodParameter 构造方法的第二个参数表示需要解析的方法中参数的索引
        DependencyDescriptor dd2 = new DependencyDescriptor(new MethodParameter(setBean2, 0), false);
        Object o2 = beanFactory.doResolveDependency(dd2, null, null, null);
        System.out.println(o2);

        Method setHome = Bean1.class.getDeclaredMethod("setHome", String.class);
        DependencyDescriptor dd3 = new DependencyDescriptor(new MethodParameter(setHome, 0), true);
        Object o3 = beanFactory.doResolveDependency(dd3, null, null, null);
        System.out.println(o3);
    }
}
