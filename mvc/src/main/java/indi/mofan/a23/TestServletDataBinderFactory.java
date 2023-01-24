package indi.mofan.a23;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.util.Date;

/**
 * @author mofan
 * @date 2023/1/24 17:21
 */
public class TestServletDataBinderFactory {
    @SneakyThrows
    public static void main(String[] args) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("birthday", "1999|01|02");
        request.setParameter("address.name", "成都");

        User user = new User();

        // 使用 @InitBinder 转换
//        InvocableHandlerMethod handlerMethod =
//                new InvocableHandlerMethod(new MyController(), MyController.class.getMethod("myMethod", WebDataBinder.class));
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(Collections.singletonList(handlerMethod), null);

        // 使用 ConversionService 转换
//        FormattingConversionService service = new FormattingConversionService();
//        service.addFormatter(new MyDateFormatter("用 ConversionService 方式拓展转换功能"));
//        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
//        initializer.setConversionService(service);
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, initializer);

        // 同时存在
//        FormattingConversionService service = new FormattingConversionService();
//        service.addFormatter(new MyDateFormatter("用 ConversionService 方式拓展转换功能"));
//        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
//        initializer.setConversionService(service);
//        InvocableHandlerMethod handlerMethod =
//                new InvocableHandlerMethod(new MyController(), MyController.class.getMethod("myMethod", WebDataBinder.class));
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(Collections.singletonList(handlerMethod), initializer);

        // 默认 ConversionService
//        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        ApplicationConversionService conversionService = new ApplicationConversionService();
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(conversionService);
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, initializer);

        WebDataBinder dataBinder = factory.createBinder(new ServletWebRequest(request), user, "user");
        dataBinder.bind(new ServletRequestParameterPropertyValues(request));
        System.out.println(user);
    }

    static class MyController {
        @InitBinder
        public void myMethod(WebDataBinder dataBinder) {
            // 拓展 dataBinder 的转换器
            dataBinder.addCustomFormatter(new MyDateFormatter("用 @InitBinder 进行拓展"));
        }
    }

    @Getter
    @Setter
    @ToString
    public static class User {
        @DateTimeFormat(pattern = "yyyy|MM|dd")
        private Date birthday;
        private Address address;
    }

    @Getter
    @Setter
    @ToString
    public static class Address {
        private String name;
    }
}
