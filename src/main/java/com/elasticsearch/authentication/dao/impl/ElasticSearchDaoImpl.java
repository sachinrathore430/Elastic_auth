package com.elasticsearch.authentication.dao.impl;

import com.elasticsearch.authentication.dao.ElasticSearchDao;
import com.elasticsearch.authentication.dto.ElasticFilter;
import com.elasticsearch.authentication.exception.LoggingException;
import com.elasticsearch.authentication.service.filter.SearchFilter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.java.Log;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Log
public class ElasticSearchDaoImpl implements ElasticSearchDao {

    private RestHighLevelClient client;

    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("basicSearchFilter")
    SearchFilter basicFilter;

    @Autowired
    @Qualifier("errorSearchFilter")
    SearchFilter errorFilter;

    @Autowired
    @Qualifier("headerSearchFilter")
    SearchFilter headerFilter;

    @Autowired
    @Qualifier("rangeSearchFilter")
    SearchFilter rangeFilters;

    @Autowired
    @Qualifier("businessSearchFilter")
    SearchFilter businessFilter;

    @Value("${logs.onetime.limit}")
    int defaultOneTimeExcelLimit;


    @Autowired
    public ElasticSearchDaoImpl(ObjectMapper objectMapper, RestHighLevelClient client) {
        this.objectMapper = objectMapper;
        this.client = client;
    }

