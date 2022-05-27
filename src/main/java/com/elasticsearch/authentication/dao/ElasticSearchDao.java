package com.elasticsearch.authentication.dao;

import com.elasticsearch.authentication.dto.AgentRequest;
import com.elasticsearch.authentication.dto.AgentResponse;
import com.elasticsearch.authentication.dto.ElasticFilter;
import com.elasticsearch.authentication.exception.LoggingException;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface ElasticSearchDao {

    List<Map<String, Object>> getFilteredData(ElasticFilter elasticFilter, int limit, String... indices) throws IOException, LoggingException;

  }
