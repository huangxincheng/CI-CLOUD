package com.husen.ci.user.dao;

import com.husen.ci.framework.cache.GuavaCache;
import com.husen.ci.user.entity.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 @Author:MrHuang
 @Date: 2019/6/4 18:16
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Repository
public class UserDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    private GuavaCache<String, UserDTO> guavaCache = GuavaCache.getInstace();

    public UserDTO findById(String userId) {
        if ("5cf7d93859c61d51708da706".equals(userId)) {
            throw new RuntimeException("TEST EXCEPTION");
        }
        return guavaCache.get(userId, () -> mongoTemplate.findById(userId, UserDTO.class));
    }


    public UserDTO findOneByName(String userName) {
       return mongoTemplate.findOne(new Query(Criteria.where("userName").is(userName)), UserDTO.class);
    }

    public List<UserDTO> getAll() {
        List<UserDTO> all = mongoTemplate.findAll(UserDTO.class);
        return all;
    }

    public boolean add(UserDTO userDTO) {
        UserDTO insert = mongoTemplate.insert(userDTO);
        return true;
    }
}
