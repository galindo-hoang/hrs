package org.example.hrs.service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {
    private String text;
    private Integer offset;
    private Integer limit;
}
