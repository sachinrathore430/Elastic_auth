package com.elasticsearch.authentication.controller;

import com.elasticsearch.authentication.dto.AgentRequest;
import com.elasticsearch.authentication.dto.AgentResponse;
import com.elasticsearch.authentication.service.ElasticSearch;
import lombok.extern.java.Log;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("elastic/get/")
@Log
public class DataController {

    @Autowired
    ElasticSearch elasticSearchService;

    @Autowired
    RestHighLevelClient client;

//    @Autowired
//    Validator validator;

    @PostMapping("all/audit")
    public void getAllAuditLogs(@RequestBody AgentRequest agentRequest) throws Exception {
//        validator.validatePullRequest(agentRequest);
        log.info("in controller");

        CreateIndexRequest request = new CreateIndexRequest("articles");
        CreateIndexResponse createIndexResponse = client.indices().create(request);

        IndexRequest request1 = new IndexRequest(
                "articles",
                "doc",
                "1");
        String jsonString = "{" +
                "\"author\":\"john ronald reuel tolkien\"," +
                "\"content\":\"the lord of the rings is an epic high fantasy novel\"," +
                "}";
        request1.source(jsonString, XContentType.JSON);
        IndexResponse indexResponse = client.index(request1);
        client.ping();
//        return elasticSearchService.getAuditLogs(agentRequest);
    }
}
