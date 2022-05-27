package com.elasticsearch.authentication.serviceImpl;

import com.elasticsearch.authentication.dao.ElasticSearchDao;
import com.elasticsearch.authentication.dto.AgentRequest;
import com.elasticsearch.authentication.dto.AgentResponse;
import com.elasticsearch.authentication.dto.AuditLogs;
import com.elasticsearch.authentication.dto.ElasticFilter;
import com.elasticsearch.authentication.enums.PayloadType;
import com.elasticsearch.authentication.model.headers.AuditTemplate;
import com.elasticsearch.authentication.service.ElasticSearch;
import com.elasticsearch.authentication.util.Constant;
import com.elasticsearch.authentication.util.Predicates;
import com.elasticsearch.authentication.util.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Log
public class ElasticSearchImpl implements ElasticSearch {

    @Autowired
    ElasticSearchDao elasticSearchConnector;

    @Autowired
    RestHighLevelClient client;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${limit}")
    int defaultLimit;

    @Autowired
    AuditTemplate auditTemplate;

//    @Autowired
//    ErrorTemplate errorTemplate;
//
//    @Autowired
//    TextPayloadTemplate textPayloadTemplate;


    private Supplier<ElasticFilter> ELASTIC_FILTER_SUPPLIER = ElasticFilter::new;

    private List<Map<String, Object>> getAllTypesLogs(AgentRequest agentRequest) throws Exception {


        log.info("in getAllTypesLogs method _________________");
        String index = Utility.getStringJoinerObj(Constant.DELIMITER, agentRequest.getOrgId(), agentRequest.getAppId(), agentRequest.getType(), agentRequest.getSystemName(),Constant.ASTRISK);

        int limit = agentRequest.getLimit();

        if (limit == 0) {
            agentRequest.setLimit(defaultLimit);
        }

        List<Map<String, Object>> filteredData = elasticSearchConnector.getFilteredData(agentRequest.getFilter(), agentRequest.getLimit(), index);

        Utility.isTrue(Predicates.isNullOrEmptyList.test(filteredData), Constant.FAILURE_MSG);

        return filteredData;
    }





    @Override
    public AgentResponse getAuditLogs(AgentRequest agentRequest) throws Exception {
        log.info("In Elastic search impl __________________");
        agentRequest.setSystemName(Constant.API_GATEWAY);
        agentRequest.setType(PayloadType.audit.name());

        String auditIndex = Utility.getStringJoinerObj(Constant.DELIMITER, agentRequest.getOrgId(), agentRequest.getAppId(), PayloadType.audit.name(), agentRequest.getSystemName(),Constant.ASTRISK);

        String errorIndex = Utility.getStringJoinerObj(Constant.DELIMITER, agentRequest.getOrgId(), agentRequest.getAppId(), PayloadType.error.name(), agentRequest.getSystemName(),Constant.ASTRISK);

        List<Map<String, Object>> allTypesLogs = getAllTypesLogs(agentRequest);

        List<AuditLogs> auditLogs = allTypesLogs.stream().map(stringObjectMap -> new AuditLogs(stringObjectMap, objectMapper)).sorted(Comparator.comparing(AuditLogs::getRequestTimestamp).reversed()).collect(Collectors.toList());

        AgentResponse response = Utility.createResponseObject(Constant.STATUS_SUCCESS, "Records received successfully", auditLogs);

        response.setHeaders(auditTemplate.getHeaders());

        return response;
    }


}

