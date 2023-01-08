package indi.mofan.bean.a09;

import lombok.Getter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2023/1/8 19:24
 */
@Component
public class E {
    @Lazy
    @Getter
    @Autowired
    private F1 f1;

    @Getter
    @Autowired
    private F2 f2;

    @Autowired
    private ObjectFactory<F3> f3;

    @Autowired
    private ApplicationContext applicationContext;

    public F3 getF3() {
        return f3.getObject();
    }

    public F4 getF4() {
        return applicationContext.getBean(F4.class);
    }
}
