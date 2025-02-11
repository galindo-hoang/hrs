package org.example.hrs.controller;

import lombok.RequiredArgsConstructor;
import org.example.hrs.service.impl.HotelService;
import org.example.hrs.service.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @PostMapping(value = "create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<HotelDto> createHotel(@ModelAttribute HotelRequest hotelRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(hotelService.createHotel(hotelRequest));
    }

    @GetMapping("search")
    ResponseEntity<List<HotelDto>> searchHotel(@RequestBody SearchRequest searchRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(hotelService.getHotels(searchRequest));
    }

    @GetMapping("/{id}")
    ResponseEntity<HotelDto> detailHotel(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(hotelService.getDetailHotel(id));
    }

    @PostMapping(value = "update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<HotelDto> updateHotel(@ModelAttribute HotelRequest hotelRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(hotelService.updateHotel(hotelRequest));
    }

    @PostMapping("booking")
    ResponseEntity<BookingDto> bookingHotel(@RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(hotelService.createBooking(bookingRequest));
    }

    @PostMapping("cancel")
    ResponseEntity<BookingDto> bookingHotel(@RequestBody CancelRequest cancelRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(hotelService.cancelBooking(cancelRequest));
    }
}
