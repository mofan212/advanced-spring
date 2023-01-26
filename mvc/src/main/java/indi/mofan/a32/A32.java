package indi.mofan.a32;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author mofan
 * @date 2023/1/26 19:28
 */
public class A32 {
    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context =
                new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        handlerMapping.getHandlerMethods().forEach((k, v) -> System.out.println("映射路径: " + k + "\t方法信息: " + v));
    }
}
