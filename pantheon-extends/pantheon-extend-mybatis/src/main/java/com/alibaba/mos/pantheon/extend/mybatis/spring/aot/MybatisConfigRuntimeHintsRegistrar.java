package com.alibaba.mos.pantheon.extend.mybatis.spring.aot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.support.FilePatternResourceHintsRegistrar;

import java.util.List;

@Slf4j
public class MybatisConfigRuntimeHintsRegistrar implements RuntimeHintsRegistrar {

    private static final List<String> CONFIG_LOCATIONS = List.of("classpath:/mybatis/*/");
    private static final List<String> FILE_NAMES = List.of("");
    private static final List<String> EXTENSIONS = List.of(".xml");

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        log.info("Registering Mybatis configuration hints for {}({}) at {}", FILE_NAMES, EXTENSIONS, CONFIG_LOCATIONS);
        FilePatternResourceHintsRegistrar
                .forClassPathLocations(CONFIG_LOCATIONS)
                .withFilePrefixes(FILE_NAMES)
                .withFileExtensions(EXTENSIONS)
                .registerHints(hints.resources(), classLoader);
    }

}
