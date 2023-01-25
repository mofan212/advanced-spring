package indi.mofan.a27;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.HttpHeadersReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.ModelAndViewMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ViewNameMethodReturnValueHandler;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.springframework.web.util.UrlPathHelper;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author mofan
 * @date 2023/1/25 13:11
 */
@Slf4j
public class A27 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);
        test7(context);
    }

    private static void test1(ApplicationContext context) {
        testReturnValueProcessor(context, "test1", null, null);
    }

    private static void test2(ApplicationContext context) {
        testReturnValueProcessor(context, "test2", null, null);
    }

    private static Consumer<MockHttpServletRequest> mockHttpServletRequestConsumer(String methodName) {
        return req -> {
            req.setRequestURI("/" + methodName);
            UrlPathHelper.defaultInstance.resolveAndCacheLookupPath(req);
        };
    }

    private static void test3(ApplicationContext context) {
        String methodName = "test3";
        testReturnValueProcessor(context, methodName, mockHttpServletRequestConsumer(methodName), null);
    }

    private static void test4(ApplicationContext context) {
        String methodName = "test4";
        testReturnValueProcessor(context, methodName, mockHttpServletRequestConsumer(methodName), null);
    }

    private static final Consumer<MockHttpServletResponse> RESPONSE_CONSUMER = resp -> {
        for (String name : resp.getHeaderNames()) {
            System.out.println(name + " = " + resp.getHeader(name));
        }
        System.out.println(new String(resp.getContentAsByteArray(), StandardCharsets.UTF_8));
    };

    private static void test5(ApplicationContext context) {
        testReturnValueProcessor(context, "test5", null, RESPONSE_CONSUMER);
    }

    private static void test6(ApplicationContext context) {
        testReturnValueProcessor(context, "test6", null, RESPONSE_CONSUMER);
    }

    private static void test7(ApplicationContext context) {
        testReturnValueProcessor(context, "test7", null, RESPONSE_CONSUMER);
    }

    @SneakyThrows
    private static void testReturnValueProcessor(ApplicationContext context, String methodName,
                                                 Consumer<MockHttpServletRequest> requestConsumer,
                                                 Consumer<MockHttpServletResponse> responseConsumer) {
        Method method = Controller.class.getMethod(methodName);
        Controller controller = new Controller();
        Object returnValue = method.invoke(controller);

        HandlerMethod handlerMethod = new HandlerMethod(context, method);
        ModelAndViewContainer container = new ModelAndViewContainer();

        HandlerMethodReturnValueHandlerComposite composite = getReturnValueHandler();
        MethodParameter returnType = handlerMethod.getReturnType();
        MockHttpServletRequest request = new MockHttpServletRequest();
        Optional.ofNullable(requestConsumer).ifPresent(i -> i.accept(request));
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        if (composite.supportsReturnType(returnType)) {
            composite.handleReturnValue(returnValue, returnType, container, webRequest);
            System.out.println(container.getModel());
            System.out.println(container.getViewName());
            if (!container.isRequestHandled()) {
                // 渲染视图
                renderView(context, container, webRequest);
            } else {
                Optional.ofNullable(responseConsumer).ifPresent(i -> i.accept(response));
            }
        }
    }

    public static HandlerMethodReturnValueHandlerComposite getReturnValueHandler() {
        HandlerMethodReturnValueHandlerComposite composite = new HandlerMethodReturnValueHandlerComposite();
        composite.addHandlers(Arrays.asList(
                new ModelAndViewMethodReturnValueHandler(),
                new ViewNameMethodReturnValueHandler(),
                new ServletModelAttributeMethodProcessor(false),
                new HttpEntityMethodProcessor(Collections.singletonList(new MappingJackson2HttpMessageConverter())),
                new HttpHeadersReturnValueHandler(),
                new RequestResponseBodyMethodProcessor(Collections.singletonList(new MappingJackson2HttpMessageConverter())),
                new ServletModelAttributeMethodProcessor(true)
        ));
        return composite;
    }

    @SuppressWarnings("all")
    private static void renderView(ApplicationContext context, ModelAndViewContainer container,
                                   ServletWebRequest webRequest) throws Exception {
        log.debug(">>>>>> 渲染视图");
        FreeMarkerViewResolver resolver = context.getBean(FreeMarkerViewResolver.class);
        String viewName = container.getViewName() != null ? container.getViewName() : new DefaultRequestToViewNameTranslator().getViewName(webRequest.getRequest());
        log.debug("没有获取到视图名, 采用默认视图名: {}", viewName);
        // 每次渲染时, 会产生新的视图对象, 它并非被 Spring 所管理, 但确实借助了 Spring 容器来执行初始化
        View view = resolver.resolveViewName(viewName, Locale.getDefault());
        view.render(container.getModel(), webRequest.getRequest(), webRequest.getResponse());
        System.out.println(new String(((MockHttpServletResponse) webRequest.getResponse()).getContentAsByteArray(), StandardCharsets.UTF_8));
    }

    @Slf4j
    static class Controller {
        public ModelAndView test1() {
            log.debug("test1()");
            ModelAndView mav = new ModelAndView("view1");
            mav.addObject("name", "张三");
            return mav;
        }

        public String test2() {
            log.debug("test2()");
            return "view2";
        }

        @ModelAttribute
//        @RequestMapping("/test3")
        public User test3() {
            log.debug("test3()");
            return new User("李四", 20);
        }

        public User test4() {
            log.debug("test4()");
            return new User("王五", 30);
        }

        public HttpEntity<User> test5() {
            log.debug("test5()");
            return new HttpEntity<>(new User("赵六", 40));
        }

        public HttpHeaders test6() {
            log.debug("test6()");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "text/html");
            return headers;
        }

        @ResponseBody
        public User test7() {
            log.debug("test7()");
            return new User("钱七", 50);
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
