package com.example.Ticketing_project.service;

import com.example.Ticketing_project.dto.ScanResponse;
import com.example.Ticketing_project.entity.PassStatus;
import com.example.Ticketing_project.entity.TicketPass;
import com.example.Ticketing_project.exception.BadRequestException;
import com.example.Ticketing_project.exception.ResourceNotFoundException;
import com.example.Ticketing_project.repository.TicketPassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScanService {

    private final TicketPassRepository ticketPassRepository;

    // 1. SCAN & VALIDATE TICKET PASS LOGIC
    @Transactional
    public ScanResponse scanPass(String passToken) {
        // Step 1: Look up ticket pass by unique QR token string
        TicketPass pass = ticketPassRepository.findByPassToken(passToken)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid Ticket Pass! Token not found: " + passToken));

        // Step 2: Check if pass has already been used
        if (pass.getStatus() == PassStatus.CHECKED_IN) {
            throw new BadRequestException("ALERT: Ticket already used! This pass was checked in at " + passToken);
        }

        // Step 3: Check if pass is revoked or cancelled
        if (pass.getStatus() == PassStatus.REVOKED) {
            throw new BadRequestException("ALERT: This ticket pass has been revoked/cancelled.");
        }

        // Step 4: Mark pass as checked in
        pass.setStatus(PassStatus.CHECKED_IN);
        ticketPassRepository.save(pass);

        // Step 5: Extract details for response
        String attendeeName = pass.getBooking().getAttendee().getFullName();
        String eventTitle = pass.getBooking().getTicketTier().getEvent().getTitle();
        String tierName = pass.getBooking().getTicketTier().getTierName();

        return ScanResponse.builder()
                .passToken(pass.getPassToken())
                .status(PassStatus.CHECKED_IN)
                .message("Access Granted! Welcome, " + attendeeName)
                .attendeeName(attendeeName)
                .eventTitle(eventTitle)
                .tierName(tierName)
                .checkedInAt(LocalDateTime.now())
                .build();
    }
}