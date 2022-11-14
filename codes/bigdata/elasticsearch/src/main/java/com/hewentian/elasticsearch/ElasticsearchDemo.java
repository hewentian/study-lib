package com.hewentian.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.*;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.get.GetResult;
import co.elastic.clients.elasticsearch.core.mget.MultiGetResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.ResponseBody;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.elasticsearch.nodes.NodesStatsResponse;
import co.elastic.clients.elasticsearch.nodes.Stats;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hewentian.elasticsearch.entity.User;
import com.hewentian.elasticsearch.util.ElasticsearchUtil;
import jakarta.json.spi.JsonProvider;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * <b>ElasticsearchDemo</b> 是
 * <p>
 * https://www.elastic.co/guide/en/elasticsearch/reference/8.4/docs-index_.html
 * https://www.elastic.co/guide/en/elasticsearch/reference/8.4/docs-get.html
 * https://www.elastic.co/guide/en/elasticsearch/reference/8.4/docs-bulk.html
 * https://www.elastic.co/guide/en/elasticsearch/reference/8.4/search-your-data.html
 * https://www.elastic.co/guide/en/elasticsearch/reference/8.4/search.html
 * https://www.elastic.co/guide/en/elasticsearch/reference/8.4/search-template.html
 * https://www.elastic.co/guide/en/elasticsearch/reference/8.4/search-aggregations.html
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/blocking-and-async.html
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/loading-json.html
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/variant-types.html
 * https://github.com/elastic/elasticsearch-java/tree/8.4/java-client/src/test/java/co/elastic/clients/documentation
 *
 * </p>
 *
 * @since JDK 1.8
 */
public class ElasticsearchDemo {
    private static String indexName = "user";

    private static ElasticsearchClient elasticsearchClient;
    private static ElasticsearchAsyncClient elasticsearchAsyncClient;

//    public static void bulkProcessor() throws InterruptedException, IOException {
//        BulkProcessor bulkProcessor = BulkProcessor.builder(transportClient, new BulkProcessor.Listener() {
//            @Override
//            public void beforeBulk(long executionId, BulkRequest request) {
//                System.out.println("beforeBulk...");
//            }
//
//            @Override
//            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
//                System.out.println("afterBulk...");
//            }
//
//            @Override
//            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
//                // 出错时执行
//            }
//        })
//                .setBulkActions(10) // 10次请求执行一次
//                .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.MB)) // 1m的数据刷新一次bulk
//                .setFlushInterval(TimeValue.timeValueSeconds(10)) // 固定10秒刷新一次
//                .setConcurrentRequests(1) // 并发请求：0.不允许；1.允许
//                .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3)) // 设置退避, 100ms后执行, 最大请求3次
//                .build();
//
//        // 添加请求
//        XContentBuilder builder = XContentFactory.jsonBuilder()
//                .startObject()
//                .field("name", "Scott")
//                .field("age", 80)
//                .field("address", "Caliafornia, USA")
//                .endObject();
//        IndexRequest indexRequest = new IndexRequest(indexName, typeName, "3");
//        indexRequest.source(builder);
//
//        bulkProcessor.add(indexRequest);
//        bulkProcessor.add(new DeleteRequest(indexName, typeName, "2"));
//
//        bulkProcessor.awaitClose(2, TimeUnit.MINUTES);
////        bulkProcessor.close();
//    }

    private static void addDocDSL() throws IOException {
        User user = User.builder()
                .id(101L)
                .name("scott")
                .age(20)
                .tags(new String[]{"programmer", "reader"})
                .birthday(new Date())
                .build();

        // 如果不指定id的话，系统会自动产生
        IndexResponse indexResponse = elasticsearchClient.index(i -> i
                .index(indexName)
                .id(user.getId().toString())
                .document(user));

        System.out.println(indexResponse);
    }

