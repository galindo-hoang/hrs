package org.example.hrs.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.hrs.exception.base.BusinessException;
import org.example.hrs.repository.AgentRepository;
import org.example.hrs.repository.BookingRepository;
import org.example.hrs.repository.HotelRepository;
import org.example.hrs.repository.UploadImageRepository;
import org.example.hrs.repository.model.AgentEntity;
import org.example.hrs.repository.model.BookingEntity;
import org.example.hrs.repository.model.HotelEntity;
import org.example.hrs.repository.model.Status;
import org.example.hrs.service.IHotelService;
import org.example.hrs.service.model.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class HotelService implements IHotelService {

    private final UploadImageRepository uploadImageRepository;
    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final AgentRepository agentRepository;

    @Override
    public HotelDto createHotel(HotelRequest hotelRequest) {
        Optional<AgentEntity> agent = agentRepository.findAgentById(hotelRequest.getUserId());
        if (agent.isEmpty()) {
            throw new BusinessException("user not found");
        }
        try {
            HotelEntity entity = HotelEntity
                    .builder()
                    .agent(agent.get())
                    .name(hotelRequest.getName())
                    .prices(hotelRequest.getPrices())
                    .latitude(hotelRequest.getLatitude())
                    .longitude(hotelRequest.getLongitude())
                    .description(hotelRequest.getDescription())
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .build();
            Optional<HotelEntity> res = hotelRepository.mergeHotel(entity);
            if (res.isEmpty()) {
                throw new BusinessException("can't create hotel");
            }
            String path = "hotel-" + res.get().getId();
            List<String> images = uploadImageRepository.save(path, hotelRequest.getImages());
            res.get().setImageUrl(path);
            hotelRepository.mergeHotel(res.get());
            return HotelDto.builder()
                    .hotelId(res.get().getId())
                    .hotelName(res.get().getName())
                    .description(res.get().getDescription())
                    .images(images)
                    .longitude(res.get().getLongitude())
                    .latitude(res.get().getLatitude())
                    .build();
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public List<HotelDto> getHotels(SearchRequest searchRequest) {
        Optional<List<HotelEntity>> hotels = hotelRepository.getListHotels(searchRequest.getText(), searchRequest.getOffset(), searchRequest.getLimit());
        if (hotels.isEmpty()) {
            throw new BusinessException("hotels not found");
        }
        return hotels.get().stream().map(hotel -> HotelDto
                .builder()
                .hotelId(hotel.getId())
                .prices(hotel.getPrices())
                .hotelName(hotel.getName())
                .latitude(hotel.getLatitude())
                .longitude(hotel.getLongitude())
                .description(hotel.getDescription())
                .build()).toList();
    }

    @Override
    public HotelDto getDetailHotel(Long id) {
        Optional<HotelEntity> hotel = hotelRepository.getHotelById(id);
        if (hotel.isEmpty()) {
            throw new BusinessException("hotel not found");
        }
        List<String> images = uploadImageRepository.getAllImages(hotel.get().getImageUrl());
        return HotelDto.builder()
                .hotelId(hotel.get().getId())
                .hotelName(hotel.get().getName())
                .description(hotel.get().getDescription())
                .longitude(hotel.get().getLongitude())
                .latitude(hotel.get().getLatitude())
                .prices(hotel.get().getPrices())
                .images(images)
                .build();
    }

    @Override
    public HotelDto updateHotel(HotelRequest hotelRequest) {
        Optional<HotelEntity> hotel = hotelRepository.getHotelById(hotelRequest.getHotelId());
        if (hotel.isEmpty() || !Objects.equals(hotel.get().getAgent().getId(), hotelRequest.getUserId())) {
            throw new BusinessException("hotel not found");
        }
        if (hotelRequest.getDescription() != null) {
            hotel.get().setDescription(hotelRequest.getDescription());
        }
        if (hotelRequest.getPrices() != null) {
            hotel.get().setPrices(hotelRequest.getPrices());
        }
        if (hotelRequest.getLatitude() != null) {
            hotel.get().setLatitude(hotelRequest.getLatitude());
        }
        if (hotelRequest.getLongitude() != null) {
            hotel.get().setLatitude(hotelRequest.getLongitude());
        }
        hotel = hotelRepository.mergeHotel(hotel.get());
        if (hotel.isEmpty()) {
            throw new BusinessException("can't update hotel");
        }
        return HotelDto.builder()
                .images(uploadImageRepository.getAllImages(hotel.get().getImageUrl()))
                .hotelId(hotel.get().getId())
                .hotelName(hotel.get().getName())
                .latitude(hotel.get().getLatitude())
                .longitude(hotel.get().getLongitude())
                .description(hotel.get().getDescription())
                .prices(hotel.get().getPrices())
                .build();
    }

    @Override
    public BookingDto createBooking(BookingRequest bookingRequest) {
        Optional<AgentEntity> agent = agentRepository.findAgentById(bookingRequest.getCustomerId());
        Optional<HotelEntity> hotel = hotelRepository.getHotelById(bookingRequest.getHotelId());
        if (agent.isEmpty() || hotel.isEmpty() || Objects.equals(hotel.get().getAgent().getId(), agent.get().getId())) {
            throw new BusinessException("can't booking");
        }
        Timestamp from = new Timestamp(bookingRequest.getDateStart() * 1000);
        Timestamp to = new Timestamp(bookingRequest.getDateEnd() * 1000);
        Optional<Integer> exists = bookingRepository.checkIfBookingExists(bookingRequest.getHotelId(), from, to);
        if (exists.isPresent() && exists.get() >= 1) {
            throw new BusinessException("booking already exists");
        }

        Optional<BookingEntity> entity = bookingRepository.mergeBooking(
                BookingEntity.builder()
                        .booking_start(from)
                        .booking_end(to)
                        .hotel(hotel.get())
                        .agent(agent.get())
                        .status(Status.CONFIRMED)
                        .build()
        );
        if (entity.isEmpty()) {
            throw new BusinessException("can't create booking");
        }
        return BookingDto.builder()
                .bookingId(entity.get().getId())
                .isBooked(entity.get().getStatus() == Status.CONFIRMED)
                .startTime(entity.get().getBooking_start().getTime())
                .endTime(entity.get().getBooking_end().getTime())
                .build();
    }

    @Override
    public BookingDto cancelBooking(CancelRequest cancelRequest) {
        Optional<BookingEntity> entity = bookingRepository.findBookingById(cancelRequest.getBookingId());
        if (entity.isEmpty()) {
            throw new BusinessException("booking not found");
        }
        if (entity.get().getStatus() != Status.CONFIRMED || !Objects.equals(entity.get().getAgent().getId(), cancelRequest.getCustomerId())) {
            throw new BusinessException("have something wrong");
        }
        entity.get().setStatus(Status.CANCELLED);
        entity = bookingRepository.mergeBooking(entity.get());
        if (entity.isEmpty()) {
            throw new BusinessException("Can't cancel booking");
        }
        return BookingDto
                .builder()
                .bookingId(entity.get().getId())
                .isBooked(true)
                .build();
    }
}
