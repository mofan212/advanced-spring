package indi.mofan.a20;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author mofan
 * @date 2023/1/22 22:36
 */
@Slf4j
public class A20 {

    @SuppressWarnings("all")
    public static void main(String[] args) throws Exception {
        AnnotationConfigServletWebServerApplicationContext context =
                new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
        // 解析 @RequestMapping 以及派生注解，在初始化时生成路径与控制器方法的映射关系
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        // 获取映射结果
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        handlerMethods.forEach((k, v) -> System.out.println(k + " = " + v));
        // 根据给定请求获取控制器方法，返回处理器执行链
        HandlerExecutionChain chain = handlerMapping.getHandler(new MockHttpServletRequest("GET", "/test1"));
        System.out.println(chain);

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test2");
        request.setParameter("name", "mofan");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MyRequestMappingHandlerAdapter handlerAdapter = context.getBean(MyRequestMappingHandlerAdapter.class);
        handlerAdapter.invokeHandlerMethod(request, response, ((HandlerMethod) handlerMapping.getHandler(request).getHandler()));

        System.out.println(">>>>>>>>>>>>>>>>>>> 所有参数解析器");
        handlerAdapter.getArgumentResolvers().forEach(System.out::println);
        System.out.println(">>>>>>>>>>>>>>>>>>> 所有返回值解析器");
        handlerAdapter.getReturnValueHandlers().forEach(System.out::println);

        MockHttpServletRequest tokenRequest = new MockHttpServletRequest("PUT", "/test3");
        tokenRequest.addHeader("token", "token info");
        handlerAdapter.invokeHandlerMethod(tokenRequest, response, ((HandlerMethod) handlerMapping.getHandler(tokenRequest).getHandler()));

        MockHttpServletRequest test4Req = new MockHttpServletRequest("GET", "/test4");
        handlerAdapter.invokeHandlerMethod(test4Req, response, ((HandlerMethod) handlerMapping.getHandler(test4Req).getHandler()));
        byte[] content = response.getContentAsByteArray();
        System.out.println(new String(content, StandardCharsets.UTF_8));
    }
}
