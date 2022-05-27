package com.elasticsearch.authentication.service.filter;

import com.elasticsearch.authentication.dto.ElasticFilter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.elasticsearch.index.query.Operator.AND;

@Service
public class HeaderSearchFilter implements SearchFilter {

    @Override
    public void filter(ElasticFilter elasticFilter, BoolQueryBuilder boolQueryBuilder) {
        Map<String, String> headerFilters = elasticFilter.getHeaders();

        if (headerFilters != null && !headerFilters.isEmpty()) {
            headerFilters.forEach((key, value) -> boolQueryBuilder.must(QueryBuilders.matchQuery( "headers." + key, value).operator(AND)));
        }
    }
}
