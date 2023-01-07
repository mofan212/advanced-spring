package indi.mofan.bean.a05;

import lombok.SneakyThrows;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

/**
 * @author mofan
 * @date 2023/1/7 23:45
 */
public class MapperPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    @SneakyThrows
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:indi/mofan/bean/a05/mapper/**/*.class");
        AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
        CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
        for (Resource resource : resources) {
            MetadataReader reader = factory.getMetadataReader(resource);
            ClassMetadata classMetadata = reader.getClassMetadata();
            if (classMetadata.isInterface()) {
                AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(MapperFactoryBean.class)
                        // 调用 addConstructorArgValue 方法添加 MapperFactoryBean 构造方法的参数信息
                        .addConstructorArgValue(classMetadata.getClassName())
                        // 修改注入模式，以便利用 MapperFactoryBean#setSqlSessionFactory() 方法向 MapperFactoryBean 注入 SqlSessionFactory
                        .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
                        .getBeanDefinition();
                AbstractBeanDefinition bd = BeanDefinitionBuilder.genericBeanDefinition(classMetadata.getClassName())
                        .getBeanDefinition();
                String name = generator.generateBeanName(bd, registry);
                registry.registerBeanDefinition(name, beanDefinition);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
