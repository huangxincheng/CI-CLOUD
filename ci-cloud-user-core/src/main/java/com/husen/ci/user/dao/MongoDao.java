package com.husen.ci.user.dao;

import com.husen.ci.framework.utils.BeanUtils;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/***
 @Author:MrHuang
 @Date: 2019/7/17 18:04
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Repository
public class MongoDao<T, F> {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 泛型T的Class类型
     */
    private Class<T> clazzT;

    /**
     * 泛型F的Class类型
     */
    private Class<F> clazzF;

    /**
     * 写入泛型Class类型
     */
    private void writeClassType() {
        clazzT = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        clazzF = (Class<F>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public T insert(T t) {
        return mongoTemplate.insert(t);
    }

    /**
     * 根据Id查询
     * @param id
     * @param clazz
     * @return
     */
    public T findById(F id, Class<T> clazz) {
        return mongoTemplate.findById(id, clazz);
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public T findById(F id) {
        writeClassType();
        return mongoTemplate.findById(id, clazzT);
    }

    /**
     * 根据Query查询一条记录
     * @param query
     * @param clazz
     * @return
     */
    public T findOneByQuery(Query query, Class<T> clazz) {
        return mongoTemplate.findOne(query, clazz);
    }

    /**
     * 根据Query查询一条记录
     * @param query
     * @return
     */
    public T findOneByQuery(Query query) {
        writeClassType();
        return mongoTemplate.findOne(query, clazzT);
    }

    /**
     * 根据Query查询多条记录
     * @param query
     * @param clazz
     * @return
     */
    public List<T> findByQuery(Query query, Class<T> clazz) {
        return mongoTemplate.find(query, clazz);
    }

    /**
     * 根据Query查询多条记录
     * @param query
     * @return
     */
    public List<T> findByQuery(Query query) {
        writeClassType();
        return mongoTemplate.find(query, clazzT);
    }

    /**
     * 查询所有记录
     * @param clazz
     * @return
     */
    public List<T> findAll(Class<T> clazz) {
        return mongoTemplate.findAll(clazz);
    }

    /**
     * 查询所有记录
     * @return
     */
    public List<T> findAll() {
        writeClassType();
        return mongoTemplate.findAll(clazzT);
    }

    /**
     * 根据ID更新记录
     * @param map
     * @param clazz
     * @return
     */
    public boolean updateById(Map<String, Object> map, Class<T> clazz) {
        Update update = new Update();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            if (entry.getValue() == null) {
                continue;
            }
            if (!"_id".equals(entry.getKey())) {
                update.set(entry.getKey(), entry.getValue());
            }
        }
        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(map.get("_id"))), update, clazz);
        return updateResult.getModifiedCount() >= 0;
    }


    /**
     * 根据ID更新记录
     * @param t T
     * @return
     */
    public boolean updateById(T t) {
        writeClassType();
        Map<String, Object> map = BeanUtils.bean2Map(t);
        String containIdKey = null;
        Object containIdValue = null;
        for (Field field : clazzT.getDeclaredFields()) {
            field.setAccessible(true);
            Id id = field.getDeclaredAnnotation(Id.class);
            if (id != null) {
                try {
                    containIdKey = field.getName();
                    containIdValue = field.get(t);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
        map.remove(containIdKey);
        map.put("_id", containIdValue);
        return updateById(map, (Class<T>) t.getClass());
    }

    /**
     * 根据Query更新单条记录
     * @param query
     * @param update
     * @param clazz
     * @return
     */
    public boolean updateFirst(Query query, Update update, Class<T> clazz) {
        return mongoTemplate.updateFirst(query, update, clazz).getModifiedCount() >= 0;
    }

    /**
     * 根据Query更新单条记录
     * @param query
     * @param update
     * @return
     */
    public boolean updateFirst(Query query, Update update) {
        writeClassType();
        return mongoTemplate.updateFirst(query, update, clazzT).getModifiedCount() >= 0;
    }

    /**
     * 根据Query更新多条记录
     * @param query
     * @param update
     * @param clazz
     * @return
     */
    public boolean updateMulti(Query query, Update update, Class<T> clazz) {
        return mongoTemplate.updateMulti(query, update, clazz).getModifiedCount() >= 0;
    }

    /**
     * 根据Query更新多条记录
     * @param query
     * @param update
     * @return
     */
    public boolean updateMulti(Query query, Update update) {
        writeClassType();
        return mongoTemplate.updateMulti(query, update, clazzT).getModifiedCount() >= 0;
    }

    /**
     * 根据ID删除记录
     * @param id
     * @param clazz
     * @return
     */
    public boolean deleteById(F id, Class<T> clazz) {
        return mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), clazz).getDeletedCount() >= 0;
    }


    /**
     * 根据ID删除记录
     * @param id
     * @return
     */
    public boolean deleteById(F id) {
        writeClassType();
        return mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), clazzT).getDeletedCount() >= 0;
    }

    /**
     * 更新Query删除记录
     * @param query
     * @param clazz
     * @return
     */
    public boolean deleteByQuery(Query query, Class<T> clazz) {
        return mongoTemplate.remove(query, clazz).getDeletedCount() >= 0;
    }

    /**
     * 更新Query删除记录
     * @param query
     * @return
     */
    public boolean deleteByQuery(Query query) {
        writeClassType();
        return mongoTemplate.remove(query, clazzT).getDeletedCount() >= 0;
    }
}
