package indi.mofan.a30;

import lombok.SneakyThrows;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

/**
 * @author mofan
 * @date 2023/1/26 17:59
 */
public class A30 {
    @SneakyThrows
    public static void main(String[] args) {
        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        resolver.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
        // 调用该方法，添加默认的参数解析器和返回值处理器
        resolver.afterPropertiesSet();

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerMethod handlerMethod = new HandlerMethod(new Controller1(), Controller1.class.getMethod("foo"));
        Exception e = new ArithmeticException("除以零");
        resolver.resolveException(request, response, handlerMethod, e);
        System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));

        handlerMethod = new HandlerMethod(new Controller2(), Controller2.class.getMethod("foo"));
        ModelAndView modelAndView = resolver.resolveException(request, response, handlerMethod, e);
        System.out.println(modelAndView.getModel());
        System.out.println(modelAndView.getViewName());

        // 嵌套异常
        handlerMethod = new HandlerMethod(new Controller3(), Controller3.class.getMethod("foo"));
        e = new Exception("e1", new RuntimeException("e2", new IOException("e3")));
        resolver.resolveException(request, response, handlerMethod, e);
        System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));

        // 异常处理方法参数处理
        handlerMethod = new HandlerMethod(new Controller4(), Controller4.class.getMethod("foo"));
        e = new Exception("e4");
        resolver.resolveException(request, response, handlerMethod, e);
        System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
    }

    static class Controller1 {
        public void foo() {
        }

        @ResponseBody
        @ExceptionHandler
        public Map<String, Object> handle(ArithmeticException e) {
            return Collections.singletonMap("error", e.getMessage());
        }
    }

    static class Controller2 {
        public void foo() {
        }

        @ExceptionHandler
        public ModelAndView handler(ArithmeticException e) {
            return new ModelAndView("test2", Collections.singletonMap("error", e.getMessage()));
        }
    }

    static class Controller3 {
        public void foo() {
        }

        @ResponseBody
        @ExceptionHandler
        public Map<String, Object> handle(IOException e) {
            return Collections.singletonMap("error", e.getMessage());
        }
    }

    static class Controller4 {
        public void foo() {}

        @ExceptionHandler
        @ResponseBody
        public Map<String, Object> handle(Exception e, HttpServletRequest request) {
            System.out.println(request);
            return Collections.singletonMap("error", e.getMessage());
        }
    }
}
