package com.alibaba.mos.pantheon.extend.cache.spring;

import org.springframework.aot.hint.annotation.Reflective;
import org.springframework.cache.annotation.CachePut;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Reflective
@CachePut
public @interface CachePutExt {
    @AliasFor(annotation = CachePut.class,attribute = "value")
    String[] value() default {};

    @AliasFor(annotation = CachePut.class,attribute = "cacheNames")
    String[] cacheNames() default {};

    @AliasFor(annotation = CachePut.class,attribute = "key")
    String key() default "";

    @AliasFor(annotation = CachePut.class,attribute = "keyGenerator")
    String keyGenerator() default "";

    @AliasFor(annotation = CachePut.class,attribute = "cacheManager")
    String cacheManager() default "";

    @AliasFor(annotation = CachePut.class,attribute = "condition")
    String cacheResolver() default "";

    @AliasFor(annotation = CachePut.class,attribute = "condition")
    String condition() default "";

    @AliasFor(annotation = CachePut.class,attribute = "unless")
    String unless() default "";
}
