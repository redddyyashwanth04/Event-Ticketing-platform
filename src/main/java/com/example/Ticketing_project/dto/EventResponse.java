package com.example.Ticketing_project.dto;

import com.example.Ticketing_project.entity.EventStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private EventStatus status;
    private String organizerName;
    private List<TicketTierDto> ticketTiers;

    @Data
    @Builder
    public static class TicketTierDto {
        private Long id;
        private String tierName;
        private BigDecimal price;
        private Integer totalCapacity;
        private Integer availableStock;
    }
}