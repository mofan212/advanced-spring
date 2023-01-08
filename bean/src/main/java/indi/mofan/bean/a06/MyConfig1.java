package indi.mofan.bean.a06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author mofan
 * @date 2023/1/8 16:47
 */
@Slf4j
@Configuration
public class MyConfig1 {
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        log.info("注入 ApplicationContext");
    }

    @PostConstruct
    public void init() {
        log.info("初始化");
    }

    @Bean
    public BeanFactoryPostProcessor processor1() {
        return processor -> log.info("执行 processor1");
    }
}
