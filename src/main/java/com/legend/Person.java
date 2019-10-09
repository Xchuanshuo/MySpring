package com.legend;

import lombok.Data;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
@Data
public class Person {

    private String username;
    private String password;

    public Person() {}

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void test() {
        System.out.println("The first test!");
    }
}
