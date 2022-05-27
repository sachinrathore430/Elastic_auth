package com.elasticsearch.authentication.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogs {

    String orgId;
    String appId;
    String name;
    String traceId;
    String requestTimestamp;
    String responseTimestamp;
    Double requestPayLoadSize;
    Double responsePayLoadSize;
    List<String> businessKey;
    String timeTaken;
    String spanId;
    String status;
    String loginId;

    public AuditLogs(Map<String, Object> stringObjectMap, ObjectMapper objectMapper) {

//        KafkaPayload kafkaPayload = objectMapper.convertValue(stringObjectMap, KafkaPayload.class);
//
//        this.orgId = kafkaPayload.getOrgId();
//        this.appId = kafkaPayload.getAppId();
//        this.name = kafkaPayload.getArn();
//        this.traceId = kafkaPayload.getTraceId();
//        this.spanId = kafkaPayload.getSpanId();
//
//        AuditPayload auditPayload = kafkaPayload.getAuditPayload();
//
//        this.timeTaken = kafkaPayload.getTimeTaken()+Constant.MS;
//        this.requestPayLoadSize = kafkaPayload.getRequestPayLoadSize();
//        this.responsePayLoadSize = kafkaPayload.getResponsePayLoadSize();
//        this.requestTimestamp = auditPayload.getRequestTimestamp() !=null ? Utility.UTC_STRING_TO_IST_STRING.apply(auditPayload.getRequestTimestamp().toString()) : null;
//        this.responseTimestamp = auditPayload.getResponseTimestamp() !=null ? Utility.UTC_STRING_TO_IST_STRING.apply(auditPayload.getResponseTimestamp().toString()) : null;
//        this.status = kafkaPayload.getStatus();
//        this.loginId = kafkaPayload.getLoginId();
//        this.businessKey = kafkaPayload.getBusinessFilter();
    }
}
