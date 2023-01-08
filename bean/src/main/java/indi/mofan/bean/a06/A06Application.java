package indi.mofan.bean.a06;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author mofan
 * @date 2023/1/8 16:12
 */
public class A06Application {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
//        context.registerBean("myConfig1", MyConfig1.class);
        context.registerBean("myConfig2", MyConfig2.class);
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        context.registerBean(CommonAnnotationBeanPostProcessor.class);
//        context.registerBean("myBean", MyBean.class);

        // 解析配置类中的注解
        context.registerBean(ConfigurationClassPostProcessor.class);

        context.refresh();
        context.close();
    }
}
