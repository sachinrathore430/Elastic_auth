package com.elasticsearch.authentication.service.filter;


import com.elasticsearch.authentication.dto.ElasticFilter;
import com.elasticsearch.authentication.dto.RangeFilter;
import com.elasticsearch.authentication.dto.RangeFilterDashboard;
import com.elasticsearch.authentication.util.Predicates;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.Operator.AND;

@Service
public class BasicSearchFilter implements SearchFilter {

    @Override
    public void filter(ElasticFilter elasticFilter, BoolQueryBuilder nativeSearchQueryBuilder) {

        String arn = elasticFilter.getArn();
        String loginId = elasticFilter.getLoginId();
        String spanId = elasticFilter.getSpanId();
        String status = elasticFilter.getStatus();
        String timeTaken = elasticFilter.getTimeTaken();
        String traceId = elasticFilter.getTraceId();
        List<String> customBusinessFilter=elasticFilter.getBusinessFilter();

        String systemName=elasticFilter.getSystemName();
        RangeFilter requestTimestamp = elasticFilter.getRequestTimestamp();
        RangeFilterDashboard rangeFilterDashboard=elasticFilter.getRangeFilterDashboard();

        if (Predicates.isNotNullAndNotEmpty.test(arn))
            nativeSearchQueryBuilder.must(QueryBuilders.matchQuery("arn", arn).operator(AND));

        if (Predicates.isNotNullAndNotEmpty.test(loginId))
            nativeSearchQueryBuilder.must(QueryBuilders.matchQuery("loginId", loginId).operator(AND));

        if (Predicates.isNotNullAndNotEmpty.test(spanId))
            nativeSearchQueryBuilder.must(QueryBuilders.matchQuery("spanId", spanId).operator(AND));

        if (Predicates.isNotNullAndNotEmpty.test(status))
            nativeSearchQueryBuilder.must(QueryBuilders.matchQuery("status", status).operator(AND));

        if (Predicates.isNotNullAndNotEmpty.test(timeTaken))
            nativeSearchQueryBuilder.must(QueryBuilders.rangeQuery("timeTaken").gt(timeTaken));

        if (Predicates.isNotNullAndNotEmpty.test(traceId))
            nativeSearchQueryBuilder.must(QueryBuilders.matchQuery("traceId", traceId).operator(AND));

        if (Predicates.isNotNullAndNotEmptyList.test(customBusinessFilter))
            QueryBuilders.termsQuery("businessFilter", customBusinessFilter);


        if (!Predicates.isNull.test(requestTimestamp)) {
            String from = requestTimestamp.getFrom();
            String to = requestTimestamp.getTo();

//            from = Utility.IST_STRING_TO_UTC_STRING.apply(from);
//            to = Utility.IST_STRING_TO_UTC_STRING.apply(to);

            if (Predicates.isNotNullAndNotEmpty.test(from) && Predicates.isNotNullAndNotEmpty.test(to)) {
                nativeSearchQueryBuilder.must(QueryBuilders.rangeQuery("requestTimestamp").from(from).to(to).timeZone("+05:30"));
            }
        }
        if (!Predicates.isNull.test(rangeFilterDashboard)) {
            String from = rangeFilterDashboard.getFromTimestamp();
            String to = rangeFilterDashboard.getToTimestamp();

//            from = Utility.IST_STRING_TO_UTC_STRING.apply(from);
//            to = Utility.IST_STRING_TO_UTC_STRING.apply(to);

            if (Predicates.isNotNullAndNotEmpty.test(from) && Predicates.isNotNullAndNotEmpty.test(to)) {
                nativeSearchQueryBuilder.must(QueryBuilders.rangeQuery("requestTimestamp").from(from).to(to).timeZone("+05:30"));
            }
        }

        if(Predicates.isNotNullAndNotEmpty.test(systemName))
            nativeSearchQueryBuilder.must(QueryBuilders.matchQuery("systemName",systemName).operator(AND));

    }
}
