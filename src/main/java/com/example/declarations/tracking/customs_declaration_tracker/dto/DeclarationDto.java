package com.example.declarations.tracking.customs_declaration_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeclarationDto {
    @NotBlank
    private String number;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}