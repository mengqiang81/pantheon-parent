package com.alibaba.mos;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Map;

@Service
public class RpcTestServiceImpl implements RpcTestService {
    @Override
    public void t1() {

    }

    @Override
    public void t2(String name) {

    }

    @Override
    public void t3(Point name) {

    }

    @Override
    public void t4(String firstName, Point name) {

    }

    @Override
    public void t5(String firstName, String lastName) {

    }

    @Override
    public void t6(List<String> args) {

    }

    @Override
    public void t7(List<Point> args) {

    }

    @Override
    public void t8(String name, List<String> args) {

    }

    @Override
    public void t9(String name, List<Point> args) {

    }

    @Override
    public void t10(Map<String, String> group) {

    }

    @Override
    public void t11(Map<String, Point> group) {

    }

    @Override
    public void t12(String name, Map<String, String> group) {

    }

    @Override
    public void t13(String name, Map<String, Point> group) {

    }

    @Override
    public Point t14() {
        return new Point(1,1);
    }

    @Override
    public List<Point> t15() {
        return List.of(new Point(1,1));
    }

    @Override
    public Map<String, Point> t16() {
        return Map.of("1_1", new Point(1,1));
    }
}
