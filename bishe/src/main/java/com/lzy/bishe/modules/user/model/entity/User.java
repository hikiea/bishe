package com.lzy.springbootjwtcaptcha.modules.user.model.entity;

import lombok.Data;


/**
 * @author lizhongyi
 */
@Data
public class User {

    private Integer id;

    private String username;

    private String password;

    private String power;

}
