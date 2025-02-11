package org.example.hrs.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.example.hrs.repository.AgentRepository;
import org.example.hrs.repository.model.AgentEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AgentRepositoryMySql implements AgentRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AgentEntity> createAgent(AgentEntity agent) {
        return Optional.ofNullable(entityManager.merge(agent));

    }

    @Override
    public Optional<AgentEntity> findAgent(String username) {
        return entityManager
                .createNativeQuery("select * from user where username=?1", AgentEntity.class)
                .setParameter(1, username)
                .getResultList().stream().findFirst();
    }

    @Override
    public Optional<AgentEntity> findAgentById(Long id) {
        return Optional.ofNullable(entityManager.find(AgentEntity.class, id));
    }
}
