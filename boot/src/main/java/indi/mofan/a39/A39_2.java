package indi.mofan.a39;

import lombok.SneakyThrows;
import org.springframework.boot.DefaultBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author mofan
 * @date 2023/1/28 10:36
 */
public class A39_2 {
    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication();
        app.addListeners(i -> System.out.println(i.getClass()));

        // 获取时间发送器实现类名
        List<String> names = SpringFactoriesLoader.loadFactoryNames(
                SpringApplicationRunListener.class,
                A39_2.class.getClassLoader()
        );
        for (String name : names) {
//            System.out.println(name);
            Class<?> clazz = Class.forName(name);
            Constructor<?> constructor = clazz.getConstructor(SpringApplication.class, String[].class);
            SpringApplicationRunListener publisher = (SpringApplicationRunListener) constructor.newInstance(app, args);

            // 发布事件
            DefaultBootstrapContext bootstrapContext = new DefaultBootstrapContext();
            // spring boot 开始启动
            publisher.starting(bootstrapContext);
            // 环境信息准备完毕
            publisher.environmentPrepared(bootstrapContext, new StandardEnvironment());
            // 创建 spring 容器，调用初始化器之后发布此事件
            GenericApplicationContext context = new GenericApplicationContext();
            publisher.contextPrepared(context);
            // 所有 bean definition 加载完毕
            publisher.contextLoaded(context);
            // spring 容器初始化完毕（调用 refresh() 方法后）
            context.refresh();
            publisher.started(context, null);
            // spring boot 启动完毕
            publisher.ready(context, null);

            // 启动过程中出现异常，spring boot 启动出错
            publisher.failed(context, new Exception("出错了"));
        }
    }
}
