package indi.mofan.bean.a08;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author mofan
 * @date 2023/1/8 17:59
 */
@SpringBootApplication
public class A08Application {

    public static void main(String[] args) {
        SpringApplication.run(A08Application.class, args);
    }

    @Bean(initMethod = "init")
    public DruidDataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/advanced_spring");
        dataSource.setName("root");
        dataSource.setPassword("123456");
        return dataSource;
    }
}
