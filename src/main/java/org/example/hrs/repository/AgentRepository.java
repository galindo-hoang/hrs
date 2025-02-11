package org.example.hrs.repository;

import org.example.hrs.repository.model.AgentEntity;

import java.util.Optional;

public interface AgentRepository {
    Optional<AgentEntity> createAgent(AgentEntity agent);

    Optional<AgentEntity> findAgent(String username);

    Optional<AgentEntity> findAgentById(Long id);
}
