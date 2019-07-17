package com.husen.ci.user.dao;

import com.husen.ci.user.entity.MenuDTO;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/***
 @Author:MrHuang
 @Date: 2019/7/17 17:11
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Repository
public class MenuDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public MenuDTO add(MenuDTO menuDTO) {
        return mongoTemplate.insert(menuDTO);
    }

    public MenuDTO getOneByMid(String mid) {
        return mongoTemplate.findById(mid, MenuDTO.class);
    }

    public UpdateResult updateMenuInfo(MenuDTO menuDTO) {
        return mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(menuDTO.getMid())),
                Update.update("mname", menuDTO.getMname())
                        .set("parentid", menuDTO.getParentid())
                        .set("status", menuDTO.getStatus()),
                MenuDTO.class);
    }
}
