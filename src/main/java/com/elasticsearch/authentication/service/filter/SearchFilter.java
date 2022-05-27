package com.elasticsearch.authentication.service.filter;

import com.elasticsearch.authentication.dto.ElasticFilter;

import org.elasticsearch.index.query.BoolQueryBuilder;

public interface SearchFilter {

    void filter(ElasticFilter elasticFilter, BoolQueryBuilder boolQueryBuilder);
}
