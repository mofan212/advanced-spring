package indi.mofan.a23;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

import java.util.Date;

/**
 * @author mofan
 * @date 2023/1/24 17:10
 */
public class TestServletDataBinder {
    public static void main(String[] args) {
        // web 环境下的数据绑定
        MyBean bean = new MyBean();
        DataBinder dataBinder = new ServletRequestDataBinder(bean);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("a", "10");
        request.setParameter("b", "hello");
        request.setParameter("c", "1999/03/04");

        dataBinder.bind(new ServletRequestParameterPropertyValues(request));

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
