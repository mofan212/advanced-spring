package indi.mofan.bean.a03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/12/24 17:59
 */
@Slf4j
@Component
public class MyBeanPostProcessor implements InstantiationAwareBeanPostProcessor, DestructionAwareBeanPostProcessor {

    @Override
    public void postProcessBeforeDestruction(Object o, String beanName) throws BeansException {
        if ("lifeCycleBean".equals(beanName)) {
            log.info("<<<<<<<<<< 销毁执行之前，如 @PreDestroy");
        }
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if ("lifeCycleBean".equals(beanName)) {
            log.info("<<<<<<<<<< 实例化之前执行，这里返回的对象会替换掉原本的 bean");
        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if ("lifeCycleBean".equals(beanName)) {
            log.info("<<<<<<<<<< 实例化之后执行，如果返回 false 会跳过依赖注入节点");
//            return false;
        }
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if ("lifeCycleBean".equals(beanName)) {
            log.info("<<<<<<<<<< 依赖注入阶段执行，如 @Autowired、@Value、@Resource");
        }
        return pvs;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("lifeCycleBean".equals(beanName)) {
            log.info("<<<<<<<<<< 初始化执行之前，这里返回的对象会替换掉原本的 bean，如 @PostConstruct、@ConfigurationProperties");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("lifeCycleBean".equals(beanName)) {
            log.info("<<<<<<<<<< 初始化之后执行，这里返回的对象会替换掉原本的 bean，如代理增强");
        }
        return bean;
    }
}
