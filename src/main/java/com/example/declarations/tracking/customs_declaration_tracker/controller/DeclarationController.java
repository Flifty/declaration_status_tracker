package com.example.declarations.tracking.customs_declaration_tracker.controller;

import com.example.declarations.tracking.customs_declaration_tracker.dto.CreateDeclarationRequestDto;
import com.example.declarations.tracking.customs_declaration_tracker.dto.DeclarationDto;
import com.example.declarations.tracking.customs_declaration_tracker.dto.DeclarationFilterDto;
import com.example.declarations.tracking.customs_declaration_tracker.exception.DeclarationNotFoundException;
import com.example.declarations.tracking.customs_declaration_tracker.service.DeclarationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @GetMapping
    @Operation(summary = "Получение списка деклараций с фильтрацией и пагинацией")
    @ApiResponse(responseCode = "200", description = "Успешное выполнение")
    @PreAuthorize("hasRole('DECLARANT') or hasRole('INSPECTOR') or hasRole('ADMIN')")
    public ResponseEntity<Page<DeclarationDto>> getDeclarations(
            @RequestParam(required = false) String number,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime createdAtFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime createdAtTo,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime updatedAtFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime updatedAtTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        DeclarationFilterDto filter = new DeclarationFilterDto();
        filter.setNumber(number);
        filter.setStatus(status);
        filter.setCreatedAtFrom(createdAtFrom);
        filter.setCreatedAtTo(createdAtTo);
        filter.setUpdatedAtFrom(updatedAtFrom);
        filter.setUpdatedAtTo(updatedAtTo);
        filter.setPage(page);
        filter.setSize(size);
        filter.setSortBy(sortBy);
        filter.setSortDirection(sortDirection);

        Page<DeclarationDto> result = declarationService.findDeclarations(filter);
        return ResponseEntity.ok(result);
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