package com.example.Ticketing_project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TicketTierRequest {

    @NotBlank(message = "Tier name is mandatory")
    private String tierName;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be positive or zero")
    private BigDecimal price;

    @Min(value = 1, message = "Total capacity must be at least 1")
    private Integer totalCapacity;
}
