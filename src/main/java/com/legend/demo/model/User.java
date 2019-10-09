package com.legend.demo.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
@Data
public class User {

    private Integer uid;
    private String username;
    private String password;
}