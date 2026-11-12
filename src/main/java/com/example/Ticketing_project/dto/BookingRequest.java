
package com.example.Ticketing_project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {

    @NotNull(message = "Ticket tier ID is mandatory")
    private Long ticketTierId;

    @Min(value = 1, message = "You must book at least 1 ticket")
    private Integer quantity;
}
