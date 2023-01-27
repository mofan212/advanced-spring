package indi.mofan.bean.a01;

import org.springframework.context.ApplicationEvent;

/**
 * @author mofan
 * @date 2023/1/27 13:26
 */
public class UserRegisteredEvent extends ApplicationEvent {
    private static final long serialVersionUID = 6319117283222183184L;

    public UserRegisteredEvent(Object source) {
        super(source);
    }
}
