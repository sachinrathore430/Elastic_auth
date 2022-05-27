package com.elasticsearch.authentication.service;


import com.elasticsearch.authentication.dto.AgentRequest;
import com.elasticsearch.authentication.dto.AgentResponse;

public interface ElasticSearch {

//    AgentResponse getReqJourney(AgentRequest agentRequest) throws Exception;
//
//    AgentResponse getSystemTrace(AgentRequest agentRequest) throws Exception;
//
//    AgentResponse getErrorLogs(AgentRequest agentRequest) throws Exception;

    AgentResponse getAuditLogs(AgentRequest agentRequest) throws Exception;

//    AgentResponse getTextPayload(AgentRequest agentRequest) throws Exception;
//
//    AgentResponse getErrorPayloadLogs(AgentRequest agentRequest) throws Exception;
}