    private static void addDocDSL2() throws IOException {
        User user = User.builder()
                .id(102L)
                .name("scott")
                .age(20)
                .tags(new String[]{"programmer", "reader"})
                .birthday(new Date())
                .build();

        IndexRequest<User> indexRequest = IndexRequest.of(i -> i
                .index(indexName)
                .id(user.getId().toString())
                .document(user));

        IndexResponse indexResponse = elasticsearchClient.index(indexRequest);
        System.out.println(indexResponse);
    }

    private static void addDocBuilder() throws IOException {
        User user = User.builder()
                .id(103L)
                .name("scott")
                .age(20)
                .tags(new String[]{"programmer", "reader"})
                .birthday(new Date())
                .build();

        IndexRequest.Builder<User> indexRequestBuilder = new IndexRequest.Builder<>();
        indexRequestBuilder.index(indexName);
        indexRequestBuilder.id(user.getId().toString());
        indexRequestBuilder.document(user);

        IndexResponse indexResponse = elasticsearchClient.index(indexRequestBuilder.build());
        System.out.println(indexResponse);
    }

    private static void addDocJSON() throws IOException {
//        String jsonStr = "{'id':104, 'name':'scott', 'age':20, 'tags':['programmer', 'reader'], 'birthday':'2022-04-08T13:55:32Z'}";
        String jsonStr = "{'id':104, 'name':'scott', 'age':20, 'tags':['programmer', 'reader'], 'birthday':1664951549729}";

        Reader input = new StringReader(jsonStr.replace('\'', '"'));

        IndexRequest<JsonData> indexRequest = IndexRequest.of(i -> i
                .index(indexName)
                .id("104")
                .withJson(input));

        IndexResponse indexResponse = elasticsearchClient.index(indexRequest);
        System.out.println(indexResponse);
    }

    private static void addDocDSLAsync() {
        User user = User.builder()
                .id(105L)
                .name("scott")
                .age(20)
                .tags(new String[]{"programmer", "reader"})
                .birthday(new Date())
                .build();

        elasticsearchAsyncClient.index(i -> i
                .index(indexName)
                .id(user.getId().toString())
                .document(user))
                .whenComplete(((indexResponse, throwable) -> {
                    if (throwable != null) {
//                logger.error("Failed to index", exception);
                        throwable.printStackTrace();
                    } else {
                        System.out.println(indexResponse);
                    }
                }));
    }

