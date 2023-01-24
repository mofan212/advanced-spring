package indi.mofan.a21;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.ui.ModelMap;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletCookieValueMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mofan
 * @date 2023/1/24 10:45
 */
public class A21 {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        HttpServletRequest request = mockRequest();

        // 控制器方法封装成 HandlerMethod
        Method method = Controller.class.getMethod("test", String.class, String.class,
                int.class, String.class, MultipartFile.class,
                int.class, String.class, String.class,
                String.class, HttpServletRequest.class, User.class,
                User.class, User.class);
        HandlerMethod handlerMethod = new HandlerMethod(new Controller(), method);

        // 准备对象绑定与类型转换
        ServletRequestDataBinderFactory binderFactory = new ServletRequestDataBinderFactory(null, null);

        // 准备 ModelAndViewContainer 用来存储中间的 Model 结果
        ModelAndViewContainer container = new ModelAndViewContainer();

        // 解析每个参数值
        for (MethodParameter parameter : handlerMethod.getMethodParameters()) {
            // 多个参数解析器的组合
            HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
            composite.addResolvers(
                    // useDefaultResolution 为 false 表示必须添加 @RequestParam 注解
                    new RequestParamMethodArgumentResolver(beanFactory, false),
                    // 解析 @PathVariable
                    new PathVariableMethodArgumentResolver(),
                    // 解析 @RequestHeader
                    new RequestHeaderMethodArgumentResolver(beanFactory),
                    // 解析 @CookieValue
                    new ServletCookieValueMethodArgumentResolver(beanFactory),
                    // 解析 @Value
                    new ExpressionValueMethodArgumentResolver(beanFactory),
                    // 解析 HttpServletRequest
                    new ServletRequestMethodArgumentResolver(),
                    // 解析 @ModelAttribute，且不能省略
                    new ServletModelAttributeMethodProcessor(false),
                    new RequestResponseBodyMethodProcessor(Collections.singletonList(new MappingJackson2HttpMessageConverter())),
                    new ServletModelAttributeMethodProcessor(true),
                    new RequestParamMethodArgumentResolver(beanFactory, true)
            );

            String annotations = Arrays.stream(parameter.getParameterAnnotations())
                    .map(i -> i.annotationType().getSimpleName()).collect(Collectors.joining());
            String appendAt = annotations.length() > 0 ? "@" + annotations + " " : "";
            // 设置参数名解析器
            parameter.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());
            String paramInfo = "[" + parameter.getParameterIndex() + "] " + appendAt +
                    parameter.getParameterType().getSimpleName() + " " + parameter.getParameterName();

            if (composite.supportsParameter(parameter)) {
                Object v = composite.resolveArgument(parameter, container, new ServletWebRequest(request), binderFactory);
                System.out.println(paramInfo + " -> " + v);
                // 打印模型数据
                ModelMap modelMap = container.getModel();
                if (MapUtils.isNotEmpty(modelMap)) {
                    System.out.println("模型数据: " + modelMap);
                }
            } else {
                System.out.println(paramInfo);
            }
        }
    }

    private static HttpServletRequest mockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name1", "zhangsan");
        request.setParameter("name2", "lisi");
        request.addPart(new MockPart("file", "abc", "hello".getBytes(StandardCharsets.UTF_8)));
        Map<String, String> map = new AntPathMatcher().extractUriTemplateVariables("/test/{id}", "/test/123");
//        System.out.println(map);
        request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, map);
        request.setContentType("application/json");
        request.setCookies(new Cookie("token", "123456"));
        request.setParameter("name", "张三");
        request.setParameter("age", "18");
        //language=JSON
        String json = "{\n" +
                "  \"name\":\"李四\",\n" +
                "  \"age\":20\n" +
                "}";
        request.setContent(json.getBytes(StandardCharsets.UTF_8));

        return new StandardServletMultipartResolver().resolveMultipart(request);
    }

    static class Controller {
        public void test(
                @RequestParam("name1") String name1,                                        // name1=张三
                String name2,                                                               // name2=李四
                @RequestParam("age") int age,                                               // age=18
                @RequestParam(name = "home", defaultValue = "${JAVA_HOME}") String home1,   // spring 获取数据
                @RequestParam("file") MultipartFile file,                                   // 上传文件
                @PathVariable("id") int id,                                                 //  /test/124   /test/{id}
                @RequestHeader("Content-Type") String header,
                @CookieValue("token") String token,
                @Value("${JAVA_HOME}") String home2,                                        // spring 获取数据  ${} #{}
                HttpServletRequest request,                                                 // request, response, session ...
                @ModelAttribute("abc") User user1,                                          // name=zhang&age=18
                User user2,                                                                 // name=zhang&age=18
                @RequestBody User user3                                                     // json
        ) {
        }
    }

    @Getter
    @Setter
    @ToString
    static class User {
        private String name;
        private int age;
    }
}
