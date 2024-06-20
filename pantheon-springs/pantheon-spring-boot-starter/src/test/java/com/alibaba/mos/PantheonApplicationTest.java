package com.alibaba.mos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = {"local","local-test"})
class PantheonApplicationTest {

    @Test
    void contextLoads() {
    }

}