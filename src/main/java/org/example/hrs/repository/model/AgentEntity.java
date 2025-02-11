package org.example.hrs.repository.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    // owner
    @OneToMany(mappedBy = "agent")
    private List<HotelEntity> hotels;
    // owner
    @OneToMany(mappedBy = "agent", orphanRemoval = true)
    private List<BookingEntity> bookings;
}
