package org.example.hrs.service.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class HotelDto {
    private Long hotelId;
    private String hotelName;
    private String description;
    private List<String> images;
    private Double latitude;
    private Double longitude;
    private Double prices;
}
