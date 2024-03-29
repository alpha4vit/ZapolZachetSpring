package by.gurinovich.ZapolZachetSpring.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {
    private String username;
    private String password;
    private String resetLink;
    private String host;
    private Integer port;
}
