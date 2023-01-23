package indi.mofan.a20;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletResponse;

/**
 * @author mofan
 * @date 2023/1/23 21:36
 */
public class YmlReturnValueHandler implements HandlerMethodReturnValueHandler {
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(Yml.class) != null;
    }

    @Override
    public void handleReturnValue(Object returnValue,
                                  MethodParameter returnType,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
        // 转换返回结果为 YAML
        String str = new Yaml().dump(returnValue);
        // 将 YAML 字符串写入响应体
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType("text/plain;charset=utf-8");
        response.getWriter().print(str);
        // 设置请求已经处理完毕
        mavContainer.setRequestHandled(true);
    }
}
