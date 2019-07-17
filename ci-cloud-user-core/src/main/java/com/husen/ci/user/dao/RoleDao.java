package com.husen.ci.user.dao;

import com.husen.ci.user.entity.RoleDTO;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/***
 @Author:MrHuang
 @Date: 2019/7/17 16:22
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Repository
public class RoleDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public RoleDTO add(RoleDTO roleDTO) {
        return mongoTemplate.insert(roleDTO);
    }

    public RoleDTO getOneByRid(String rid) {
        return mongoTemplate.findById(rid, RoleDTO.class);
    }

    public RoleDTO getOneByRname(String rname) {
        return mongoTemplate.findOne(Query.query(Criteria.where("rname").is(rname)), RoleDTO.class);
    }

    public UpdateResult updateRoleInfo(RoleDTO roleDTO) {
        return mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(roleDTO.getRid())),
                Update.update("rname", roleDTO.getRname())
                        .set("status", roleDTO.getStatus()),
                RoleDTO.class);
    }

    public UpdateResult updateRoleBindMenu(RoleDTO roleDTO) {
        return mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(roleDTO.getRid())),
                Update.update("bindMenuIds", roleDTO.getBindMenuIds()),
                RoleDTO.class);
    }



}

