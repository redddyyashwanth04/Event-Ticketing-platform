package com.example.Ticketing_project.service;

import com.example.Ticketing_project.dto.EventRequest;
import com.example.Ticketing_project.dto.EventResponse;
import com.example.Ticketing_project.entity.Event;
import com.example.Ticketing_project.entity.EventStatus;
import com.example.Ticketing_project.entity.TicketTier;
import com.example.Ticketing_project.entity.User;
import com.example.Ticketing_project.exception.ResourceNotFoundException;
import com.example.Ticketing_project.repository.EventRepository;
import com.example.Ticketing_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    // 1. CREATE EVENT LOGIC
    @Transactional
    public EventResponse createEvent(EventRequest request) {
        // Extract logged-in organizer's email from Spring Security Context
        String organizerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User organizer = userRepository.findByEmail(organizerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Organizer not found with email: " + organizerEmail));

        // Build Event entity
        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setLocation(request.getLocation());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setStatus(EventStatus.PUBLISHED); // Default to PUBLISHED upon creation
        event.setOrganizer(organizer);

        // Map and attach Ticket Tiers
        List<TicketTier> tiers = request.getTicketTiers().stream().map(tierReq -> {
            TicketTier tier = new TicketTier();
            tier.setTierName(tierReq.getTierName());
            tier.setPrice(tierReq.getPrice());
            tier.setTotalCapacity(tierReq.getTotalCapacity());
            tier.setAvailableStock(tierReq.getTotalCapacity()); // Initially, available stock = total capacity
            tier.setEvent(event);
            return tier;
        }).collect(Collectors.toList());

        event.setTicketTiers(tiers);

        // Save event (Cascades and saves ticket tiers automatically due to CascadeType.ALL)
        Event savedEvent = eventRepository.save(event);

        return mapToEventResponse(savedEvent);
    }

    // 2. GET PUBLIC EVENTS CATALOG
    public List<EventResponse> getPublicEvents() {
        List<Event> events = eventRepository.findByStatus(EventStatus.PUBLISHED);
        return events.stream().map(this::mapToEventResponse).collect(Collectors.toList());
    }

    // Helper mapper method
    private EventResponse mapToEventResponse(Event event) {
        List<EventResponse.TicketTierDto> tierDtos = event.getTicketTiers().stream().map(tier ->
                EventResponse.TicketTierDto.builder()
                        .id(tier.getId())
                        .tierName(tier.getTierName())
                        .price(tier.getPrice())
                        .totalCapacity(tier.getTotalCapacity())
                        .availableStock(tier.getAvailableStock())
                        .build()
        ).collect(Collectors.toList());

        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .location(event.getLocation())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .status(event.getStatus())
                .organizerName(event.getOrganizer().getFullName())
                .ticketTiers(tierDtos)
                .build();
    }
}