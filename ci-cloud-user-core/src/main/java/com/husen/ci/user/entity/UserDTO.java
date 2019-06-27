package com.husen.ci.user.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

/***
 @Author:MrHuang
 @Date: 2019/6/4 18:15
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
@Document(collection = "user")
public class UserDTO implements Serializable {

    /**
     * 自动映射_id属性，每一个model都必须要一个这个
     */
    @Id
    private String userId;

    /**
     * 类似更新标识的作用，防止脏查
     */
    @Version
    private Long version;

    @Field(value = "userName")
    private String userName;

    @Field(value = "userStatus")
    private Integer userStatus;

    @Field(value = "userCreateTime")
    private LocalDateTime userCreateTime;

    @Field(value = "userActiveTime")
    private LocalDateTime userActiveTime;

    @Field
    private String host;
}
