package com.example.declarations.tracking.customs_declaration_tracker.controller;

import com.example.declarations.tracking.customs_declaration_tracker.dto.DeclarationDto;
import com.example.declarations.tracking.customs_declaration_tracker.exception.DeclarationNotFoundException;
import com.example.declarations.tracking.customs_declaration_tracker.service.DeclarationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/declarations")
public class DeclarationController {

    private final DeclarationService declarationService;

    public DeclarationController(DeclarationService declarationService) {
        this.declarationService = declarationService;
    }

    @GetMapping("/{number}")
    public ResponseEntity<DeclarationDto> getStatus(@PathVariable String number) {
        try {
            DeclarationDto declarationDto = declarationService.getDeclarationStatus(number);
            return ResponseEntity.ok(declarationDto);
        } catch (DeclarationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}