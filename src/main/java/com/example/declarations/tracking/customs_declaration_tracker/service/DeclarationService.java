package com.example.declarations.tracking.customs_declaration_tracker.service;

import com.example.declarations.tracking.customs_declaration_tracker.dto.DeclarationDto;
import com.example.declarations.tracking.customs_declaration_tracker.dto.DeclarationFilterDto;
import com.example.declarations.tracking.customs_declaration_tracker.dto.StatusUpdateDto;
import com.example.declarations.tracking.customs_declaration_tracker.entity.Declaration;
import com.example.declarations.tracking.customs_declaration_tracker.exception.DeclarationNotFoundException;
import com.example.declarations.tracking.customs_declaration_tracker.mapper.DeclarationMapper;
import com.example.declarations.tracking.customs_declaration_tracker.repository.DeclarationRepository;
import com.example.declarations.tracking.customs_declaration_tracker.specification.DeclarationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeclarationService {

    private final DeclarationRepository declarationRepository;
    private final DeclarationMapper declarationMapper;
    private final SseService sseService;

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

        String oldStatus = declaration.getStatus();

        if (oldStatus.equals(newStatus)) {
            return declarationMapper.toDto(declaration);
        }

        declaration.setStatus(newStatus);

        declaration = declarationRepository.save(declaration);
        DeclarationDto dto = declarationMapper.toDto(declaration);

        StatusUpdateDto event = StatusUpdateDto.builder()
                .number(dto.getNumber())
                .oldStatus(oldStatus)
                .newStatus(dto.getStatus())
                .updatedAt(dto.getUpdatedAt())
                .build();

        sseService.sendEventToUser("inspector", event);
        sseService.sendEventToUser("admin", event);

        return dto;
    }

    public Page<DeclarationDto> findDeclarations(DeclarationFilterDto filter) {
        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(filter.getSortDirection(), filter.getSortBy())
        );

        Specification<Declaration> spec = DeclarationSpecification.combined(filter);
        Page<Declaration> declarations = declarationRepository.findAll(spec, pageable);
        return declarations.map(declarationMapper::toDto);
    }
}