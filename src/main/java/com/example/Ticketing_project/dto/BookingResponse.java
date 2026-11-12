package com.example.Ticketing_project.dto;

import com.example.Ticketing_project.entity.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BookingResponse {
    private Long bookingId;
    private LocalDateTime bookingTime;
    private BookingStatus status;
    private BigDecimal totalPrice;
    private String eventTitle;
    private String tierName;
    private Integer quantity;
    private List<String> passTokens; // Unique QR tokens for venue entry
}