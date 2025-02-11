package org.example.hrs.service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookingDto {
    private Long bookingId;
    private Boolean isBooked;
    private Long startTime;
    private Long endTime;
}
