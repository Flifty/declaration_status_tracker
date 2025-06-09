package com.example.declarations.tracking.customs_declaration_tracker.service;

import com.example.declarations.tracking.customs_declaration_tracker.dto.DeclarationDto;
import com.example.declarations.tracking.customs_declaration_tracker.entity.Declaration;
import com.example.declarations.tracking.customs_declaration_tracker.exception.DeclarationNotFoundException;
import com.example.declarations.tracking.customs_declaration_tracker.mapper.DeclarationMapper;
import com.example.declarations.tracking.customs_declaration_tracker.repository.DeclarationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeclarationService {

    private final DeclarationRepository declarationRepository;
    private final DeclarationMapper declarationMapper;

    public DeclarationDto getDeclaration(String number) {
        return declarationRepository.findByNumber(number)
                .map(declarationMapper::toDto)
                .orElseThrow(() -> new DeclarationNotFoundException("Declaration with number " + number + " not found"));
    }

    public DeclarationDto createDeclaration(String number, String status) {
        if (declarationRepository.findByNumber(number).isPresent()) {
            throw new IllegalArgumentException("Declaration with number " + number + " already exists");
        }

        Declaration declaration = Declaration.builder()
                .number(number)
                .status(status)
                .build();

        declaration = declarationRepository.save(declaration);
        return declarationMapper.toDto(declaration);
    }

    public DeclarationDto updateDeclaration(String number, String newStatus) {
        Declaration declaration = declarationRepository.findByNumber(number)
                .orElseThrow(() -> new DeclarationNotFoundException("Declaration not found"));

        declaration.setStatus(newStatus);

        declaration = declarationRepository.save(declaration);
        return declarationMapper.toDto(declaration);
    }
}