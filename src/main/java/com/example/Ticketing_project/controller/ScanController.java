package com.example.Ticketing_project.controller;

import com.example.Ticketing_project.dto.ScanResponse;
import com.example.Ticketing_project.service.ScanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scanner")
@RequiredArgsConstructor
public class ScanController {

    private final ScanService scanService;

    // POST: /api/scanner/scan/{passToken} (Restricted to Event Staff / Scanners or Organizers)
    @PostMapping("/scan/{passToken}")
    @PreAuthorize("hasAnyRole('SCANNER', 'ORGANIZER')")
    public ResponseEntity<ScanResponse> scanTicketPass(@PathVariable String passToken) {
        ScanResponse response = scanService.scanPass(passToken);
        return ResponseEntity.ok(response);
    }
}