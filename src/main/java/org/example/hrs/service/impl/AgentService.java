package org.example.hrs.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.hrs.exception.base.BusinessException;
import org.example.hrs.repository.AgentRepository;
import org.example.hrs.repository.model.AgentEntity;
import org.example.hrs.service.IAgentService;
import org.example.hrs.service.model.AgentDto;
import org.example.hrs.service.model.AgentRequest;
import org.example.hrs.service.model.Token;
import org.example.hrs.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.example.hrs.utils.Utils.JWT_ACCESS_TOKEN_VALIDITY;
import static org.example.hrs.utils.Utils.JWT_REFRESH_TOKEN_VALIDITY;

@Service
@Transactional
@RequiredArgsConstructor
public class AgentService implements IAgentService {
    private final AgentRepository agentRepository;

    @Override
    public Boolean register(AgentRequest agentRequest) {
        Optional<AgentEntity> agent = agentRepository.findAgent(agentRequest.getUsername());
        if (agent.isPresent()) {
            throw new BusinessException("account is exist!");
        }
        String hashPassword = Utils.hashPassword(agentRequest.getPassword());

        Optional<AgentEntity> res = agentRepository
                .createAgent(
                        AgentEntity.builder()
                                .username(agentRequest.getUsername())
                                .password(hashPassword)
                                .build()
                );

        if (res.isEmpty()) {
            throw new BusinessException("Can't create account!");
        }
        return true;
    }

    @Override
    public AgentDto login(AgentRequest agentRequest) {
        Optional<AgentEntity> agent = agentRepository.findAgent(agentRequest.getUsername());

        if (agent.isEmpty() || !Utils.verifyPassword(agentRequest.getPassword(), agent.get().getPassword())) {
            throw new BusinessException("username or password is incorrect!");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", agent.get().getUsername());
        claims.put("id", agent.get().getId());

        String accessToken = Utils.generateToken(claims, agent.get().getUsername(), JWT_ACCESS_TOKEN_VALIDITY);
        String refreshToken = Utils.generateToken(new HashMap<>(), agent.get().getUsername(), JWT_REFRESH_TOKEN_VALIDITY);

        return AgentDto.builder()
                .id(agent.get().getId())
                .username(agent.get().getUsername())
                .token(Token.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build())
                .build();
    }
}
