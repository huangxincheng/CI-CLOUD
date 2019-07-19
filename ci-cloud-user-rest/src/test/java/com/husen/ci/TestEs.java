package com.husen.ci;

import com.husen.ci.framework.json.JSONUtils;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/***
 @Author:MrHuang
 @Date: 2019/7/19 15:53
 @DESC: TODO
 @VERSION: 1.0
 ***/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestEs {

    @Autowired
    private EsBeanDao esBeanDao;

    @Test
    public void test() throws IOException {
        String uuid = UUID.randomUUID().toString();
        EsBean bean = new EsBean().setId(uuid).setBeanName("BeanName:" + uuid).setBeanTitle("BeanTitle:" + uuid).setBeanSize(uuid.length());
        IndexResponse index = esBeanDao.index(bean);
        System.out.println("INDEX:" + index);
        System.out.println("GET: " + esBeanDao.get(uuid));
        System.out.println("GETTOBEAN: " + esBeanDao.getToBean(uuid));
        bean.setBeanSize(1233333);
        System.out.println("UPDATE:" + esBeanDao.update(bean));
        System.out.println("GET: " + esBeanDao.get(uuid));
        System.out.println("DELETE:" + esBeanDao.delete(uuid));
        System.out.println("GET: " + esBeanDao.get(uuid));
        MultiGetResponse mget = esBeanDao.multiGet(
                new String[]{"6d734eea-bcf1-4eca-a64b-6fd5a20b40ee", "046d7cd3-28e6-4cc5-b2a0-b6ac2c07e388",
                        "5b9c901a-606c-4602-b93d-acf8fbaa94fc", "123123123213"});
        System.out.println("MGET:" + JSONUtils.object2Json(mget));
        Map<String, EsBean> mgetMap = esBeanDao.multiGetToMap(
                new String[]{"6d734eea-bcf1-4eca-a64b-6fd5a20b40ee", "046d7cd3-28e6-4cc5-b2a0-b6ac2c07e388",
                        "5b9c901a-606c-4602-b93d-acf8fbaa94fc", "123123123213"});
        System.out.println("MGETMAP:" + JSONUtils.object2Json(mgetMap));

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(20);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        boolQueryBuilder.must(matchAllQueryBuilder);
        SearchSourceBuilder searchSourceBuilder = sourceBuilder.query(boolQueryBuilder);
        SearchResponse SEARCH = esBeanDao.search(searchSourceBuilder);
        System.out.println("SEARCH:" + JSONUtils.object2Json(SEARCH));

        List<EsBean> SEARCHLIST = esBeanDao.searchToList(searchSourceBuilder);
        System.out.println("SEARCHLIST:" + JSONUtils.object2Json(SEARCHLIST));

        Map<String, EsBean> SEARCHMAP = esBeanDao.searchToMap(searchSourceBuilder);
        System.out.println("SEARCHMAP:" + JSONUtils.object2Json(SEARCHMAP));

    }
}
