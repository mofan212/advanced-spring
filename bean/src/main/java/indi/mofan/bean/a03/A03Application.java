package indi.mofan.bean.a03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author mofan
 * @date 2022/12/24 17:42
 */
@SpringBootApplication
public class A03Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A03Application.class, args);
        // 调用 close 方法，显示生命周期的销毁阶段
        context.close();
    }
}
