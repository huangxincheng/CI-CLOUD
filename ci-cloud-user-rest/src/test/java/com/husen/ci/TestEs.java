package com.husen.ci;

import com.husen.ci.es.ElasticSearchPage;
import com.husen.ci.framework.json.JSONUtils;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

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
    public void test() throws Exception {
//        String uuid = UUID.randomUUID().toString();
//        EsBean bean = new EsBean().setId(uuid).setBeanName("BeanName:" + uuid).setBeanTitle("BeanTitle:" + uuid).setBeanSize(uuid.length());
//        IndexResponse index = esBeanDao.index(bean);
//        System.out.println("INDEX:" + index);
//        System.out.println("GET: " + esBeanDao.get(uuid));
//        System.out.println("GETTOBEAN: " + esBeanDao.getToBean(uuid));
//        bean.setBeanSize(1233333);
//        System.out.println("UPDATE:" + esBeanDao.update(bean));
//        System.out.println("GET: " + esBeanDao.get(uuid));
//        System.out.println("DELETE:" + esBeanDao.delete(uuid));
//        System.out.println("GET: " + esBeanDao.get(uuid));
//        MultiGetResponse mget = esBeanDao.multiGet(
//                new String[]{"6d734eea-bcf1-4eca-a64b-6fd5a20b40ee", "046d7cd3-28e6-4cc5-b2a0-b6ac2c07e388",
//                        "5b9c901a-606c-4602-b93d-acf8fbaa94fc", "123123123213"});
//        System.out.println("MGET:" + JSONUtils.object2Json(mget));
//        Map<String, EsBean> mgetMap = esBeanDao.multiGetToMap(
//                new String[]{"6d734eea-bcf1-4eca-a64b-6fd5a20b40ee", "046d7cd3-28e6-4cc5-b2a0-b6ac2c07e388",
//                        "5b9c901a-606c-4602-b93d-acf8fbaa94fc", "123123123213"});
//        System.out.println("MGETMAP:" + JSONUtils.object2Json(mgetMap));

        boolean ping = esBeanDao.ping();
        System.out.println(ping);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(20);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        boolQueryBuilder.must(matchAllQueryBuilder);
        SearchSourceBuilder searchSourceBuilder = sourceBuilder.query(boolQueryBuilder);
//        SearchResponse SEARCH = esBeanDao.search(searchSourceBuilder);
        ElasticSearchPage SEARCH = esBeanDao.searchToPage(searchSourceBuilder);
        System.out.println("SEARCH:" + JSONUtils.object2Json(SEARCH));

        Map<String, MappingMetaData> mapping = esBeanDao.getMapping();
        System.out.println(JSONUtils.object2Json(mapping));
//        List<EsBean> SEARCHLIST = esBeanDao.searchToList(searchSourceBuilder);
//        System.out.println("SEARCHLIST:" + JSONUtils.object2Json(SEARCHLIST));
//
//        Map<String, EsBean> SEARCHMAP = esBeanDao.searchToMap(searchSourceBuilder);
//        System.out.println("SEARCHMAP:" + JSONUtils.object2Json(SEARCHMAP));

//        while (true) {s
//            long i = System.currentTimeMillis();
//            Map<String, EsBean> mgetMap = esBeanDao.multiGetToMap(
//        new String[]{"6d734eea-bcf1-4eca-a64b-6fd5a20b40ee", "046d7cd3-28e6-4cc5-b2a0-b6ac2c07e388",
//                "5b9c901a-606c-4602-b93d-acf8fbaa94fc", "123123123213"});
//            System.out.println((System.currentTimeMillis() - i) + "ms    " + "MGETMAP:" + JSONUtils.object2Json(mgetMap));
//        }

    }
}
