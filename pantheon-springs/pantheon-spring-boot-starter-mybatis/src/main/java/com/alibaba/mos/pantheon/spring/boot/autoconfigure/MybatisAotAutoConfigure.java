package com.alibaba.mos.pantheon.spring.boot.autoconfigure;

import com.alibaba.mos.pantheon.extend.mybatis.spring.aot.MybatisMapperFactoryBeanPostProcessor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(
        proxyBeanMethods = false
)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
public class MybatisAotAutoConfigure {

    @Bean
    public MybatisMapperFactoryBeanPostProcessor mapperFactoryBeanPostProcessor() {
        return new MybatisMapperFactoryBeanPostProcessor();
    }

}
