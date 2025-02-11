package org.example.hrs.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.example.hrs.repository.HotelRepository;
import org.example.hrs.repository.model.HotelEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HotelRepositoryMySql implements HotelRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<HotelEntity> mergeHotel(HotelEntity hotel) {
        return Optional.ofNullable(em.merge(hotel));
    }

    @Override
    public Optional<List<HotelEntity>> getListHotels(String name, int offset, int limit) {
        return Optional.ofNullable(em.createNativeQuery("select * from hotel where name LIKE ?1 offset ?2 limit ?3", HotelEntity.class)
                .setParameter(1, "%" + name + "%")
                .setParameter(2, offset)
                .setParameter(3, limit)
                .getResultList());
    }

    @Override
    public Optional<HotelEntity> getHotelById(Long id) {
        return Optional.ofNullable(em.find(HotelEntity.class, id));
    }
}
