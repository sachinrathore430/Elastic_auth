package com.elasticsearch.authentication.dto;

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
public class ElasticFilter
{
    private String arn;
    private String status;
    private String spanId;
    private String loginId;
    private String timeTaken;
    private String traceId;
    private String systemName;
    private String date;
    private String dateFormat;
    private String interval;
    private List<String> businessFilter; // todo array
    private RangeFilter requestTimestamp;
    private RangeFilterDashboard rangeFilterDashboard;
    private ErrorFilter errorFilter;
    private Map<String, String> headers;
    private Map<String, String> filters;
    private List<RangeFilter> rangeFilter;

    public List<RangeFilter> getRangeFilter() {
        return rangeFilter;
    }
}
