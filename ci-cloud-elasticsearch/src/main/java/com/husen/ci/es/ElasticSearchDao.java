package com.husen.ci.es;

import com.husen.ci.framework.json.JSONUtils;
import com.husen.ci.framework.utils.BeanUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/***
 @Author:MrHuang
 @Date: 2019/7/19 11:02
 @VERSION: 1.0
 @DESC: TODO
 ***/
@Repository
public abstract class ElasticSearchDao<T> {

    /**
     * 泛型T的Class类型
     */
    private Class<T> clazzT;

    /**
     * 默认的页起始Index
     */
    private static final int DEFAULT_PAGE_FROM = 0;

    /**
     * 默认的页码
     */
    private static final int DEFAULT_PAGE_NO = 1;

    /**
     * 默认的每页大小
     */
    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 单例的esCLient
     */
    private ElasticSearchClient esClient = ElasticSearchClient.getInstance();



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
        IndexRequest request = new IndexRequest(index).id(domId).source(JSONUtils.object2Json(object), XContentType.JSON);
        IndexResponse rsp = esClient.getRestClient().index(request, RequestOptions.DEFAULT);
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
        UpdateRequest request = new UpdateRequest(index, domId).doc(BeanUtils.bean2BeanMap(object));
        UpdateResponse rsp = esClient.getRestClient().update(request, RequestOptions.DEFAULT);
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
        DeleteResponse rsp = esClient.getRestClient().delete(request, RequestOptions.DEFAULT);
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
        GetResponse rsp = esClient.getRestClient().get(request, RequestOptions.DEFAULT);
        return rsp;
    }

    /**
     * 根据DomId获取单个Dom
     * ES 7.1的语法
     * GET /${index}/_doc/${_id}
     * @param domId
     * @return
     */
    public T getToBean(String domId) throws IOException {
        GetResponse rsp = get(domId);
        if (rsp.isExists()) {
            return JSONUtils.json2Bean(rsp.getSourceAsString(), clazzT);
        }
        return null;
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
    public MultiGetResponse multiGet(String[] domIds) throws IOException {
        writeClassType();
        String index = esClient.getIndex(clazzT);
        Assert.notNull(domIds, "domIds must not be null!");
        MultiGetRequest request = new MultiGetRequest();
        for (String domId : domIds) {
            request.add(index, domId);
        }
        MultiGetResponse rsp = esClient.getRestClient().mget(request, RequestOptions.DEFAULT);
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
    public Map<String, T> multiGetToMap(String[] domIds) throws IOException {
        MultiGetResponse rsp = multiGet(domIds);
        Iterator<MultiGetItemResponse> iterator = rsp.iterator();
        Map<String, T> map = new LinkedHashMap<>();
        while (iterator.hasNext()) {
            MultiGetItemResponse item = iterator.next();
            String domId = item.getId();
            if(item.getResponse().isExists()) {
                map.put(domId, JSONUtils.json2Bean(item.getResponse().getSourceAsString(), clazzT));
            } else {
                map.put(domId, null);
            }
        }
        return map;
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
    public List<T> multiGetToList(String[] domIds) throws IOException {
        MultiGetResponse rsp = multiGet(domIds);
        Iterator<MultiGetItemResponse> iterator = rsp.iterator();
        List<T> list = new ArrayList<>();
        while (iterator.hasNext()) {
            MultiGetItemResponse item = iterator.next();
            if(item.getResponse().isExists()) {
                list.add(JSONUtils.json2Bean(item.getResponse().getSourceAsString(), clazzT));
            }
        }
        return list;
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
        BulkResponse rsp = esClient.getRestClient().bulk(request, RequestOptions.DEFAULT);
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
        BulkResponse rsp = esClient.getRestClient().bulk(request, RequestOptions.DEFAULT);
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
        BulkResponse rsp = esClient.getRestClient().bulk(request, RequestOptions.DEFAULT);
        return rsp;
    }

    /**
     * Search To Response
     * 搜索结果为SearchResponse对象
     * @param sourceBuilder
     * @return
     * @throws IOException
     */
    public SearchResponse search(SearchSourceBuilder sourceBuilder) throws IOException {
        writeClassType();
        writeSearchSourceBuilder(sourceBuilder);
        String index = esClient.getIndex(clazzT);
        SearchRequest request = new SearchRequest(index).source(sourceBuilder);
        SearchResponse rsp = esClient.getRestClient().search(request, RequestOptions.DEFAULT);
        return rsp;
    }

    /**
     * Search To List
     * 搜索结果为List对象
     * @param sourceBuilder
     * @return
     * @throws IOException
     */
    public List<T> searchToList(SearchSourceBuilder sourceBuilder) throws IOException {
        SearchResponse rsp = search(sourceBuilder);
        List<T> list = new ArrayList<>();
        for (SearchHit hit : rsp.getHits()) {
            list.add(JSONUtils.json2Bean(hit.getSourceAsString(), clazzT));
        }
        return list;
    }

    /**
     * Search To Map
     * 搜索结果为Map对象
     * @param sourceBuilder
     * @return
     * @throws IOException
     */
    public Map<String, T> searchToMap(SearchSourceBuilder sourceBuilder) throws IOException {
        SearchResponse rsp = search(sourceBuilder);
        Map<String, T> map = new LinkedHashMap<>();
        for (SearchHit hit : rsp.getHits()) {
            map.put(hit.getId(), JSONUtils.json2Bean(hit.getSourceAsString(), clazzT));
        }
        return map;
    }

    /**
     * Search To Page
     * 搜索结果为Page对象
     * @param sourceBuilder
     * @return
     * @throws IOException
     */
    public ElasticSearchPage searchToPage(SearchSourceBuilder sourceBuilder) throws IOException {
        SearchResponse rsp = search(sourceBuilder);
        int pageFrom = sourceBuilder.from();
        int pageSize = sourceBuilder.size();
        long totalRecord = rsp.getHits().getTotalHits().value;
        int totalPage = getTotalPage(totalRecord, pageSize);
        int pageNo = getPageNo(pageFrom, pageSize);
        List<T> list = new ArrayList<>();
        for (SearchHit hit : rsp.getHits()) {
            list.add(JSONUtils.json2Bean(hit.getSourceAsString(), clazzT));
        }
        return new ElasticSearchPage().setPageFrom(pageFrom).setPageNo(pageNo)
                .setPageSize(pageSize).setTotalRecord(totalRecord).setTotalPage(totalPage)
                .setRecords(list);
    }


    /**
     * 获取mapping
     * ES 7.1的语法
     * GET /${index}/_mapping
     *
     *  创建mapping
     * PUT /es_bean2/
     * {
     *   "mappings": {
     *      "properties": {
     *       "t_no":{
     *           "type":"long"
     *       }
     *     }
     *   }
     * }
     *
     * 更新mapping
     * PUT /es_bean2/_mapping
     * {
     *     "properties": {
     *       "t_no2":{
     *           "type":"long"
     *       }
     *     }
     * }
     * @return
     * @throws IOException
     */
    public Map<String, MappingMetaData> getMapping() throws IOException {
        writeClassType();
        String index = esClient.getIndex(clazzT);
        GetMappingsRequest request = new GetMappingsRequest().indices(index);
        return esClient.getRestClient().indices().getMapping(request, RequestOptions.DEFAULT).mappings();
    }


    /**
     * The ping
     * @return
     * @throws IOException
     */
    public boolean ping() throws IOException {
        return esClient.getRestClient().ping(RequestOptions.DEFAULT);
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

    /**
     * 写入泛型Class类型
     */
    private void writeClassType() {
        clazzT = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 写入SearchSourceBuild
     * @param builder
     */
    private void writeSearchSourceBuilder(SearchSourceBuilder builder) {
        builder.from(builder.from() < 0 ? DEFAULT_PAGE_FROM : builder.from());
        builder.size(builder.size() < 0 ? DEFAULT_PAGE_SIZE : builder.size());
    }

    /**
     * 获取页起始索引
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @return
     */
    private int getPageFrom(int pageNo, int pageSize) {
        if (pageNo <= 0) {
            pageNo = DEFAULT_PAGE_NO;
        }
        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        return (pageNo - 1) * pageSize;
    }

    /**
     * 获取页数
     * @param pageFrom 起始页索引
     * @param pageSize 每页大小
     * @return
     */
    private int getPageNo(int pageFrom, int pageSize) {
        if (pageFrom < 0) {
            pageFrom = DEFAULT_PAGE_FROM;
        }
        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        return  (pageFrom + pageSize) / pageSize;
    }

    /**
     * 获取总页数
     * @param totalRecord 总记录条数
     * @param pageSize 每页大小
     * @return
     */
    private int getTotalPage(long totalRecord, int pageSize) {
        return (int) ((totalRecord  +  pageSize  - 1) / pageSize);
    }
}