    @Override
    public List<Map<String, Object>> getFilteredData(ElasticFilter elasticFilter, int limit, String... indices) throws IOException, LoggingException {

        log.info("In elastic search dao imp");

        log.info("In elastic search dao imp : getFilteredData");
           SearchRequest searchRequest = new SearchRequest(indices);

           SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
           searchSourceBuilder.query(QueryBuilders.matchAllQuery());

           BoolQueryBuilder builder1 = getFilteredRequest(elasticFilter);

           if (limit != 0) {
               searchSourceBuilder.from(0);
               searchSourceBuilder.size(limit);

           }

           searchSourceBuilder.query(builder1).sort("requestTimestamp", SortOrder.DESC);
           searchRequest.source(searchSourceBuilder);

           log.info("calling client search --------------------");
           SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

           SearchHits hits = response.getHits();

           if (hits != null) {
               SearchHit[] records = hits.getHits();

               return Arrays.stream(records).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
           }


        return null;
    }

//    @Override
//    public SearchResultsPage getExcelData(ElasticFilter elasticFilter, int limit, Object[] sortValues, String... indices) throws IOException {
//
//
//        SearchRequest searchRequest = new SearchRequest(indices);
//
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
//
//        BoolQueryBuilder builder1 = getFilteredRequest(elasticFilter);
//
//        searchSourceBuilder.from(0);
//
//        if(limit!=0)
//            searchSourceBuilder.size(limit);
//        else
//            searchSourceBuilder.size(defaultOneTimeExcelLimit);
//
//
//        searchSourceBuilder.query(builder1).sort("requestTimestamp", SortOrder.DESC);
//
//        System.out.println("------------------Setting Sort Values---------------");
//
//
//        if(sortValues!=null) {
//            for (Object obj : sortValues) {
//                System.out.println(obj);
//            }
//            searchSourceBuilder.searchAfter(sortValues);
//        }
//
//        searchRequest.source(searchSourceBuilder);
//
//        System.out.println("----------------------------Search API-------------------------------");
//
//        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
//
//        SearchHits searchHits = response.getHits();
//
//        SearchResultsPage searchResultsPage=new SearchResultsPage();
//
//        if(searchHits!=null && searchHits.getHits().length>0){
//            SearchHit[] records = searchHits.getHits();
//
//            if(records.length <  defaultOneTimeExcelLimit) {
//                System.out.println("-------------Setting Last Page-------------------------");
//                searchResultsPage.setLastPage(true);
//            }
//
//            System.out.println("---------Length---------"+records.length);
//
//            Object[] sortValues1 = searchHits.getAt(records.length-1).getSortValues();
//            searchResultsPage.setSortValues(sortValues1);
//
//            System.out.println("-----Sort Values----------");
//            for(Object obj : sortValues1){
//                System.out.println(obj);
//            }
//
//            searchResultsPage.setContent(Arrays.stream(records).map(SearchHit::getSourceAsMap).collect(Collectors.toList()));
//
//            return searchResultsPage;
//        }
//
//        return null;
//
//    }
//    @Override
//
//    public void deleteRecords(String timeStamp) throws IOException {
//
//        String index = Utility.getStringJoinerObj(Constant.DELIMITER, Constant.ASTRISK, Constant.ASTRISK, Constant.ASTRISK, Constant.ASTRISK,Constant.ASTRISK);
//
//        DeleteByQueryRequest deleteByQueryRequest =new DeleteByQueryRequest(index).setQuery(QueryBuilders.matchAllQuery());
//
//        BoolQueryBuilder builder1 = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("purgeTimestamp").lte(timeStamp));
//
//        deleteByQueryRequest.setQuery(builder1);
//
//        BulkByScrollResponse bulkResponse = client.deleteByQuery(deleteByQueryRequest,RequestOptions.DEFAULT);
//
//        long noOfDeleted=bulkResponse.getDeleted();
//
//        System.out.println("------------------"+noOfDeleted+" Records Deleted---------------------------------");
//
//    }
//
//
//    @Override
//    public String getTotalRecordsCountOfIndex(String index) throws IOException {
//        RestClient lowLevelClient = client.getLowLevelClient();
//
//        Response res = lowLevelClient.performRequest("GET", index + "/_count");
//
//        String stringResponse = EntityUtils.toString(res.getEntity());
//
//        ObjectNode jsonNodes = objectMapper.readValue(stringResponse, ObjectNode.class);
//
//        JsonNode count = jsonNodes.get(Constant.COUNT);
//
//        return count.asText();
//    }
//
//    @Override
//    public String[] getIndexesByPattern(String... args) throws IOException, LoggingException {
//
//        String index = Utility.getStringJoinerObj(Constant.DELIMITER, args);
//
//        GetIndexRequest getIndexRequest = new GetIndexRequest().indices(index + Constant.DELIMITER + Constant.ASTRISK);
//
//        GetIndexResponse response = client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
//
//        if (response.getIndices().length == 0) {
//            throw new LoggingException(Constant.VALIDATION_DATA_NOT_FOUND, Constant.NO_DATA + index, null);
//
//        }
//        return response.getIndices();
//
//    }
//
//    @Override
//    public List<CountOverTime> getCountOverTimeByHours(String index, ElasticFilter elasticFilter, int hoursUnit, String fieldName) throws IOException, LoggingException {
//
//        SearchRequest searchRequest = new SearchRequest(index);
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
//
//        DateHistogramAggregationBuilder aggregation = AggregationBuilders.
//                dateHistogram("hits_over_time").
//                field(fieldName).
//                dateHistogramInterval(DateHistogramInterval.hours(hoursUnit));
//
//        searchSourceBuilder.aggregation(aggregation);
//
//        BoolQueryBuilder filteredRequest = getFilteredRequest(elasticFilter);
//
//        searchSourceBuilder.query(filteredRequest);
//        searchSourceBuilder.size(0);
//        searchRequest.source(searchSourceBuilder);
//
//        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
//
//        if(response.getAggregations()==null)
//        {
//            throw new LoggingException(Constant.VALIDATION_DATA_NOT_FOUND,Constant.NO_DATA + index,null);
//        }
//
//        Aggregations aggregations = response.getAggregations();
//
//        ParsedDateHistogram filters = aggregations.get("hits_over_time");
//
//        List<CountOverTime> overTimes = new ArrayList<>();
//
//        for (Histogram.Bucket entry : filters.getBuckets()) {
//            overTimes.add(new CountOverTime(Utility.getHourFromTime(entry.getKeyAsString()), String.valueOf(entry.getDocCount()), Utility.UTC_STRING_TO_IST_STRING.apply(entry.getKeyAsString())));
//        }
//
//        return overTimes;
//    }
//
//    @Override
//    public AgentResponse getAggregateApis(AgentRequest agentRequest, String index, String keyName) throws IOException, LoggingException {
//
//        SearchRequest searchRequest = new SearchRequest(index);
//
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//
//        BoolQueryBuilder builder1 = getFilteredRequest(agentRequest.getFilter());
//
//        TermsAggregationBuilder field = AggregationBuilders.terms("group_by_arn").field(keyName);
//
//        searchSourceBuilder.aggregation(field);
//        searchSourceBuilder.query(builder1);
//
//        searchRequest.source(searchSourceBuilder);
//
//        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
//        if (response.getAggregations()== null) {
//            throw new LoggingException(Constant.VALIDATION_DATA_NOT_FOUND, Constant.NO_DATA + index, null);
//        }
//
//        Aggregations aggregations = response.getAggregations();
//
//        ParsedStringTerms group_by_arn = aggregations.get("group_by_arn");
//
//
//        List<AggregateResponse> mostFaultyApis = group_by_arn.getBuckets().stream().map(bucket -> new AggregateResponse(bucket.getKeyAsString(), bucket.getDocCount())).collect(Collectors.toList());
//
//        return Utility.createResponseObject(Constant.STATUS_SUCCESS, Constant.SUCCESS_MSG, mostFaultyApis);
//    }
//
//
//
//
//    @Override
//    public AgentResponse getTimeTakingApis(AgentRequest agentRequest, String index) throws IOException, LoggingException {
//        SearchRequest searchRequest = new SearchRequest(index);
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        BoolQueryBuilder builder = getFilteredRequest(agentRequest.getFilter());
//        TermsAggregationBuilder field = AggregationBuilders.terms("group_by_arn").field("arn.keyword")
//                .subAggregation(AggregationBuilders.max("maxTimeTaken").field("timeTaken"));
//
//
//        searchSourceBuilder.aggregation(field);
//        searchSourceBuilder.query(builder);
//
//        searchRequest.source(searchSourceBuilder);
//
//        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
//
//        if(response.getAggregations()==null)
//        {
//            throw new LoggingException(Constant.VALIDATION_DATA_NOT_FOUND,Constant.NO_DATA + index,null);
//        }
//
//        Aggregations aggregations = response.getAggregations();
//
//        ParsedStringTerms max_Time = aggregations.get("group_by_arn");
//
//        List<AggregateResponse> mostTimeTaking = max_Time.getBuckets().stream().map(bucket -> {
//            String apiName = bucket.getKeyAsString();
//
//            long docCount = bucket.getDocCount();
//
//            ParsedMax maxTimeTaken = bucket.getAggregations().get("maxTimeTaken");
//
//            return new AggregateResponse(apiName, docCount, maxTimeTaken.getValue());
//
//        }).collect(Collectors.toList());
//
//        return Utility.createResponseObject(Constant.STATUS_SUCCESS, Constant.SUCCESS_MSG, mostTimeTaking);
//
//    }
//
//    @Override
//    public AgentResponse getAvgTimeTaken(AgentRequestForVmonitor agentRequest, String index) throws IOException, LoggingException {
//
//        SearchRequest searchRequest = new SearchRequest(index);
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//
//        int totalMaxTime = 0;
//        Map<String, Object> responseMap = new LinkedHashMap<>();
//
//        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
//                .must(QueryBuilders.termQuery("headers.servicename.keyword", agentRequest.getServiceName()));
//
//        TermsAggregationBuilder field = AggregationBuilders.terms("group_by_arn").field("arn.keyword")
//                .subAggregation(AggregationBuilders.max("maxTimeTaken").field("timeTaken"));
//
//        searchSourceBuilder.aggregation(field);
//        searchSourceBuilder.query(queryBuilder);
//
//        searchRequest.source(searchSourceBuilder);
//
//        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
//
//        if (response.getAggregations() == null) {
//            throw new LoggingException(Constant.VALIDATION_DATA_NOT_FOUND, Constant.NO_DATA + index, null);
//        }
//
//        SearchHits hits = response.getHits();
//        if (hits != null) {
//            SearchHit[] records = hits.getHits();
//
//            List<Map<String, Object>> collect = Arrays.stream(records).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
//
//
//            for (Map<String, Object> mapData : collect) {
//                totalMaxTime = totalMaxTime + (int) mapData.get("timeTaken");
//
//            }
//
//            double avgTime = 0;
//            avgTime = totalMaxTime / 10;
//            responseMap.put("serviceName",agentRequest.getServiceName());
//            responseMap.put("avgTimeTaken",avgTime);
//            System.out.println(responseMap);
//
//        }
//        return Utility.createResponseObject(Constant.STATUS_SUCCESS, Constant.SUCCESS_MSG, responseMap);
//    }
//
    public BoolQueryBuilder getFilteredRequest(ElasticFilter elasticFilter) {

        BoolQueryBuilder builder1 = QueryBuilders.boolQuery();

        if (elasticFilter != null) {

            basicFilter.filter(elasticFilter, builder1);
            businessFilter.filter(elasticFilter, builder1);
            rangeFilters.filter(elasticFilter, builder1);
            errorFilter.filter(elasticFilter, builder1);
            headerFilter.filter(elasticFilter, builder1);
        }

        return builder1;
    }
//
//    public AgentResponse getAggregateApisAsErrorCodeCount(AgentRequest agentRequest, String index, String keyName) throws IOException, LoggingException {
//
//        SearchRequest searchRequest = new SearchRequest(index);
//
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//
//        BoolQueryBuilder builder1 = getFilteredRequest(agentRequest.getFilter());
//
//        TermsAggregationBuilder field = AggregationBuilders.terms("group_by_arn").field(keyName);
//
//        searchSourceBuilder.aggregation(field);
//        searchSourceBuilder.query(builder1);
//
//        searchRequest.source(searchSourceBuilder);
//
//        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
//
//        Aggregations aggregations = response.getAggregations();
//        if(aggregations==null)
//        {
//            throw new LoggingException(Constant.VALIDATION_DATA_NOT_FOUND,Constant.NO_DATA + index,null);
//        }
//
//        ParsedStringTerms group_by_arn = aggregations.get("group_by_arn");
//
//
//        List<ErrorCountBySystem> mostFaultyApis = group_by_arn.getBuckets().stream().map(bucket -> new ErrorCountBySystem(bucket.getKeyAsString(), String.valueOf(bucket.getDocCount()))).collect(Collectors.toList());
//
//        return Utility.createResponseObject(Constant.STATUS_SUCCESS, Constant.SUCCESS_MSG, mostFaultyApis);
//
//    }
}
