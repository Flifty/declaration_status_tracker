package com.example.declarations.tracking.customs_declaration_tracker.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

@Builder
@Data
public class DeclarationFilterDto {
    private String number;
    private String status;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;
    private int page;
    private int size;
    private String sortBy;
    private Sort.Direction sortDirection;
}