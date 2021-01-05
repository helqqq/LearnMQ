package com.helq3.dto;

import lombok.Data;

/**
 * @ClassName Person
 * @Description 人员实体类
 * @Author Helena
 * @Date 2020/12/19 13:15
 */
@Data
public class Person {
    private Integer id;
    private String name;
    private String code;
    private String address;

    public Person() {
    }

    public Person(Integer id, String name, String code, String address) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.address = address;
    }
}
