package org.springframework.boot;

import org.springframework.core.SpringVersion;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.io.DefaultResourceLoader;

import java.util.Collections;

/**
 * @author mofan
 * @date 2023/1/28 17:46
 */
public class Step7 {
    public static void main(String[] args) {
        ApplicationEnvironment env = new ApplicationEnvironment();
        SpringApplicationBannerPrinter printer = new SpringApplicationBannerPrinter(
                new DefaultResourceLoader(),
                new SpringBootBanner()
        );

        // 测试文字 banner
        env.getPropertySources().addLast(new MapPropertySource(
                "custom",
                Collections.singletonMap("spring.banner.location", "banner1.txt")
        ));
        // 测试图片 banner
//        env.getPropertySources().addLast(new MapPropertySource(
//                "custom",
//                Collections.singletonMap("spring.banner.image.location", "banner2.gif")
//        ));
        // 版本号的获取
        System.out.println("SpringBoot: " + SpringBootVersion.getVersion());
        System.out.println("Spring: " + SpringVersion.getVersion());
        printer.print(env, Step7.class, System.out);
    }
}
