package com.example.Ticketing_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many bookings belong to one Attendee (User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendee_id", nullable = false)
    private User attendee;

    // Many bookings can be made for a specific TicketTier
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_tier_id", nullable = false)
    private TicketTier ticketTier;

    @Column(nullable = false)
    private Integer quantity; // e.g., bought 3 tickets in this transaction

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice; // quantity * tier.price

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus bookingStatus; // PENDING, CONFIRMED, CANCELLED

    // One booking can generate multiple individual ticket passes
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketPass> ticketPasses = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime bookedAt;
}
