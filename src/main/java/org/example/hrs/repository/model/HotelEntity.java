package org.example.hrs.repository.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "hotel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double prices;
    private Double latitude;
    private Double longitude;
    private String imageUrl;
    private Timestamp createdAt;

    //owner
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private AgentEntity agent;

    //booking
    @OneToMany(mappedBy = "hotel")
    private List<BookingEntity> bookings;
}
