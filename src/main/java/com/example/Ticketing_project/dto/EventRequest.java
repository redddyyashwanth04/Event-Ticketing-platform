package com.example.Ticketing_project.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventRequest {

    @NotBlank(message = "Event title is mandatory")
    private String title;

    private String description;

    @NotBlank(message = "Location is mandatory")
    private String location;

    @NotNull(message = "Start time is mandatory")
    @Future(message = "Event start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is mandatory")
    @Future(message = "Event end time must be in the future")
    private LocalDateTime endTime;

    @NotEmpty(message = "An event must have at least one ticket tier")
    @Valid
    private List<TicketTierRequest> ticketTiers;
}