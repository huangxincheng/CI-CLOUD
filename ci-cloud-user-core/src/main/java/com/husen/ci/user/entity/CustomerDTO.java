package com.husen.ci.user.entity;

import com.husen.ci.framework.net.bean.HttpResult;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/***
 @Author:MrHuang
 @Date: 2019/7/17 16:26
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Data
@Accessors(chain = true)
@Document(collection = "customer")
public class CustomerDTO {

    @Id
    private String cid;

    @Field
    private String cname;

    @Field
    private int status;

    @Field
    private List<String> bindRoleIds;

    @Field
    private List<HttpResult> bindResult;
}
