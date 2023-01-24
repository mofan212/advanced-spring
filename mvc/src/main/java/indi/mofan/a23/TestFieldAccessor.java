package indi.mofan.a23;

import lombok.ToString;
import org.springframework.beans.DirectFieldAccessor;

import java.util.Date;

/**
 * @author mofan
 * @date 2023/1/24 16:56
 */
public class TestFieldAccessor {
    public static void main(String[] args) {
        // 利用反射为 bean 的字段赋值
        MyBean bean = new MyBean();
        DirectFieldAccessor accessor = new DirectFieldAccessor(bean);
        accessor.setPropertyValue("a", "10");
        accessor.setPropertyValue("b", "hello");
        accessor.setPropertyValue("c", "1999/03/04");
        System.out.println(bean);
    }

    @ToString
    static class MyBean {
        private int a;
        private String b;
        private Date c;
    }
}
