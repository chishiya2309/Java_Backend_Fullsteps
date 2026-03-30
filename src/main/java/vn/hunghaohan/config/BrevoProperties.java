package vn.hunghaohan.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.brevo")
@Getter
@Setter
public class BrevoProperties {
    private String apiKey;
    private String fromEmail;
    private String fromName;
    private Long templateIdEmailVerification;
    private String verificationLink;
}

