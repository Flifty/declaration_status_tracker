package com.example.declarations.tracking.customs_declaration_tracker.mapper;

import com.example.declarations.tracking.customs_declaration_tracker.dto.DeclarationDto;
import com.example.declarations.tracking.customs_declaration_tracker.entity.Declaration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@Component
public interface DeclarationMapper {
    DeclarationDto toDto(Declaration declaration);
}