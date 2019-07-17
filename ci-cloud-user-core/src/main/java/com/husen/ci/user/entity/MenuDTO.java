package com.husen.ci.user.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/***
 @Author:MrHuang
 @Date: 2019/7/17 16:16
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
@Document(collection = "menu")
public class MenuDTO implements Serializable {

    @Id
    private String mid;

    @Field
    private String mname;

    @Field
    private String parentid;

    @Field
    private int status;
}
