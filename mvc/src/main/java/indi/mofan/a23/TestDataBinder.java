package indi.mofan.a23;

import lombok.ToString;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

import java.util.Date;

/**
 * @author mofan
 * @date 2023/1/24 17:00
 */
public class TestDataBinder {
    public static void main(String[] args) {
        // 执行数据绑定
        MyBean bean = new MyBean();
        DataBinder binder = new DataBinder(bean);
        binder.initDirectFieldAccess();
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.add("a", "10");
        pvs.add("b", "hello");
        pvs.add("c", "1999/03/04");
        binder.bind(pvs);
        System.out.println(bean);
    }

    @ToString
    static class MyBean {
        private int a;
        private String b;
        private Date c;
    }
}
