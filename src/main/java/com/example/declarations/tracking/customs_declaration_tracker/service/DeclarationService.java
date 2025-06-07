package com.example.declarations.tracking.customs_declaration_tracker.service;

import com.example.declarations.tracking.customs_declaration_tracker.dto.DeclarationDto;
import com.example.declarations.tracking.customs_declaration_tracker.entity.Declaration;
import com.example.declarations.tracking.customs_declaration_tracker.exception.DeclarationNotFoundException;
import com.example.declarations.tracking.customs_declaration_tracker.repository.DeclarationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeclarationService {

    private final DeclarationRepository declarationRepository;

    public DeclarationService(DeclarationRepository declarationRepository) {
        this.declarationRepository = declarationRepository;
    }

    public DeclarationDto getDeclarationStatus(String number) {
        Optional<Declaration> optionalDeclaration = declarationRepository.findByNumber(number);
        if (optionalDeclaration.isPresent()) {
            Declaration declaration = optionalDeclaration.get();
            return mapToDto(declaration);
        } else {
            throw new DeclarationNotFoundException("Declaration with number " + number + " not found");
        }
    }

    private DeclarationDto mapToDto(Declaration declaration) {
        return DeclarationDto.builder()
                .number(declaration.getNumber())
                .status(declaration.getStatus())
                .createdAt(declaration.getCreatedAt())
                .updatedAt(declaration.getUpdatedAt())
                .build();
    }
}