package org.example.hrs.service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {
    private Long hotelId;
    private Long customerId;
    private Long dateStart;
    private Long dateEnd;
    private String email;
    private String phone;
    private String name;
}
