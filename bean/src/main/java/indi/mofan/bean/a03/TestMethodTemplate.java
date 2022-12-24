package indi.mofan.bean.a03;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mofan
 * @date 2022/12/24 20:41
 */
public class TestMethodTemplate {
    public static void main(String[] args) {
        MyBeanFactory beanFactory = new MyBeanFactory();
        beanFactory.addProcessor(bean -> System.out.println("解析 @Autowired"));
        beanFactory.addProcessor(bean -> System.out.println("解析 @Resource"));
        beanFactory.getBean();
    }

    static class MyBeanFactory {
        public Object getBean() {
            Object bean = new Object();
            System.out.println("构造 " + bean);
            System.out.println("依赖注入 " + bean);
            for (BeanPostProcessor processor : processors) {
                processor.inject(bean);
            }
            System.out.println("初始化 " + bean);
            return bean;
        }

        private List<BeanPostProcessor> processors = new ArrayList<>();

        public void addProcessor(BeanPostProcessor processor) {
            processors.add(processor);
        }
    }

    interface BeanPostProcessor {
        void inject(Object bean);
    }
}
