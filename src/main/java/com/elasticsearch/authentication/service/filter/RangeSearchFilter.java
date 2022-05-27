package com.elasticsearch.authentication.service.filter;

import com.elasticsearch.authentication.dto.ElasticFilter;
import com.elasticsearch.authentication.dto.RangeFilter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RangeSearchFilter implements SearchFilter {

    @Override
    public void filter(ElasticFilter elasticFilter, BoolQueryBuilder boolQueryBuilder) {

        List<RangeFilter> rangeFilterMap = elasticFilter.getRangeFilter();

        if (rangeFilterMap == null) {
            return;
        }

        for (RangeFilter entry : rangeFilterMap) {

            String from = entry.getFrom();
            String to = entry.getTo();

            if (from != null && !from.isEmpty() && (to != null && !to.isEmpty())) {
                boolQueryBuilder.should(QueryBuilders.rangeQuery(entry.getKey()).from(from).to(to).timeZone("+05:30"));
            }
        }

    }
}
