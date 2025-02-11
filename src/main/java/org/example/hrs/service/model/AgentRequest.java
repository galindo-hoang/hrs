package org.example.hrs.service.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgentRequest {
    private Long id;
    private String username;
    private String password;
}
