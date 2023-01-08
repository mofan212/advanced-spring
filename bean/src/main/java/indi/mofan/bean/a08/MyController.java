package indi.mofan.bean.a08;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author mofan
 * @date 2023/1/8 18:08
 */
@RestController
public class MyController {
    @Lazy
    @Autowired
    private BeanForRequest beanForRequest;

    @Lazy
    @Autowired
    private BeanForSession beanForSession;

    @Lazy
    @Autowired
    private BeanForApplication beanForApplication;

    @GetMapping(value = "/test", produces = "text/html")
    public String test(HttpServletRequest request, HttpSession session) {
        // 设置 session 过期时间为 10 秒
        session.setMaxInactiveInterval(10);
        // ServletContext sc = request.getServletContext();
        return "<ul>" +
                "<li>request scope: " +  beanForRequest + "</li>" +
                "<li>session scope: " +  beanForSession + "</li>" +
                "<li>application scope: " +  beanForApplication + "</li>" +
                "</ul>";
    }
}
