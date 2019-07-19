package com.husen.ci;

import com.husen.ci.es.ElasticSearchDao;
import org.springframework.stereotype.Repository;

/***
 @Author:MrHuang
 @Date: 2019/7/19 15:54
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Repository
public class EsBeanDao extends ElasticSearchDao<EsBean> {
}
