package org.example.hrs.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.example.hrs.repository.BookingRepository;
import org.example.hrs.repository.model.BookingEntity;
import org.example.hrs.repository.model.Status;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryMySql implements BookingRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<BookingEntity> mergeBooking(BookingEntity bookingEntity) {
        return Optional.ofNullable(em.merge(bookingEntity));
    }

    @Override
    public Optional<BookingEntity> findBookingById(Long id) {
        return Optional.ofNullable(em.find(BookingEntity.class, id));
    }

    @Override
    public Optional<Integer> checkIfBookingExists(Long id, Timestamp startDate, Timestamp endDate) {
        return Optional.of(em.createNativeQuery("SELECT * FROM hotel_booking WHERE hotel_id = ?1 AND status = ?2 AND ((booking_end >= ?3 AND booking_start <= ?3) OR (booking_end >= ?4 AND booking_start <= ?4) OR (booking_start >= ?3 AND booking_end <= ?4))", BookingEntity.class)
                .setParameter(1, id)
                .setParameter(2, Status.CONFIRMED.name())
                .setParameter(3, startDate)
                .setParameter(4, endDate)
                .getResultList().size());
    }

    @Override
    public Optional<List<BookingEntity>> getBookingsWithHotelId(Long hotelId, Timestamp date_end, Status status) {
        return Optional.ofNullable(
                em.createNativeQuery("select * from hotel_booking where hotel_id=?1 and booking_end >= ?2 and status = ?3", BookingEntity.class)
                        .setParameter(1, hotelId)
                        .setParameter(2, date_end)
                        .setParameter(3, status.name())
                        .getResultList()
        );
    }
}
