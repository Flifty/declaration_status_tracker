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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class DeclarationService {

    private static final Logger log = LoggerFactory.getLogger(DeclarationService.class);

    private final DeclarationRepository declarationRepository;
    private final DeclarationMapper declarationMapper;
    private final SseService sseService;

    public DeclarationDto getDeclaration(String number) {
        log.info("Получен запрос на получение декларации с номером {}", number);
        return declarationRepository.findByNumber(number)
                .map(declarationMapper::toDto)
                .orElseThrow(() -> new DeclarationNotFoundException("Декларация с номером " + number + " не найдена"));
    }

    public DeclarationDto createDeclaration(String number, String status) {
        log.info("Получен запрос на создание декларации: {} | Статус: {}", number, status);

        if (declarationRepository.findByNumber(number).isPresent()) {
            log.warn("Попытка создать дублирующую декларацию с номером {}", number);
            throw new IllegalArgumentException("Декларация с номером " + number + " уже существует");
        }

        Declaration declaration = Declaration.builder()
                .number(number)
                .status(status)
                .build();

        declaration = declarationRepository.save(declaration);
        log.info("Декларация {} успешно создана", number);
        return declarationMapper.toDto(declaration);
    }

    public DeclarationDto updateDeclaration(String number, String newStatus) {
        log.info("Получен запрос на обновление статуса декларации {}: {}", number, newStatus);

        Declaration declaration = declarationRepository.findByNumber(number)
                .orElseThrow(() -> new DeclarationNotFoundException("Декларация с номером " + number + " не найдена"));

        String oldStatus = declaration.getStatus();

        if (oldStatus.equals(newStatus)) {
            log.debug("Статус декларации {} не изменился", number);
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

        log.info("Статус декларации {} изменён с {} на {}", number, oldStatus, newStatus);

        sseService.sendEventToUser("inspector", event);
        sseService.sendEventToUser("admin", event);

        return dto;
    }

    public Page<DeclarationDto> findDeclarations(DeclarationFilterDto filter) {
        log.info("Получен запрос на получение списка деклараций с фильтром: {}", filter);

        try {
            Pageable pageable = PageRequest.of(
                    filter.getPage(),
                    filter.getSize(),
                    Sort.by(filter.getSortDirection(), filter.getSortBy())
            );

            Specification<Declaration> spec = DeclarationSpecification.combined(filter);
            Page<Declaration> declarations = declarationRepository.findAll(spec, pageable);
            return declarations.map(declarationMapper::toDto);

        } catch (Exception e) {
            log.error("Ошибка при получении списка деклараций", e);
            throw new DeclarationNotFoundException("Не удалось получить список деклараций");
        }
    }
}