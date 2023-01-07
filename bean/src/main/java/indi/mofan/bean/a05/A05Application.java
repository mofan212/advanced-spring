package indi.mofan.bean.a05;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author mofan
 * @date 2023/1/7 18:50
 */
@Slf4j
public class A05Application {
    @SneakyThrows
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", Config.class);
        // @ComponentScan @Bean @Import @ImportResource
//        context.registerBean(ConfigurationClassPostProcessor.class);
//        context.registerBean(MapperScannerConfigurer.class,
//                i -> i.getPropertyValues().add("basePackage", "indi.mofan.bean.a05.mapper"));

//        context.registerBean(ComponentScanPostProcessor.class);

        context.registerBean(AtBeanPostProcessor.class);

        /*
         * AtBeanPostProcessor 的注册不能少，因为需要容器中存在 SqlSessionFactoryBean
         * 而 SqlSessionFactoryBean 是在配置类中利用 @Bean 进行注册的
         */
        context.registerBean(MapperPostProcessor.class);

        context.refresh();

        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        context.close();
    }
}
