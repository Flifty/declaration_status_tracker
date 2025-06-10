package com.example.declarations.tracking.customs_declaration_tracker.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateDto {
    private String number;
    private String oldStatus;
    private String newStatus;
    private LocalDateTime updatedAt;
}
