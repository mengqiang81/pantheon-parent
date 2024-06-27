package com.alibaba.mos;

import com.alibaba.mos.pantheon.extend.rpc.Resp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = {"local", "local-test"})
class RpcTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @SuppressWarnings("rawtypes")
    @Test
    void testInvoke() throws Exception {
        String baseUrl = "http://localhost:" + port + "/api/ex/";
        List<String> listStrParam = new ArrayList<>();
        listStrParam.add("1");
        List<Point> listPointParam = new ArrayList<>();
        listPointParam.add(new Point(1,1));
        Map<String, Object> mapParam = new HashMap<>();
        mapParam.put("a", "a");

        Resp resp = restTemplate.postForObject(baseUrl + "t1", null, Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNull(resp.getData());


        resp = restTemplate.postForObject(baseUrl + "t2", "null", Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNull(resp.getData());


        resp = restTemplate.postForObject(baseUrl + "t3", new Point(1,1), Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNull(resp.getData());


        mapParam.put("b", new Point(1,1));
        resp = restTemplate.postForObject(baseUrl + "t4", mapParam, Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNull(resp.getData());

        mapParam.put("b", "b");
        resp = restTemplate.postForObject(baseUrl + "t5", mapParam, Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNull(resp.getData());

        resp = restTemplate.postForObject(baseUrl + "t6", listStrParam, Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNull(resp.getData());

        resp = restTemplate.postForObject(baseUrl + "t7", listPointParam, Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNull(resp.getData());

        mapParam.put("b", listStrParam);
        resp = restTemplate.postForObject(baseUrl + "t8", mapParam, Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNull(resp.getData());

        mapParam.put("b", listPointParam);
        resp = restTemplate.postForObject(baseUrl + "t9", mapParam, Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNull(resp.getData());

        resp = restTemplate.postForObject(baseUrl + "t10", Map.of("1","1"), Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNull(resp.getData());

        resp = restTemplate.postForObject(baseUrl + "t11", Map.of("1",new Point(1,1)), Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNull(resp.getData());

        mapParam.put("b", Map.of("1",new Point(1,1)));
        resp = restTemplate.postForObject(baseUrl + "t12", mapParam, Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNull(resp.getData());

        mapParam.put("b", Map.of("1",new Point(1,1)));
        resp = restTemplate.postForObject(baseUrl + "t13", mapParam, Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNull(resp.getData());

        resp = restTemplate.postForObject(baseUrl + "t14", null, Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNotNull(resp.getData());


        resp = restTemplate.postForObject(baseUrl + "t15", null, Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNotNull(resp.getData());

        resp = restTemplate.postForObject(baseUrl + "t16", null, Resp.class);
        Assertions.assertEquals(resp.getCode(),"ok");
        Assertions.assertNotNull(resp.getData());

        resp = restTemplate.postForObject(baseUrl, null, Resp.class);
        Assertions.assertEquals(resp.getCode(),"api-error");

        resp = restTemplate.postForObject(baseUrl + "s1", null, Resp.class);
        Assertions.assertEquals(resp.getCode(),"method-off");
    }

}