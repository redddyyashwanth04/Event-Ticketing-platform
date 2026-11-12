package com.example.Ticketing_project.controller;

import com.example.Ticketing_project.dto.BookingRequest;
import com.example.Ticketing_project.dto.BookingResponse;
import com.example.Ticketing_project.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // POST: /api/bookings (Restricted to users with ROLE_ATTENDEE)
    @PostMapping
    @PreAuthorize("hasRole('ATTENDEE')")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.ok(response);
    }
}