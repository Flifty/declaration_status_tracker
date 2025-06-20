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

    @Scheduled(fixedRateString = "${app.scheduler.check-interval}")
    public void checkStuckDeclarations() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.minusHours(1);

        List<Declaration> stuckDeclarations = declarationRepository.findByStatus("PROCESSING").stream()
                .filter(d -> d.getUpdatedAt().isBefore(threshold))
                .peek(d -> {
                    d.setStatus("STUCK");
                    d.setUpdatedAt(now);
                })
                .toList();

        if (!stuckDeclarations.isEmpty()) {
            declarationRepository.saveAll(stuckDeclarations);

            stuckDeclarations.forEach(d -> {
                StatusUpdateDto event = StatusUpdateDto.builder()
                        .number(d.getNumber())
                        .oldStatus("PROCESSING")
                        .newStatus("STUCK")
                        .updatedAt(now)
                        .build();

                sseService.broadcastEvent(event);
            });
        }
    }
}