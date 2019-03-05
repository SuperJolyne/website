package elas;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jijngbao
 * @date 19-1-23
 */
public class SearchDocument {
//    private static Logger logger = LogManager.getRootLogger();

    public static void main(String[] args) throws IOException {

//        groupSearch("my00391","goods","{\"one\":\"男士\",\"two\":\"服装\",\"three\":\"西服\",\"title\":\"ARMANI\"}");
//        groupSearch("my00391","goods","{\"ctn\":\"男士 服装 西服 ARMANI\"}",0,0,30000);


//        groupSearch("my0039","goods","{\"one\":\"女士\"}");
//        simpleSearch("my0039","goods","title","男士");

//        esSearch("my0039","goods","男士",0);

    }

    public static void simpleSearch(String indexName,String typeName,String
            filedName,String filedValue){
        try (RestHighLevelClient client = InitClient.getClient();) {

            // 1、创建search请求
            //SearchRequest searchRequest = new SearchRequest();
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.types(typeName);

            // 2、用SearchSourceBuilder来构造查询请求体 ,请仔细查看它的方法，构造各种查询的方法都在这。
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            //构造QueryBuilder
            /*QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("user", "kimchy")
                    .fuzziness(Fuzziness.AUTO)
                    .prefixLength(3)
                    .maxExpansions(10);
            sourceBuilder.query(matchQueryBuilder);*/

            sourceBuilder.query(QueryBuilders.termQuery(filedName, filedValue));
            sourceBuilder.from(0);
            sourceBuilder.size(10);
            sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

            //是否返回_source字段
            //sourceBuilder.fetchSource(false);

            //设置返回哪些字段
            /*String[] includeFields = new String[] {"title", "user", "innerObject.*"};
            String[] excludeFields = new String[] {"_type"};
            sourceBuilder.fetchSource(includeFields, excludeFields);*/

            //指定排序
            //sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
            //sourceBuilder.sort(new FieldSortBuilder("_uid").order(SortOrder.ASC));

            // 设置返回 profile
            //sourceBuilder.profile(true);

            //将请求体加入到请求中
            searchRequest.source(sourceBuilder);

            // 可选的设置
            //searchRequest.routing("routing");

            // 高亮设置
            /*
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            HighlightBuilder.Field highlightTitle =
                    new HighlightBuilder.Field("title");
            highlightTitle.highlighterType("unified");
            highlightBuilder.field(highlightTitle);
            HighlightBuilder.Field highlightUser = new HighlightBuilder.Field("user");
            highlightBuilder.field(highlightUser);
            sourceBuilder.highlighter(highlightBuilder);*/


            //加入聚合
            /*TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_company")
                    .field("company.keyword");
            aggregation.subAggregation(AggregationBuilders.avg("average_age")
                    .field("age"));
            sourceBuilder.aggregation(aggregation);*/

            //做查询建议
            /*SuggestionBuilder termSuggestionBuilder =
                    SuggestBuilders.termSuggestion("user").text("kmichy");
                SuggestBuilder suggestBuilder = new SuggestBuilder();
                suggestBuilder.addSuggestion("suggest_user", termSuggestionBuilder);
            sourceBuilder.suggest(suggestBuilder);*/

            //3、发送请求
            SearchResponse searchResponse = client.search(searchRequest);


            //4、处理响应
            //搜索结果状态信息
            RestStatus status = searchResponse.status();
            TimeValue took = searchResponse.getTook();
            Boolean terminatedEarly = searchResponse.isTerminatedEarly();
            boolean timedOut = searchResponse.isTimedOut();

            //分片搜索情况
            int totalShards = searchResponse.getTotalShards();
            int successfulShards = searchResponse.getSuccessfulShards();
            int failedShards = searchResponse.getFailedShards();
            for (ShardSearchFailure failure : searchResponse.getShardFailures()) {
                // failures should be handled here
            }

            //处理搜索命中文档结果
            SearchHits hits = searchResponse.getHits();

            long totalHits = hits.getTotalHits();
            float maxScore = hits.getMaxScore();

            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                // do something with the SearchHit

                String index = hit.getIndex();
                String type = hit.getType();
                String id = hit.getId();
                float score = hit.getScore();

                //取_source字段值
                String sourceAsString = hit.getSourceAsString(); //取成json串
                Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 取成map对象
                //从map中取字段值
                /*
                String documentTitle = (String) sourceAsMap.get("title");
                List<Object> users = (List<Object>) sourceAsMap.get("user");
                Map<String, Object> innerObject = (Map<String, Object>) sourceAsMap.get("innerObject");
                */
                System.out.println("index:" + index + "  type:" + type + "  id:" + id);
                System.out.println(sourceAsString);
//                logger.info("index:" + index + "  type:" + type + "  id:" + id);
//                logger.info(sourceAsString);

                //取高亮结果
                /*Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField highlight = highlightFields.get("title");
                Text[] fragments = highlight.fragments();
                String fragmentString = fragments[0].string();*/
            }

            // 获取聚合结果
            /*
            Aggregations aggregations = searchResponse.getAggregations();
            Terms byCompanyAggregation = aggregations.get("by_company");
            Bucket elasticBucket = byCompanyAggregation.getBucketByKey("Elastic");
            Avg averageAge = elasticBucket.getAggregations().get("average_age");
            double avg = averageAge.getValue();
            */

            // 获取建议结果
            /*Suggest suggest = searchResponse.getSuggest();
            TermSuggestion termSuggestion = suggest.getSuggestion("suggest_user");
            for (TermSuggestion.Entry entry : termSuggestion.getEntries()) {
                for (TermSuggestion.Entry.Option option : entry) {
                    String suggestText = option.getText().string();
                }
            }
            */

            //异步方式发送获查询请求
            /*
            ActionListener<SearchResponse> listener = new ActionListener<SearchResponse>() {
                @Override
                public void onResponse(SearchResponse getResponse) {
                    //结果获取
                }

                @Override
                public void onFailure(Exception e) {
                    //失败处理
                }
            };
            client.searchAsync(searchRequest, listener);
            */

        } catch (IOException e) {
//            logger.error(e);
        }
    }

    public static List<String> groupSearch(String indexName, String typeName,
                                   String fields,int from, int gte, int lte,int size) throws
            IOException {

        RestHighLevelClient client = InitClient.getClient();
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(typeName);



        // 2、用SearchSourceBuilder来构造查询请求体 ,请仔细查看它的方法，构造各种查询的方法都在这。
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder;
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("price").gte(0).lte(30000);

        Pattern pattern2 = Pattern.compile("(ctn)");
        Matcher matcher2=pattern2.matcher(fields);
        if (matcher2.find()){
            boolQueryBuilder= (BoolQueryBuilder) new SearchTitan
                    ().mustdBuild(fields);
        }else {
            boolQueryBuilder= (BoolQueryBuilder) new SearchTitan
                    ().createBuilder(fields);
        }

        boolQueryBuilder.must(rangeQueryBuilder);

        sourceBuilder.query(boolQueryBuilder);
//        sourceBuilder.query(rangeQueryBuilder);
        sourceBuilder.from(from);
        sourceBuilder.size(size);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        //处理搜索命中文档结果
        SearchHits hits = searchResponse.getHits();

        SearchHit[] searchHits = hits.getHits();
        List<String> list = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            // do something with the SearchHit

            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();

            String sourceAsString = hit.getSourceAsString(); //取成json串
            System.out.println("index:" + index + "  type:" + type + "  id:" + id);
            System.out.println(sourceAsString);
            list.add(sourceAsString);
        }
        client.close();


        return list;
    }

    public static List<String> esSearch(String indexName, String typeName,
                                        String fields, int from, int gte, int lte,int size) throws IOException {

        RestHighLevelClient client = InitClient.getClient();
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(typeName);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(fields,"title","one","gid","two","three","llen","brand");
        ((MultiMatchQueryBuilder) queryBuilder).minimumShouldMatch("75%");
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("price").gte(gte).lte(lte);
        boolQueryBuilder.must(queryBuilder);
        boolQueryBuilder.must(rangeQueryBuilder);

        sourceBuilder.query(boolQueryBuilder);
//        sourceBuilder.query(rangeQueryBuilder);
        sourceBuilder.from(from);
        sourceBuilder.size(size);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        //处理搜索命中文档结果
        SearchHits hits = searchResponse.getHits();

        SearchHit[] searchHits = hits.getHits();
        List<String> list = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            // do something with the SearchHit

            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();

            String sourceAsString = hit.getSourceAsString(); //取成json串
            System.out.println("index:" + index + "  type:" + type + "  id:" + id);
            System.out.println(sourceAsString);
            list.add(sourceAsString);
        }
        client.close();

        return list;
    }
}
