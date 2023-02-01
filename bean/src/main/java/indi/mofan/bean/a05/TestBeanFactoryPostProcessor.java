package indi.mofan.bean.a05;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * @author mofan
 * @date 2023/2/1 17:27
 */
@Slf4j
public class TestBeanFactoryPostProcessor {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean("bean2", Bean2.class);
        context.registerBean(MyBeanFactoryPostProcessor.class);
        context.refresh();

        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
        System.out.println(">>>>>>>>>>>>>>>>>>");
        System.out.println(context.getBean(Bean1.class));
    }

    static class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            Bean1 bean1 = new Bean1();
            bean1.setName("mofan");
            beanFactory.registerSingleton("bean1", bean1);
        }
    }

    @Getter
    @ToString
    static class Bean1 {
        @Setter
        private String name;
        private Bean2 bean2;

        @Autowired
        private void setBean2(Bean2 bean2) {
            log.debug("依赖注入 bean2");
            this.bean2 = bean2;
        }

        @PostConstruct
        public void init() {
            log.debug("初始化...");
        }
    }

    static class Bean2 {
    }
}
