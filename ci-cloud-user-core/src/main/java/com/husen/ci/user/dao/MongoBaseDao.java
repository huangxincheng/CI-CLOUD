package com.husen.ci.user.dao;

import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Map;

/***
 @Author:MrHuang
 @Date: 2019/7/17 18:04
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Repository
public class  MongoBaseDao<T, F> {

    @Autowired
    private MongoTemplate mongoTemplate;

    public T findById(F id, Class<T> clazz) {
        return mongoTemplate.findById(id, clazz);
    }

    public UpdateResult updateById(Map<String, Object> map, Class<T> clazz) {
        Update update = new Update();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!"_id".equals(entry.getKey())) {
                update.set(entry.getKey(), entry.getValue());
            }
        }
        return mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(map.get("_id"))), update, clazz);
    }
}
