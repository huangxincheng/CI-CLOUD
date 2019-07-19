package com.husen.ci.es;

import com.husen.ci.framework.json.JSONUtils;
import com.husen.ci.framework.utils.BeanUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/***
 @Author:MrHuang
 @Date: 2019/7/19 11:02
 @DESC: TODO
 @VERSION: 1.0
 ***/
@Repository
public abstract class ElasticSearchDao<T> {

    /**
     * 泛型T的Class类型
     */
    private Class<T> clazzT;


    private ElasticSearchClient esClient = ElasticSearchClient.getInstance();

    /**
     * 写入泛型Class类型
     */
    private void writeClassType() {
        clazzT = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 创建Dom
     * ES 7.1的语法
     * POST /{index}/_doc
     * {
     *   "name":"456"
     * }
     *
     * POST /${index}/_doc/${_id}
     *{
     *   "name":"456"
     * }
     * @param object
     * @return
     * @throws IOException
     */
    public IndexResponse index(T object) throws IOException {
        writeClassType();
        String index = esClient.getIndex(clazzT);
        String domId = esClient.getDomId(object);
        Assert.notNull(index, "domId must not be null!");
        IndexRequest request = new IndexRequest(index);
        request.id(domId);
        request.source(JSONUtils.object2Json(object), XContentType.JSON);
        IndexResponse rsp = esClient.restClient.index(request, RequestOptions.DEFAULT);
        return rsp;
    }

    /**
     * 更新Dom
     * ES 7.1的语法
     * POST /${index}/_update/${_id}
     * {
     *   "doc":{
     *      "name":"sss"
     *   }
     * }
     * @param object
     * @return
     * @throws IOException
     */
    public UpdateResponse update(T object) throws IOException {
        writeClassType();
        String index = esClient.getIndex(clazzT);
        String domId = esClient.getDomId(object);
        Assert.notNull(domId, "domId must not be null!");
        UpdateRequest request = new UpdateRequest(index, domId);
        request.doc(BeanUtils.bean2BeanMap(object));
        UpdateResponse rsp = esClient.restClient.update(request, RequestOptions.DEFAULT);
        return rsp;
    }


    /**
     * 删除Dom
     * ES 7.1 根据DomId删除单个Index
     * DELETE /${index}/_doc/${_id}
     * @param domId
     * @return
     * @throws IOException
     */
    public DeleteResponse delete(String domId) throws IOException {
        writeClassType();
        String index = esClient.getIndex(clazzT);
        Assert.notNull(domId, "domId must not be null!");
        DeleteRequest request = new DeleteRequest(index, domId);
        DeleteResponse rsp = esClient.restClient.delete(request, RequestOptions.DEFAULT);
        return rsp;
    }

    /**
     * 根据DomId获取单个Dom
     * ES 7.1的语法
     * GET /${index}/_doc/${_id}
     * @param domId
     * @return
     */
    public GetResponse get(String domId) throws IOException {
        writeClassType();
        String index = esClient.getIndex(clazzT);
        Assert.notNull(domId, "domId must not be null!");
        GetRequest request = new GetRequest(index, domId);
        GetResponse rsp = esClient.restClient.get(request, RequestOptions.DEFAULT);
        return rsp;
    }


    /**
     * 批量获取Dom
     * ES 7.1的语法
     * GET /_mget
     * {
     *   "docs":[
     *     {"_index":"dto_es","_id":"y92qBGwBQn3QETkmXOaX"},
     *      {"_index":"dto_es","_id":"5188"}
     *   ]
     * }
     * @param domIds
     * @return
     */
    public MultiGetResponse mget(String[] domIds) throws IOException {
        writeClassType();
        String index = esClient.getIndex(clazzT);
        Assert.notNull(domIds, "domIds must not be null!");
        MultiGetRequest request = new MultiGetRequest();
        for (String domId : domIds) {
            request.add(index, domId);
        }
        MultiGetResponse rsp = esClient.restClient.mget(request, RequestOptions.DEFAULT);
        return rsp;
    }

    /**
     * 批量Index Dom
     * ES 7.1的语法
     * POST /_bulk
     * { "index":  { "_index": "test_index",  "_id": "2" }}
     * { "test_field":    "replaced test2" }
     * { "index":  { "_index": "test_index",  "_id": "3" }}
     * { "test_field":    "replaced test3" }
     *
     * @param ts
     * @return
     * @throws IOException
     */
    public BulkResponse bulkIndex(T [] ts) throws IOException {
        writeClassType();
        String index = esClient.getIndex(clazzT);
        Assert.notNull(ts, "ts must not be null!");
        BulkRequest request = new BulkRequest();
        for (T t : ts) {
            String domId = esClient.getDomId(t);
            Assert.notNull(domId, "domId must not be null!");
            request.add(new IndexRequest(index).id(domId).source(JSONUtils.object2Json(t), XContentType.JSON));
        }
        BulkResponse rsp = esClient.restClient.bulk(request, RequestOptions.DEFAULT);
        return rsp;
    }

    /**
     * 批量删除Dom
     * ES 7.1的语法
     * POST /_bulk
     * { "delete": { "_index": "test_index", "_id": "3" }}
     * { "delete": { "_index": "test_index", "_id": "4" }}
     * @param domIds
     * @return
     * @throws IOException
     */
    public BulkResponse bulkDelete(String [] domIds) throws IOException {
        writeClassType();
        String index = esClient.getIndex(clazzT);
        Assert.notNull(domIds, "domIds must not be null!");
        BulkRequest request = new BulkRequest();
        for (String domId : domIds) {
            Assert.notNull(domId, "domId must not be null!");
            request.add(new DeleteRequest(index, domId));
        }
        BulkResponse rsp = esClient.restClient.bulk(request, RequestOptions.DEFAULT);
        return rsp;
    }

    /**
     * 批量Update Dom
     * ES 7.1的语法
     * POST /_bulk
     * { "update": { "_index": "test_index", "_id": "1"} }
     * { "doc" : {"test_field2" : "bulk test1"} }
     * { "update": { "_index": "test_index", "_id": "2"} }
     * { "doc" : {"test_field2" : "bulk test2"} }
     * @param ts
     * @return
     * @throws IOException
     */
    public BulkResponse bulkUpdate(T [] ts) throws IOException {
        writeClassType();
        String index = esClient.getIndex(clazzT);
        Assert.notNull(ts, "ts must not be null!");
        BulkRequest request = new BulkRequest();
        for (T t : ts) {
            String domId = esClient.getDomId(t);
            Assert.notNull(domId, "domId must not be null!");
            request.add(new UpdateRequest(index, domId).doc(BeanUtils.bean2BeanMap(t)));
        }
        BulkResponse rsp = esClient.restClient.bulk(request, RequestOptions.DEFAULT);
        return rsp;
    }

    /**
     * Search
     * ES 7.1的语法
     * @param sourceBuilder
     * @return
     * @throws IOException
     */
    public SearchResponse search(SearchSourceBuilder sourceBuilder) throws IOException {
        writeClassType();
        String index = esClient.getIndex(clazzT);
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.from(0);
//        sourceBuilder.size(10);
//        sourceBuilder.fetchSource(new String[]{"title"}, new String[]{});
//        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "费德勒");
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("tag", "体育");
//        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishTime");
//        rangeQueryBuilder.gte("2018-01-26T08:00:00Z");
//        rangeQueryBuilder.lte("2018-01-26T20:00:00Z");
//        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
//        boolBuilder.must(matchQueryBuilder);
//        boolBuilder.must(termQueryBuilder);
//        boolBuilder.must(rangeQueryBuilder);
//        sourceBuilder.query(boolBuilder);
        SearchRequest request = new SearchRequest(index).source(sourceBuilder);
        SearchResponse rsp = esClient.restClient.search(request, RequestOptions.DEFAULT);
        return rsp;
    }



    /**
     * execute 通用执行入口
     * @param method
     * @param endpoint
     * @param entity
     * @return
     * @throws IOException
     */
    public Response execute(String method, String endpoint, String entity) throws IOException {
        return esClient.execute(method, endpoint, entity);
    }
}
