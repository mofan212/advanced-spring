package indi.mofan.a23;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Date;

/**
 * @author mofan
 * @date 2023/1/24 16:49
 */
public class TestBeanWrapper {
    public static void main(String[] args) {
        // 利用反射为 bean 的属性赋值
        MyBean bean = new MyBean();
        BeanWrapperImpl wrapper = new BeanWrapperImpl(bean);
        wrapper.setPropertyValue("a", "10");
        wrapper.setPropertyValue("b", "hello");
        wrapper.setPropertyValue("c", "1999/03/04");
        System.out.println(bean);
    }

    @Getter
    @Setter
    @ToString
    static class MyBean {
        private int a;
        private String b;
        private Date c;
    }
}
