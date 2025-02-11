package org.example.hrs.service;

import org.example.hrs.service.model.*;

import java.util.List;

public interface IHotelService {
    HotelDto createHotel(HotelRequest hotelRequest);

    List<HotelDto> getHotels(SearchRequest searchRequest);

    HotelDto getDetailHotel(Long id);

    HotelDto updateHotel(HotelRequest hotelRequest);

    BookingDto createBooking(BookingRequest bookingRequest);

    BookingDto cancelBooking(CancelRequest cancelRequest);
}
