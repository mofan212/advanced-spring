package indi.mofan.bean.a05;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2023/1/7 22:13
 */
public class ComponentScanPostProcessor implements BeanDefinitionRegistryPostProcessor {

    /**
     * 调用 context.refresh() 方法时回调
     */
    @Override
    @SneakyThrows
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
        if (componentScan != null) {
            for (String packageName : componentScan.basePackages()) {
                System.out.println(packageName);
                // indi.mofan.bean.a05.component -> classpath*:indi/mofan/bean/a05/component/**/**.class
                String path = "classpath*:" + packageName.replace(".", "/") + "/**/**.class";
                //  Resource[] resources = context.getResources(path);
                Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
                CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
                AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
                for (Resource resource : resources) {
                    MetadataReader reader = factory.getMetadataReader(resource);
                    AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
//                    System.out.println("类名: " + reader.getClassMetadata().getClassName());
//                    System.out.println("是否加了 @Component: " + annotationMetadata.hasAnnotation(Component.class.getName()));
//                    System.out.println("是否加了 @Component 派生: " + annotationMetadata.hasMetaAnnotation(Component.class.getName()));
                    if (annotationMetadata.hasAnnotation(Component.class.getName())
                            || annotationMetadata.hasMetaAnnotation(Component.class.getName())) {
                        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(reader.getClassMetadata().getClassName())
                                .getBeanDefinition();
                        String name = generator.generateBeanName(beanDefinition, registry);
                        registry.registerBeanDefinition(name, beanDefinition);
                    }
                }
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
