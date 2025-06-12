package com.example.declarations.tracking.customs_declaration_tracker.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

@Data
public class DeclarationFilterDto {
    private String number;
    private String status;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;
    private int page = 0;
    private int size = 20;
    private String sortBy = "createdAt";
    private Sort.Direction sortDirection = Sort.Direction.DESC;
}