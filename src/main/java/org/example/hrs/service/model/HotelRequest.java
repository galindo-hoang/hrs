package org.example.hrs.service.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class HotelRequest {
    private Long userId;
    private String name;
    private Double latitude;
    private Double longitude;
    private String description;
    private List<MultipartFile> images;
    private Double prices;

    private Long hotelId;
}
