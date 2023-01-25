package indi.mofan.a29;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author mofan
 * @date 2023/1/25 23:00
 */
@Configuration
public class WebConfig {

    @ControllerAdvice
    static class MyControllerAdvice implements ResponseBodyAdvice<Object> {

        @Override
        public boolean supports(MethodParameter returnType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
            /*
             * 满足条件才转换
             * 1. 控制器方法被 @ResponseBody 注解标记
             * 2. 控制器方法所在类被 @ResponseBody 注解或包含 @ResponseBody 注解的注解标记
             */
            return returnType.getMethodAnnotation(ResponseBody.class) != null
                    || AnnotationUtils.findAnnotation(returnType.getContainingClass(), ResponseBody.class) != null;
        }

        @Override
        public Object beforeBodyWrite(Object body,
                                      MethodParameter returnType,
                                      MediaType selectedContentType,
                                      Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                      ServerHttpRequest request,
                                      ServerHttpResponse response) {
            if (body instanceof Result) {
                return body;
            }
            return Result.ok(body);
        }
    }

    @RestController
    public static class MyController {
        public User user() {
            return new User("王五", 18);
        }
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class User {
        private String name;
        private int age;
    }
}
