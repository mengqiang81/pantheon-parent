package com.alibaba.mos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class PantheonApplication {

    /**
     * 配置文件加载顺序
     */
    private static final List<String> DEFAULT_CONFIG_LOAD = List.of(
            "optional:file:./",
            "optional:file:./mos",
            "optional:file:./mos/*",
            "optional:file:./pantheon",
            "optional:file:./pantheon/*",
            "optional:file:./config/",
            "optional:file:./config/*",
            "optional:classpath:/mos/",
            "optional:classpath:/pantheon/",
            "optional:classpath:/",
            "optional:classpath:/config/"
    );

    private static final Map<String, Object> DEFAULT_CONFIG = Map.of(
            "spring.config.location", String.join(";", DEFAULT_CONFIG_LOAD)
    );

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PantheonApplication.class);
        app.setDefaultProperties(DEFAULT_CONFIG);
        app.run(args);
    }

}
