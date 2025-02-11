package org.example.hrs.service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AgentDto {
    private Long id;
    private String username;
    private Token token;
}
