package com.elasticsearch.authentication.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class AgentRequest {

    String orgId;
    String appId;
    String systemName;
    String traceId;
    String type;
    int limit;
    ElasticFilter filter;


}
