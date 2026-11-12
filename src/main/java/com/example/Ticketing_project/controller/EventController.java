package com.example.Ticketing_project.controller;

import com.example.Ticketing_project.dto.EventRequest;
import com.example.Ticketing_project.dto.EventResponse;
import com.example.Ticketing_project.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // POST: /api/events (Restricted to users with ROLE_ORGANIZER)
    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        EventResponse response = eventService.createEvent(request);
        return ResponseEntity.ok(response);
    }

    // GET: /api/events/public (Public catalog - anyone can access)
    @GetMapping("/public")
    public ResponseEntity<List<EventResponse>> getPublicEvents() {
        List<EventResponse> response = eventService.getPublicEvents();
        return ResponseEntity.ok(response);
    }
}