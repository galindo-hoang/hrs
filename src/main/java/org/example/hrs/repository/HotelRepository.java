package org.example.hrs.repository;

import org.example.hrs.repository.model.HotelEntity;

import java.util.List;
import java.util.Optional;

public interface HotelRepository {
    Optional<HotelEntity> mergeHotel(HotelEntity hotel);

    Optional<List<HotelEntity>> getListHotels(String name, int offset, int limit);

    Optional<HotelEntity> getHotelById(Long id);
}