    // 可以一次执行新增或者删除等操作
    private static void addDocDSLBulk() throws IOException {
        User user1 = User.builder()
                .id(106L)
                .name("scott")
                .age(22)
                .tags(new String[]{"programmer", "traveler"})
                .birthday(new Date())
                .build();

        User user2 = User.builder()
                .id(107L)
                .name("tiger")
                .age(21)
                .tags(new String[]{"programmer", "runner"})
                .birthday(new Date())
                .build();

        List<User> userList = Stream.of(user1, user2).collect(Collectors.toList());

        BulkRequest.Builder br = new BulkRequest.Builder();

        for (User user : userList) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id(user.getId().toString())
                            .document(user)));
        }

        BulkResponse bulkResponse = elasticsearchClient.bulk(br.build());

        bulkResponse.items().forEach(System.out::println);

        // Log errors, if any
        if (bulkResponse.errors()) {
            System.out.println("Bulk had errors");
            for (BulkResponseItem item : bulkResponse.items()) {
                if (item.error() != null) {
                    System.out.println(item.error().reason());
                }
            }
        }
    }

    private static void addDocJSONBulk() throws IOException {
        String resourcesPath = ElasticsearchDemo.class.getClassLoader().getResource("").getPath();
        File resourcesDir = new File(resourcesPath);

        // List json user files in the resources directory
        File[] userFiles = resourcesDir.listFiles(
                file -> file.getName().matches("user-.*\\.json")
        );

        BulkRequest.Builder br = new BulkRequest.Builder();

        for (File file : userFiles) {
            JsonData json = readJson(new FileInputStream(file), elasticsearchClient);

            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id(json.toJson().asJsonObject().get("id").toString())
                            .document(json)
                    )
            );
        }

        BulkResponse bulkResponse = elasticsearchClient.bulk(br.build());

        bulkResponse.items().forEach(System.out::println);

        // Log errors, if any
        if (bulkResponse.errors()) {
            System.out.println("Bulk had errors");
            for (BulkResponseItem item : bulkResponse.items()) {
                if (item.error() != null) {
                    System.out.println(item.error().reason());
                }
            }
        }
    }

    private static void addDocJSONBulk2() throws IOException {
        InputStream inputStream = ElasticsearchDemo.class.getClassLoader().getResourceAsStream("users.json");

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode[] jsonNodes = objectMapper.readValue(inputStream, JsonNode[].class);

        BulkRequest.Builder br = new BulkRequest.Builder();

        for (JsonNode jsonNode : jsonNodes) {
            JsonData json = JsonData.fromJson(jsonNode.toString());

            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id(jsonNode.get("id").toString())
                            .document(json)
                    )
            );
        }

        BulkResponse bulkResponse = elasticsearchClient.bulk(br.build());

        bulkResponse.items().forEach(System.out::println);

        // Log errors, if any
        if (bulkResponse.errors()) {
            System.out.println("Bulk had errors");
            for (BulkResponseItem item : bulkResponse.items()) {
                if (item.error() != null) {
                    System.out.println(item.error().reason());
                }
            }
        }
    }

    // 这种读法，一次只能在文件中读取一个 json 对象，适合一个文件保存一个 json 对象的情况
    private static JsonData readJson(InputStream input, ElasticsearchClient esClient) {
        JsonpMapper jsonpMapper = esClient._transport().jsonpMapper();
        JsonProvider jsonProvider = jsonpMapper.jsonProvider();

        return JsonData.from(jsonProvider.createParser(input), jsonpMapper);
    }

    private static void getById() throws IOException {
        String id = "101";

        GetResponse<User> response = elasticsearchClient.get(g -> g
                        .index(indexName)
                        .id(id),
                User.class
        );

        if (response.found()) {
            User user = response.source();
            System.out.println(user);
        } else {
            System.out.println("user not found");
        }
    }

    private static void getByIdJson() throws IOException {
        String id = "101";

        GetResponse<ObjectNode> response = elasticsearchClient.get(g -> g
                        .index(indexName)
                        .id(id),
                ObjectNode.class
        );

        if (response.found()) {
            ObjectNode user = response.source();
            System.out.println(user);
        } else {
            System.out.println("user not found");
        }
    }

    private static void simpleSearch() throws IOException {
        String searchText = "scott";

        SearchResponse<User> response = elasticsearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .match(t -> t
                                        .field("name")
                                        .query(searchText)
                                )
                        ),
                User.class
        );

        TotalHits total = response.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

        if (isExactResult) {
            System.out.println("There are " + total.value() + " results");
        } else {
            System.out.println("There are more than " + total.value() + " results");
        }

        List<Hit<User>> hits = response.hits().hits();
        for (Hit<User> hit : hits) {
            User user = hit.source();
            System.out.println("Found user: " + user + ", score " + hit.score());
        }
    }

    private static void termQuerySearch() throws IOException {
        TermQuery query = QueryBuilders.term()
                .field("name")
                .value("scott")
                .build();

        // DSL format
//        query = TermQuery.of(t -> t.field("name").value("scott"));

        SearchRequest request = new SearchRequest.Builder()
                .index(indexName)
                .query(query._toQuery())
                .build();

        SearchResponse<User> response = elasticsearchClient.search(request, User.class);

        TotalHits total = response.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

        if (isExactResult) {
            System.out.println("There are " + total.value() + " results");
        } else {
            System.out.println("There are more than " + total.value() + " results");
        }

        List<Hit<User>> hits = response.hits().hits();
        for (Hit<User> hit : hits) {
            User user = hit.source();
            System.out.println("Found user: " + user + ", score " + hit.score());
        }
    }

    private static void withJsonSearch() throws IOException {
        StringBuilder queryJson = new StringBuilder();
        queryJson.append("{").append(System.lineSeparator());
        queryJson.append("  \"query\": {").append(System.lineSeparator());
        queryJson.append("    \"match\": {").append(System.lineSeparator());
        queryJson.append("      \"name\": \"scott\"").append(System.lineSeparator());
        queryJson.append("    }").append(System.lineSeparator());
        queryJson.append("  }").append(System.lineSeparator());
        queryJson.append("}");

        System.out.println(queryJson.toString());

        InputStream queryJsonInputStream = new ByteArrayInputStream(queryJson.toString().getBytes(StandardCharsets.UTF_8));
        SearchResponse<User> response = elasticsearchClient.search(s -> s
                        .index(indexName)
                        .withJson(queryJsonInputStream),
                User.class
        );

        TotalHits total = response.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

        if (isExactResult) {
            System.out.println("There are " + total.value() + " results");
        } else {
            System.out.println("There are more than " + total.value() + " results");
        }

        List<Hit<User>> hits = response.hits().hits();
        for (Hit<User> hit : hits) {
            User user = hit.source();
            System.out.println("Found user: " + user + ", score " + hit.score());
        }
    }

    private static void nestedSearch() throws IOException {
        String searchText = "scott";
        int minAge = 22;

        // Search by name
        Query byName = MatchQuery.of(m -> m
                .field("name")
                .query(searchText)
        )._toQuery();

        // Search by min age
        Query byMinAge = RangeQuery.of(r -> r
                .field("age")
                .gte(JsonData.of(minAge))
        )._toQuery();

        // Combine name and age queries to search the user index
        SearchResponse<User> response = elasticsearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .bool(b -> b
                                        .must(byName)
                                        .must(byMinAge)
                                )
                        ),
                User.class
        );

        List<Hit<User>> hits = response.hits().hits();
        for (Hit<User> hit : hits) {
            User user = hit.source();
            System.out.println("Found user: " + user + ", score " + hit.score());
        }
    }

    private static void createTemplateScript() throws IOException {
        // Create a script
        PutScriptResponse putScriptResponse = elasticsearchClient.putScript(r -> r
                .id("query-script") // Identifier of the template script to create.
                .script(s -> s
                        .lang("mustache")
                        .source("{\"query\":{\"match\":{\"{{field}}\":\"{{value}}\"}}}")
                ));

        System.out.println(putScriptResponse);
    }

    private static void templatedSearch() throws IOException {
        SearchTemplateResponse<User> response = elasticsearchClient.searchTemplate(r -> r
                        .index(indexName)
                        .id("query-script") // Identifier of the template script to use.
                        .params("field", JsonData.of("name")) // Template parameter values.
                        .params("value", JsonData.of("tiger")),
                User.class
        );

        List<Hit<User>> hits = response.hits().hits();
        for (Hit<User> hit : hits) {
            User user = hit.source();
            System.out.println("Found user: " + user + ", score " + hit.score());
        }
    }

    private static void simpleAggregation() throws IOException {
        String searchText = "scott";

        Query query = MatchQuery.of(m -> m
                .field("name")
                .query(searchText)
        )._toQuery();

        SearchResponse<Void> response = elasticsearchClient.search(s -> s
                        .index(indexName)
                        .size(0)
                        .query(query)
                        .aggregations("age-histogram", a -> a
                                .histogram(h -> h
                                        .field("age")
                                        .interval(22.0)
                                )
                        ),
                Void.class
        );

        List<HistogramBucket> buckets = response.aggregations()
                .get("age-histogram")
                .histogram()
                .buckets().array();

        for (HistogramBucket bucket : buckets) {
            System.out.println("There are " + bucket.docCount() + " " + searchText + " under " + bucket.key());
        }
    }

    private static void complexAggregation() throws IOException {
        Query query = QueryBuilders.matchAll().build()._toQuery();

        SearchResponse<Void> response = elasticsearchClient.search(s -> s
                        .index(indexName)
                        .size(0)
                        .query(query)
                        .searchType(SearchType.QueryThenFetch)
                        .aggregations("age-term", a -> a.terms(t -> t.field("age")))
                        .aggregations("name-term", a -> a.terms(t -> t.field("name.keyword")))
                , Void.class
        );

        for (Map.Entry<String, Aggregate> en : response.aggregations().entrySet()) {
            System.out.println(en.getKey());

            Aggregate aggregate = en.getValue();
            if (aggregate.isSterms()) {
                StringTermsAggregate sterms = aggregate.sterms();
                sterms.buckets().array().forEach(e -> System.out.println(e.key() + ", " + e.docCount()));
            } else if (aggregate.isLterms()) {
                LongTermsAggregate lterms = aggregate.lterms();
                lterms.buckets().array().forEach(e -> System.out.println(e.key() + ", " + e.docCount()));
            }
        }
    }

    private static void deleteDocDSL() throws IOException {
        String id = "102";

        DeleteRequest deleteRequest = DeleteRequest.of(i -> i
                .index(indexName)
                .id(id)
        );

        DeleteResponse deleteResponse = elasticsearchClient.delete(deleteRequest);
        System.out.println(deleteResponse);
    }

    public static void updateDoc() throws IOException {
        // 是部分更新，只会更新指定的字段：若存在的则修改，否则增加。
        // 要更新的 id 一定要存在，否则更新失败
        User userPartial = User.builder()
                .id(101L)
                .name("scott again")
                .age(22)
                .tags(new String[]{"programmer", "reader", "runner"})
//                .birthday(new Date())
                .build();

        UpdateRequest<User, User> updateRequest = UpdateRequest.of(i -> i
                .index(indexName)
                .id(userPartial.getId().toString())
                .doc(userPartial)
        );

        UpdateResponse<User> updateResponse = elasticsearchClient.update(updateRequest, User.class);
        System.out.println(updateResponse);
    }

    public static void updateScript() throws IOException {
        // 是部分更新，只会更新指定的字段：若存在的则修改，否则增加。
        // 要更新的 id 一定要存在，否则更新失败
        String id = "101";

        Script script = Script.of(s -> s.inline(InlineScript.of(i -> i
                .lang("painless")
                .source("ctx._source.name=\"scott\"")
        )));

        UpdateRequest<User, User> updateRequest = UpdateRequest.of(i -> i
                .index(indexName)
                .id(id)
                .script(script)
        );

        UpdateResponse<User> updateResponse = elasticsearchClient.update(updateRequest, User.class);
        System.out.println(updateResponse);
    }

    public static void upsertDoc() throws IOException {
        // 当文档不存在的时候，就自动新增
        // CREATED: 当 id 不存在的时候，会仅按 user 的内容创建
        // UPDATED: 当 id 存在的时候，则会按 userPartial 的内容更新

        Long id = 102L;

        User user = User.builder()
                .id(id)
                .name("scott")
                .age(20)
                .tags(new String[]{"programmer", "reader"})
                .birthday(new Date())
                .build();

        User userPartial = User.builder()
                .id(id)
                .name("scott again")
                .age(22)
                .tags(new String[]{"programmer", "reader", "runner"})
//                .birthday(new Date())
                .build();

        UpdateRequest<User, User> updateRequest = UpdateRequest.of(i -> i
                .index(indexName)
                .id(id.toString())
                .doc(userPartial)
                .upsert(user)
        );

        UpdateResponse<User> updateResponse = elasticsearchClient.update(updateRequest, User.class);
        System.out.println(updateResponse);
    }

    /**
     * 从不同的index, id中获取
     */
    public static void mGet() throws IOException {
        MgetResponse<User> mgetResponse = elasticsearchClient.mget(m -> m
                        .index(indexName)
                        .ids("101", "102", "103")
                , User.class);

        for (MultiGetResponseItem<User> multiGetResponseItem : mgetResponse.docs()) {
            GetResult<User> getResult = multiGetResponseItem.result();
            if (null != getResult && getResult.found()) {
                User user = getResult.source();
                System.out.println(user);
            }
        }
    }

    private static void filter() throws IOException {
        Query query = QueryBuilders.matchAll().build()._toQuery();

        SearchResponse<User> response = elasticsearchClient.search(s -> s
                        .index(indexName)
                        .query(query)
                        .searchType(SearchType.QueryThenFetch)
                        .postFilter(f -> f.range(r -> r.field("age")
                                .gte(JsonData.of(21))
                                .lte(JsonData.of(23))))
                        .explain(true) // 根据数据相关度排序，和关键字匹配最高的排在前面
                , User.class
        );

        TotalHits total = response.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

        if (isExactResult) {
            System.out.println("There are " + total.value() + " results");
        } else {
            System.out.println("There are more than " + total.value() + " results");
        }

        List<Hit<User>> hits = response.hits().hits();
        for (Hit<User> hit : hits) {
            User user = hit.source();
            System.out.println("Found user: " + user + ", score " + hit.score());
        }
    }

    private static void scrollSearch() throws IOException {
        String searchText = "scott";

        Time time = Time.of(t -> t.time("1m"));

        ResponseBody<User> response = elasticsearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .match(t -> t
                                        .field("name")
                                        .query(searchText)
                                )
                        )
                        .scroll(time)
                        .sort(SortOptions.of(so -> so.field(FieldSort.of(f -> f.field("age").order(SortOrder.Asc)))))
                        .size(3)
                , User.class
        );

        TotalHits total = response.hits().total();
        boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

        if (isExactResult) {
            System.out.println("There are " + total.value() + " results");
        } else {
            System.out.println("There are more than " + total.value() + " results");
        }

        do {
            System.out.println("-----------------------------------");
            List<Hit<User>> hits = response.hits().hits();
            for (Hit<User> hit : hits) {
                User user = hit.source();
                System.out.println("Found user: " + user + ", score " + hit.score());
            }

            String scrollId = response.scrollId();
            System.out.println("scrollId: " + scrollId);

            response = elasticsearchClient.scroll(s -> s.scrollId(scrollId).scroll(time), User.class);
        } while (response.hits().hits().size() != 0);
    }

    public static void nodeStat() throws Exception {
        NodesStatsResponse nodesStatsResponse = elasticsearchClient.nodes().stats();
        for (Map.Entry<String, Stats> entry : nodesStatsResponse.nodes().entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        try {
            elasticsearchClient = ElasticsearchUtil.getElasticsearchClient();
            elasticsearchAsyncClient = ElasticsearchUtil.getElasticsearchAsyncClient();

//            addDocDSL();
//            addDocDSL2();
//            addDocBuilder();
//            addDocJSON();
//            addDocDSLAsync(); // TODO 要注释掉 finally 块中的 ElasticsearchUtil.close()
//            addDocDSLBulk();
//            addDocJSONBulk();
//            addDocJSONBulk2();
//            getById();
//            getByIdJson();
//            simpleSearch();
//            termQuerySearch();
//            withJsonSearch();
//            nestedSearch();
//            createTemplateScript();
//            templatedSearch();
//            simpleAggregation();
//            complexAggregation();
//            deleteDocDSL();
//            updateDoc();
//            updateScript();
//            upsertDoc();
//            mGet();
//            filter();
//            scrollSearch();
//            nodeStat();

//            bulkProcessor(); // todo 这个好像在 8.0 后，被废弃
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 当使用 Async Client 的时候，不能在这里关闭。因为是 Async 执行的，可能还没执行完，就被关闭了相关资源
            ElasticsearchUtil.close();
        }
    }
}
