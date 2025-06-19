package com.example.declarations.tracking.customs_declaration_tracker.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateDeclarationRequestDto {
    private String number;
    private String status;
}