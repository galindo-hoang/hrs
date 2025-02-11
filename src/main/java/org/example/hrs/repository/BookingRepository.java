package org.example.hrs.repository;

import org.example.hrs.repository.model.BookingEntity;
import org.example.hrs.repository.model.Status;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Optional<BookingEntity> mergeBooking(BookingEntity bookingEntity);

    Optional<BookingEntity> findBookingById(Long id);

    Optional<Integer> checkIfBookingExists(Long id, Timestamp startDate, Timestamp endDate);

    Optional<List<BookingEntity>> getBookingsWithHotelId(Long id, Timestamp from, Status status);
}
