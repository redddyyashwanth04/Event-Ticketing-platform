package com.example.Ticketing_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ticket_tiers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tierName; // e.g., "VIP", "General Admission"

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // Always use BigDecimal for money to avoid floating-point rounding errors!

    @Column(nullable = false)
    private Integer totalCapacity; // e.g., 100 max seats

    @Column(nullable = false)
    private Integer availableStock; // Decrements as bookings are made

    // Many ticket tiers belong to one Event
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
}
