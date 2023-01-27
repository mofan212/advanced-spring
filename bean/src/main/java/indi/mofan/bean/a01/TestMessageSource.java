package indi.mofan.bean.a01;

import org.springframework.context.MessageSource;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author mofan
 * @date 2023/1/27 13:26
 */
public class TestMessageSource {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();

        context.registerBean("messageSource", MessageSource.class, () -> {
            ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
            // 设置编码格式
            ms.setDefaultEncoding("utf-8");
            // 设置国际化资源文件的 basename
            ms.setBasename("messages");
            return ms;
        });

        context.refresh();

        System.out.println(context.getMessage("thanks", null, Locale.ENGLISH));
        System.out.println(context.getMessage("thanks", null, Locale.SIMPLIFIED_CHINESE));
        System.out.println(context.getMessage("thanks", null, Locale.TRADITIONAL_CHINESE));
    }
}
