package com.chenshinan.exercises.elasticsearch;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author shinan.chen
 * @since 2019/7/2
 */
@Service
public class EsService {
    public static final Logger LOGGER = LoggerFactory.getLogger(EsService.class);
    @Autowired
    private RestHighLevelClient highLevelClient;

    public SearchResponse search(String index, String searchStr) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder x = new TermQueryBuilder("project_id", "28");
        BoolQueryBuilder boolBuilder = new BoolQueryBuilder();
        boolBuilder.filter(x);
        boolBuilder.should(QueryBuilders.matchQuery("title", searchStr))
                .should(QueryBuilders.matchQuery("content", searchStr));
        sourceBuilder.query(boolBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(1000);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.requireFieldMatch(true).field("title").field("content")
                .preTags("<strong>").postTags("</strong>");

        sourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Arrays.stream(response.getHits().getHits())
                    .forEach(hit -> {
                        System.out.println(hit.getId());
//                        System.out.println(hit.getIndex());
//                        System.out.println(hit.getSourceAsString());
//                        //取高亮结果
//                        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
//                        HighlightField highlight = highlightFields.get("content");
//                        if (highlight != null) {
//                            Text[] fragments = highlight.fragments();  //多值的字段会有多个值
//                            if (fragments != null) {
//                                String fragmentString = fragments[0].string();
//                                LOGGER.info("content highlight : " + fragmentString);
//                                //可用高亮字符串替换上面sourceAsMap中的对应字段返回到上一级调用
//                                //sourceAsMap.put("content", fragmentString);
//                            }
//                        }
                    });
            System.out.println(response.getHits());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public SearchResponse searchAll(String index) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        SearchResponse response = null;
        try {
            response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(response.getHits().getTotalHits().value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public SearchResponse searchTest(String index, String searchStr) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder x = new TermQueryBuilder("title", "csn");
        BoolQueryBuilder boolBuilder = new BoolQueryBuilder();
        boolBuilder.filter(x);
        boolBuilder.must(QueryBuilders.matchQuery("content", searchStr));
        sourceBuilder.query(boolBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.requireFieldMatch(true).field("content")
                .preTags("<strong>").postTags("</strong>");

        sourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse response = null;
        try {
            response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Arrays.stream(response.getHits().getHits())
                    .forEach(hit -> {
                        System.out.println(hit.getIndex());
                        System.out.println(hit.getSourceAsString());
                        //取高亮结果
                        Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                        HighlightField highlight = highlightFields.get("content");
                        if (highlight != null) {
                            Text[] fragments = highlight.fragments();  //多值的字段会有多个值
                            if (fragments != null) {
                                String fragmentString = fragments[0].string();
                                LOGGER.info("content highlight : " + fragmentString);
                                //可用高亮字符串替换上面sourceAsMap中的对应字段返回到上一级调用
                                //sourceAsMap.put("content", fragmentString);
                            }
                        }
                    });
            System.out.println(response.getHits());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public CreateIndexResponse createIndex(String index) {
        // 1、创建 创建索引request
        CreateIndexRequest request = new CreateIndexRequest(index);
        // 2、设置索引的settings，设置默认分词
        request.settings(Settings.builder().put("analysis.analyzer.default.tokenizer", "ik_smart"));

        // 3、设置索引的mappings
//        request.mapping("_page",
//                "  {\n" +
//                        "    \"_page\": {\n" +
//                        "      \"properties\": {\n" +
//                        "        \"title\": {\n" +
//                        "          \"type\": \"string\"\n" +
//                        "        },\n" +
//                        "           \"content\": {\n" +
//                        "          \"type\": \"string\"\n" +
//                        "        }\n" +
//                        "       }\n" +
//                        "    }\n" +
//                        "  }",
//                XContentType.JSON);

        // 4、 设置索引的别名
//        request.alias(new Alias("mmm"));

        // 5、 发送请求 这里和RESTful风格不同
        CreateIndexResponse createIndexResponse = null;
        try {
            createIndexResponse = highLevelClient.indices()
                    .create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 6、处理响应
        boolean acknowledged = createIndexResponse.isAcknowledged();
        boolean shardsAcknowledged = createIndexResponse
                .isShardsAcknowledged();
        System.out.println("acknowledged = " + acknowledged);
        System.out.println("shardsAcknowledged = " + shardsAcknowledged);

        // listener方式发送请求
            /*ActionListener<CreateIndexResponse> listener = new ActionListener<CreateIndexResponse>() {
                @Override
                public void onResponse(
                        CreateIndexResponse createIndexResponse) {
                    // 6、处理响应
                    boolean acknowledged = createIndexResponse.isAcknowledged();
                    boolean shardsAcknowledged = createIndexResponse
                            .isShardsAcknowledged();
                    System.out.println("acknowledged = " + acknowledged);
                    System.out.println(
                            "shardsAcknowledged = " + shardsAcknowledged);
                }

                @Override
                public void onFailure(Exception e) {
                    System.out.println("创建索引异常：" + e.getMessage());
                }
            };
            client.admin().indices().create(request, listener);
            */
        return createIndexResponse;
    }

    public IndexResponse createPage(String index, Long id, String title, String content) {
        // 1、创建索引请求
        IndexRequest request = new IndexRequest(index);
        request.id(String.valueOf(id));

        // 2、准备文档数据
        Map<String, Object> jsonMap = new HashMap<>(2);
        jsonMap.put("title", title);
        jsonMap.put("content", content);
        request.source(jsonMap);
        //3、其他的一些可选设置
            /*
            request.routing("routing");  //设置routing值
            request.timeout(TimeValue.timeValueSeconds(1));  //设置主分片等待时长
            request.setRefreshPolicy("wait_for");  //设置重刷新策略
            request.version(2);  //设置版本号
            request.opType(DocWriteRequest.OpType.CREATE);  //操作类别
            */

        //4、发送请求
        IndexResponse indexResponse = null;
        try {
            // 同步方式
            indexResponse = highLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            // 捕获，并处理异常
            //判断是否版本冲突、create但文档已存在冲突
            if (e.status() == RestStatus.CONFLICT) {
                LOGGER.error("冲突了，请在此写冲突处理逻辑！\n" + e.getDetailedMessage());
            }
            LOGGER.error("索引异常", e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //5、处理响应
        if (indexResponse != null) {
            long version = indexResponse.getVersion();
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                System.out.println("新增文档成功，处理逻辑代码写到这里。");
            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                System.out.println("修改文档成功，处理逻辑代码写到这里。");
            }
            // 分片处理信息
            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {

            }
            // 如果有分片副本失败，可以获得失败原因信息
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    String reason = failure.reason();
                    System.out.println("副本失败原因：" + reason);
                }
            }
        }
        return indexResponse;
        //异步方式发送索引请求
            /*ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {

                }

                @Override
                public void onFailure(Exception e) {

                }
            };
            client.indexAsync(request, listener);
            */
    }

    public GetResponse getPage(String index, Long id) {
        // 1、创建获取文档请求
        GetRequest request = new GetRequest(index, String.valueOf(id));

        // 2、可选的设置
        //request.routing("routing");
        //request.version(2);

        //request.fetchSourceContext(new FetchSourceContext(false)); //是否获取_source字段
        //选择返回的字段
        String[] includes = new String[]{"title", "content"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        request.fetchSourceContext(fetchSourceContext);

        //也可写成这样
            /*String[] includes = Strings.EMPTY_ARRAY;
            String[] excludes = new String[]{"message"};
            FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
            request.fetchSourceContext(fetchSourceContext);*/


        // 取stored字段
            /*request.storedFields("message");
            GetResponse getResponse = client.get(request);
            String message = getResponse.getField("message").getValue();*/


        //3、发送请求
        GetResponse getResponse = null;
        try {
            // 同步请求
            getResponse = highLevelClient.get(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                LOGGER.error("没有找到该id的文档");
            }
            if (e.status() == RestStatus.CONFLICT) {
                LOGGER.error("获取时版本冲突了，请在此写冲突处理逻辑！");
            }
            LOGGER.error("获取文档异常", e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //4、处理响应
        if (getResponse != null) {
            String indexx = getResponse.getIndex();
            String type = getResponse.getType();
            String idx = getResponse.getId();
            if (getResponse.isExists()) { // 文档存在
                long version = getResponse.getVersion();
                String sourceAsString = getResponse.getSourceAsString(); //结果取成 String
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();  // 结果取成Map
                byte[] sourceAsBytes = getResponse.getSourceAsBytes();    //结果取成字节数组

                LOGGER.info("index:" + indexx + "  type:" + type + "  id:" + idx);
                LOGGER.info(sourceAsString);

            } else {
                LOGGER.error("没有找到该id的文档");
            }
        }

        return getResponse;
        //异步方式发送获取文档请求
            /*
            ActionListener<GetResponse> listener = new ActionListener<GetResponse>() {
                @Override
                public void onResponse(GetResponse getResponse) {

                }

                @Override
                public void onFailure(Exception e) {

                }
            };
            client.getAsync(request, listener);
            */
    }

    public DeleteResponse deletePage(String index, Long id) {
        DeleteRequest request = new DeleteRequest(index, String.valueOf(id));
        DeleteResponse deleteResponse = null;
        try {
            // 同步请求
            deleteResponse = highLevelClient.delete(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            LOGGER.error("删除文档异常", e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //4、处理响应
        if (deleteResponse != null) {
            LOGGER.info(deleteResponse.toString());
        }
        return deleteResponse;
    }
}
