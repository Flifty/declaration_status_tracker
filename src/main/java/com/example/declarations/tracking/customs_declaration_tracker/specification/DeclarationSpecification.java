package com.example.declarations.tracking.customs_declaration_tracker.specification;

import com.example.declarations.tracking.customs_declaration_tracker.dto.DeclarationFilterDto;
import com.example.declarations.tracking.customs_declaration_tracker.entity.Declaration;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class DeclarationSpecification {

    public static Specification<Declaration> withNumber(String number) {
        return (root, query, cb) ->
                number == null ? null : cb.like(root.get("number"), number);
    }

    public static Specification<Declaration> withStatus(String status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Declaration> createdAtBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return cb.conjunction();
            if (from != null && to != null)
                return cb.between(root.get("createdAt"), from, to);
            if (from != null)
                return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
            return cb.lessThanOrEqualTo(root.get("createdAt"), to);
        };
    }

    public static Specification<Declaration> updatedAtBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return null;
            if (from != null && to != null)
                return cb.between(root.get("updatedAt"), from, to);
            if (from != null)
                return cb.greaterThanOrEqualTo(root.get("updatedAt"), from);
            return cb.lessThanOrEqualTo(root.get("updatedAt"), to);
        };
    }

    public static Specification<Declaration> combined(DeclarationFilterDto filter) {
        return withNumber(filter.getNumber())
                .and(withStatus(filter.getStatus()))
                .and(createdAtBetween(filter.getCreatedAtFrom(), filter.getCreatedAtTo()))
                .and(updatedAtBetween(filter.getUpdatedAtFrom(), filter.getUpdatedAtTo()));
    }
}