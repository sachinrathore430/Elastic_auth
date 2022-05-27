package com.elasticsearch.authentication.service.filter;

import com.elasticsearch.authentication.dto.ElasticFilter;
import com.elasticsearch.authentication.dto.ErrorFilter;
import com.elasticsearch.authentication.util.Constant;
import com.elasticsearch.authentication.util.Predicates;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import static org.elasticsearch.index.query.Operator.AND;

@Service
public class ErrorSearchFilter implements SearchFilter {
    @Override
    public void filter(ElasticFilter elasticFilter, BoolQueryBuilder boolQueryBuilder) {

        ErrorFilter errorFilter = elasticFilter.getErrorFilter();

        if (Predicates.isNull.test(errorFilter))
            return;

        String errorCode = errorFilter.getErrorCode();
        String type = errorFilter.getType();

        String queryString = Constant.BUSINESS_ERROR.equalsIgnoreCase(type) ? "errorPayload.businessError.errorCode" : "errorPayload.systemError.errorCode";

        boolQueryBuilder.must(QueryBuilders.matchQuery(queryString, errorCode).operator(AND));

    }
}
