package com.workonenight.winteambe.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterCondition {
    private String field;
    private FilterOperationEnum operator;
    private Object value;
}
