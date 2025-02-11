package org.example.hrs.service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelRequest {
    private Long customerId;
    private Long bookingId;
    private String reason;
}
