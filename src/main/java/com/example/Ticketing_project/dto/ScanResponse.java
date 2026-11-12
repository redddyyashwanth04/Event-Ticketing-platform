package com.example.Ticketing_project.dto;

import com.example.Ticketing_project.entity.PassStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ScanResponse {
    private String passToken;
    private PassStatus status;
    private String message;
    private String attendeeName;
    private String eventTitle;
    private String tierName;
    private LocalDateTime checkedInAt;
}