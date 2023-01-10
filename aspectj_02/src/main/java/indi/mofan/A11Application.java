package indi.mofan;

import indi.mofan.service.MyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class A11Application {

    /*
    * 启动时需要添加 JVM 参数：-javaagent:D:\environment\Maven\3.6.3-repository\.m2\repository\org\aspectj\aspectjweaver\1.9.7\aspectjweaver-1.9.7.jar
    * 其中的 D:\environment\Maven\3.6.3-repository\.m2 指本地 Maven 仓库地址
    * 除此之外，需要确保本地仓库中存在 1.9.7 版本的 aspectjweaver，否则修改至对应版本
    * */

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A11Application.class, args);
        MyService service = context.getBean(MyService.class);
        log.info("service class: {}", service.getClass());
        service.foo();
    }

}
