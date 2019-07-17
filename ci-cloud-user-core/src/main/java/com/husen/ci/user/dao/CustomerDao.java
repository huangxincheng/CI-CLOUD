package com.husen.ci.user.dao;

import com.husen.ci.user.entity.CustomerDTO;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/***
 @Author:MrHuang
 @Date: 2019/7/17 16:59
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Repository
public class CustomerDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public CustomerDTO add(CustomerDTO customerDTO) {
       return mongoTemplate.insert(customerDTO);
    }

    public UpdateResult updateCustomerInfo(CustomerDTO customerDTO) {
         return mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(customerDTO.getCid())),
                Update.update("cname", customerDTO.getCname())
                      .set("status", customerDTO.getStatus()),
                CustomerDTO.class);
    }

    public UpdateResult updateCustomerBindRole(CustomerDTO customerDTO) {
        return mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(customerDTO.getCid())),
                Update.update("bindRoleIds", customerDTO.getBindRoleIds()),
                CustomerDTO.class);
    }

    public CustomerDTO getOneById(String cid) {
        return mongoTemplate.findById(cid, CustomerDTO.class);
    }

    public CustomerDTO getOneByCname(String cname) {
       return  mongoTemplate.findOne(Query.query(Criteria.where("cname").is(cname)), CustomerDTO.class);
    }
}
