package com.elasticsearch.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RangeFilterDashboard {

    private String fromTimestamp;
    private String toTimestamp;
}
