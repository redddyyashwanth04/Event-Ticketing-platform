package com.example.Ticketing_project.entity;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_passes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketPass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many individual passes belong to one Booking transaction
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(unique = true, nullable = false, length = 100)
    private String passToken; // Unique UUID/token for QR code generation & scanning

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PassStatus status; // ACTIVE, CHECKED_IN, REVOKED

    private LocalDateTime checkedInAt; // Timestamp when scanned at the venue gate
}
