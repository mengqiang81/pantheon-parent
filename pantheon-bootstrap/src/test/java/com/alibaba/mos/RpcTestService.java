package com.alibaba.mos;

import com.alibaba.mos.pantheon.rpc.api.Param;
import com.alibaba.mos.pantheon.rpc.api.Rpc;

import java.awt.*;
import java.util.List;
import java.util.Map;

@Rpc("ex")
public interface RpcTestService {
    void t1();

    void t2(String name);

    void t3(Point name);

    void t4(@Param("a") String firstName, @Param("b") Point lastName);

    void t5(@Param("a") String firstName, @Param("b") String lastName);

    void t6(List<String> args);

    void t7(List<Point> args);

    void t8(@Param("a") String name, @Param("b") List<String> args);

    void t9(@Param("a") String name, @Param("b") List<Point> args);

    void t10(Map<String, String> group);

    void t11(Map<String, Point> group);

    void t12(@Param("a") String name, @Param("b") Map<String, String> group);

    void t13(@Param("a") String name, @Param("b") Map<String, Point> group);

    Point t14();

    List<Point> t15();

    Map<String, Point> t16();
}
