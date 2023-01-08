package indi.mofan.bean.a06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mofan
 * @date 2023/1/8 17:13
 */
@Slf4j
@Configuration
public class MyConfig2 implements InitializingBean, ApplicationContextAware {
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("初始化");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("注入 ApplicationContext");
    }

    @Bean
    public BeanFactoryPostProcessor processor2() {
        return processor -> log.info("执行 processor2");
    }
}
