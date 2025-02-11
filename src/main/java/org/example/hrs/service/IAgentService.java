package org.example.hrs.service;

import org.example.hrs.service.model.AgentDto;
import org.example.hrs.service.model.AgentRequest;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface IAgentService {
    Boolean register(AgentRequest agentRequest) throws NoSuchAlgorithmException, InvalidKeySpecException;

    AgentDto login(AgentRequest agentRequest);
}
