package com.xiaolyuh.transaction.entity;

import lombok.*;

/**
 * @author yuhao.wang3
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private Long id;

    private String name;

    private Integer age;

    private String address;
}
