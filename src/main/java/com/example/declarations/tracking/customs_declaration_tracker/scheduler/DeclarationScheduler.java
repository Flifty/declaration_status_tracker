package com.example.declarations.tracking.customs_declaration_tracker.scheduler;

import com.example.declarations.tracking.customs_declaration_tracker.dto.StatusUpdateDto;
import com.example.declarations.tracking.customs_declaration_tracker.entity.Declaration;
import com.example.declarations.tracking.customs_declaration_tracker.repository.DeclarationRepository;
import com.example.declarations.tracking.customs_declaration_tracker.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeclarationScheduler {

    private final DeclarationRepository declarationRepository;
    private final SseService sseService;

    @Scheduled(fixedRate = 3600_000)
    public void checkStuckDeclarations() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.minusHours(1);

        List<Declaration> declarations = declarationRepository.findByStatus("PROCESSING");

        for (Declaration d : declarations) {
            if (d.getUpdatedAt().isBefore(threshold)) {
                String oldStatus = d.getStatus();
                d.setStatus("STUCK");
                d.setUpdatedAt(LocalDateTime.now());
                declarationRepository.save(d);

                StatusUpdateDto event = StatusUpdateDto.builder()
                        .number(d.getNumber())
                        .oldStatus(oldStatus)
                        .newStatus(d.getStatus())
                        .updatedAt(d.getUpdatedAt())
                        .build();

                sseService.sendEventToUser("inspector", event);
                sseService.sendEventToUser("admin", event);
            }
        }
    }
}