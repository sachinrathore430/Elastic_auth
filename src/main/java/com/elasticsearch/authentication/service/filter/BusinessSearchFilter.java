package com.elasticsearch.authentication.service.filter;

import com.elasticsearch.authentication.dto.ElasticFilter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.elasticsearch.index.query.Operator.AND;

@Service
public class BusinessSearchFilter implements SearchFilter {

    @Override
    public void filter(ElasticFilter elasticFilter, BoolQueryBuilder boolQueryBuilder) {

        Map<String, String> filters = elasticFilter.getFilters();

        if (filters != null && !filters.isEmpty()) {
            filters.forEach((key, value) -> boolQueryBuilder.must(QueryBuilders.matchQuery(key, value).operator(AND)));
        }
    }
}
