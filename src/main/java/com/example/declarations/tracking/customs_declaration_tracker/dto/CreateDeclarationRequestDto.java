package com.example.declarations.tracking.customs_declaration_tracker.dto;

import lombok.Data;

@Data
public class CreateDeclarationRequestDto {
    private String number;
    private String status;
}