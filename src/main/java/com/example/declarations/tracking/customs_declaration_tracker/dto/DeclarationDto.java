package com.example.declarations.tracking.customs_declaration_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DeclarationDto {
    @NotBlank
    private String number;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}