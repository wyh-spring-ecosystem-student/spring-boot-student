package com.xiaolyuh;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class LocalDateTimeTest {
    public static void main(String[] args) {
        // 获取当前时间
        System.out.println(LocalDateTime.now());
        // 格式化时间
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        // 设置时间
        System.out.println(LocalDateTime.now().withYear(2019).withMonth(12));
        // 指定时间初始化
        System.out.println(LocalDateTime.of(2020, 3, 24, 15, 20));
        // 获取年月日
        System.out.println(LocalDateTime.now().getYear());
        System.out.println(LocalDateTime.now().getMonth().getValue());
        System.out.println(LocalDateTime.now().getDayOfMonth());
        System.out.println(LocalDateTime.now().getDayOfWeek().getValue());
        System.out.println(LocalDateTime.now().getDayOfYear());
        System.out.println(LocalDateTime.now().getHour());
        System.out.println(LocalDateTime.now().getMinute());
        System.out.println(LocalDateTime.now().getSecond());

        // 时间计算
        System.out.println(LocalDateTime.now().plusDays(1));
        System.out.println(LocalDateTime.now().minusDays(1));

        // 获取今年第一天 明年第一天
        System.out.println(LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear()));
        System.out.println(LocalDateTime.now().with(TemporalAdjusters.firstDayOfNextYear()));

        // 获取今年第一天 0点
        System.out.println(LocalDateTime.of(LocalDate.now(), LocalTime.MIN).with(TemporalAdjusters.firstDayOfYear()));

        // 获取当天0点和24点
        System.out.println(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        System.out.println(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));

        System.out.println(LocalDate.now());
        System.out.println(LocalTime.now());
        System.out.println(Instant.now());
    }


}
