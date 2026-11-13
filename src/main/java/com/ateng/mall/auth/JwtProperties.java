package com.ateng.mall.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mall.jwt")
public class JwtProperties {

    private String secret;
    private long expireSeconds = 86400;
}
