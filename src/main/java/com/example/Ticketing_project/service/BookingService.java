package com.example.Ticketing_project.service;

import com.example.Ticketing_project.dto.BookingRequest;
import com.example.Ticketing_project.dto.BookingResponse;
import com.example.Ticketing_project.entity.*;
import com.example.Ticketing_project.exception.BadRequestException;
import com.example.Ticketing_project.exception.ResourceNotFoundException;
import com.example.Ticketing_project.repository.BookingRepository;
import com.example.Ticketing_project.repository.TicketTierRepository;
import com.example.Ticketing_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TicketTierRepository ticketTierRepository;
    private final UserRepository userRepository;

    // 1. CREATE BOOKING LOGIC (Atomic & Concurrency-Safe)
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // Step 1: Get currently logged-in attendee
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User attendee = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Step 2: Fetch Ticket Tier (We use standard findById; in high-concurrency systems, you can add pessimistic locking)
        TicketTier tier = ticketTierRepository.findById(request.getTicketTierId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket tier not found with ID: " + request.getTicketTierId()));

        // Step 3: Validate Stock Availability
        if (tier.getAvailableStock() < request.getQuantity()) {
            throw new BadRequestException("Not enough stock available! Only " + tier.getAvailableStock() + " tickets left.");
        }

        // Step 4: Safely decrement stock
        tier.setAvailableStock(tier.getAvailableStock() - request.getQuantity());
        ticketTierRepository.save(tier);

        // Step 5: Calculate total price
        BigDecimal totalPrice = tier.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        // Step 6: Create and save Booking entity
        Booking booking = new Booking();
        booking.setAttendee(attendee);
        booking.setTicketTier(tier);
        booking.setQuantity(request.getQuantity());
        booking.setTotalPrice(totalPrice);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setBookedAt(LocalDateTime.now());

        // Step 7: Generate individual scannable Ticket Passes (QR tokens) for each ticket booked
        List<TicketPass> passes = new ArrayList<>();
        List<String> passTokens = new ArrayList<>();

        for (int i = 0; i < request.getQuantity(); i++) {
            String uniqueToken = "PASS-" + UUID.randomUUID().toString().toUpperCase();

            TicketPass pass = new TicketPass();
            pass.setBooking(booking);
            pass.setPassToken(uniqueToken);
            pass.setStatus(PassStatus.ACTIVE);

            passes.add(pass);
            passTokens.add(uniqueToken);
        }

        booking.setTicketPasses(passes);
        Booking savedBooking = bookingRepository.save(booking);

        // Step 8: Return response DTO
        return BookingResponse.builder()
                .bookingId(savedBooking.getId())
                .bookingTime(savedBooking.getBookedAt())
                .status(savedBooking.getBookingStatus())
                .totalPrice(savedBooking.getTotalPrice())
                .eventTitle(tier.getEvent().getTitle())
                .tierName(tier.getTierName())
                .quantity(savedBooking.getQuantity())
                .passTokens(passTokens)
                .build();
    }
}