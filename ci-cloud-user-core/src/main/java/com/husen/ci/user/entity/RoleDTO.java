package com.husen.ci.user.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

/***
 @Author:MrHuang
 @Date: 2019/7/17 16:14
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
@Document(collection = "role")
public class RoleDTO implements Serializable {

    @Id
    private String rid;

    @Field
    private String rname;

    @Field
    private List<String> bindMenuIds;

    @Field
    private int status;
}