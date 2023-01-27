package indi.mofan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author mofan
 * @date 2023/1/27 17:43
 */
@Controller
public class HelloController {
    @RequestMapping(value = "/hello")
    public String hello() {
        return "hello";
    }
}
