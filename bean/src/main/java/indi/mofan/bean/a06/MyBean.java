package indi.mofan.bean.a06;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

/**
 * @author mofan
 * @date 2023/1/8 16:12
 */
@Slf4j
public class MyBean implements BeanNameAware, ApplicationContextAware, InitializingBean {
    @Override
    public void setBeanName(String name) {
        log.info("当前 Bean: " + this + "名字叫: " + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("当前 Bean: " + this + "容器是: " + applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("当前 Bean: " + this + " 初始化");
    }

    @Autowired
    public void setApplicationContextWithAutowired(ApplicationContext applicationContext) {
        log.info("当前 Bean: " + this + " 使用 @Autowired 注解，容器是: " + applicationContext);
    }

    @PostConstruct
    public void init() {
        log.info("当前 Bean: " + this + " 使用 @PostConstruct 注解初始化");
    }
}
