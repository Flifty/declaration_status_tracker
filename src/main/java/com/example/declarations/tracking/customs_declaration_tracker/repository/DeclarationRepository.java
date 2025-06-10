package com.example.declarations.tracking.customs_declaration_tracker.repository;

import com.example.declarations.tracking.customs_declaration_tracker.entity.Declaration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {
    Optional<Declaration> findByNumber(String number);
    List<Declaration> findByStatus(String status);
}