package com.xiaolyuh;

import com.xiaolyuh.domain.model.Order;
import com.xiaolyuh.domain.model.Person;
import com.xiaolyuh.utils.DroolsUtil;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DroolsScoreExampleTest {


    /**
     * 计算额外积分金额 规则如下: 订单原价金额
     * 100以下, 不加分
     * 100-500 加100分
     * 500-1000 加500分
     * 1000 以上 加1000分
     *
     * @param args
     * @throws Exception
     */
    public static final void main(final String[] args) throws Exception {
        // 通过工具类去获取KieSession
        KieSession ksession = DroolsUtil.getKieSessionByName("point_ksession");

        List<Order> orderList = getInitData();
        try {
            for (int i = 0; i < orderList.size(); i++) {
                Order o = orderList.get(i);
                ksession.insert(o);
                ksession.fireAllRules();
                // 执行完规则后, 执行相关的逻辑
                addScore(o);
            }
        } catch (Exception e) {

        } finally {
            ksession.destroy();
        }

    }


    private static void addScore(Order o) {
        System.out.println("用户" + o.getPerson().getName() + "享受额外增加积分: " + o.getScore());
    }

    private static List<Order> getInitData() throws Exception {
        List<Order> orderList = new ArrayList<Order>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        {
            Order order = new Order();
            order.setAmout(80);
            order.setBookingDate(df.parse("2015-07-01"));
            Person user = new Person();
            user.setLevel(1);
            user.setName("Name1");
            order.setPerson(user);
            order.setScore(111);
            orderList.add(order);
        }
        {
            Order order = new Order();
            order.setAmout(200);
            order.setBookingDate(df.parse("2015-07-02"));
            Person user = new Person();
            user.setLevel(2);
            user.setName("Name2");
            order.setPerson(user);
            orderList.add(order);
        }
        {
            Order order = new Order();
            order.setAmout(800);
            order.setBookingDate(df.parse("2015-07-03"));
            Person user = new Person();
            user.setLevel(3);
            user.setName("Name3");
            order.setPerson(user);
            orderList.add(order);
        }
        {
            Order order = new Order();
            order.setAmout(1500);
            order.setBookingDate(df.parse("2015-07-04"));
            Person user = new Person();
            user.setLevel(4);
            user.setName("Name4");
            order.setPerson(user);
            orderList.add(order);
        }
        return orderList;
    }
}  