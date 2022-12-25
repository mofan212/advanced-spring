package indi.mofan.bean.a04;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author mofan
 * @date 2022/12/25 23:09
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "java")
public class Bean4 {
    private String home;
    private String version;
}
