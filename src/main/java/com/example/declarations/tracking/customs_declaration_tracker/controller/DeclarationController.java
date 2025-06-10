package com.example.declarations.tracking.customs_declaration_tracker.controller;

import com.example.declarations.tracking.customs_declaration_tracker.dto.CreateDeclarationRequestDto;
import com.example.declarations.tracking.customs_declaration_tracker.dto.DeclarationDto;
import com.example.declarations.tracking.customs_declaration_tracker.exception.DeclarationNotFoundException;
import com.example.declarations.tracking.customs_declaration_tracker.service.DeclarationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/declarations")
@Tag(name = "Declarations")
@RequiredArgsConstructor
public class DeclarationController {

    private final DeclarationService declarationService;

    @PostMapping
    @Operation(summary = "Создание новой декларации")
    @ApiResponse(responseCode = "200", description = "Успешное создание")
    @PreAuthorize("hasRole('DECLARANT') or hasRole('ADMIN')")
    public ResponseEntity<DeclarationDto> createDeclaration(@RequestBody CreateDeclarationRequestDto request) {
        DeclarationDto dto = declarationService.createDeclaration(request.getNumber(), request.getStatus());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{number}")
    @Operation(summary = "Получение декларации по номеру")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @ApiResponse(responseCode = "404", description = "Декларация не найдена")
    @PreAuthorize("hasRole('DECLARANT') or hasRole('INSPECTOR') or hasRole('ADMIN')")
    public ResponseEntity<DeclarationDto> getDeclaration(@PathVariable String number) {
        try {
            DeclarationDto declarationDto = declarationService.getDeclaration(number);
            return ResponseEntity.ok(declarationDto);
        } catch (DeclarationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{number}")
    @Operation(summary = "Обновление статуса декларации")
    @ApiResponse(responseCode = "200", description = "Статус обновлён")
    @PreAuthorize("hasRole('INSPECTOR') or hasRole('ADMIN')")
    public ResponseEntity<DeclarationDto> updateDeclaration(
            @PathVariable String number,
            @RequestParam String newStatus) {

        DeclarationDto dto = declarationService.updateDeclaration(number, newStatus);
        return ResponseEntity.ok(dto);
    }
}