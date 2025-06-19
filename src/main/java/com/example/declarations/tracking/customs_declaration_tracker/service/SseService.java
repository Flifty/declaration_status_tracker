package com.example.declarations.tracking.customs_declaration_tracker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {

    private static final Logger log = LoggerFactory.getLogger(SseService.class);

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String username) {
        log.info("Пользователь {} подписывается на SSE", username);

        SseEmitter emitter = new SseEmitter(120_000L);
        emitters.put(username, emitter);

        emitter.onCompletion(() -> {
            log.info("Пользователь {} отписался от SSE", username);
            emitters.remove(username);
        });

        return emitter;
    }

    public void sendEventToUser(String username, Object event) {
        SseEmitter emitter = emitters.get(username);

        if (emitter != null) {
            try {
                log.debug("Отправка события пользователю {}: {}", username, event);
                emitter.send(event);
            } catch (IOException e) {
                log.warn("Ошибка при отправке события для пользователя {}", username, e);
                emitter.complete();
                emitters.remove(username);
            }
        } else {
            log.warn("Невозможно отправить событие: пользователь {} не подписан", username);
        }
    }

    public void broadcastEvent(Object event) {
        log.info("Рассылка события всем подписчикам");

        emitters.forEach((username, emitter) -> {
            try {
                log.debug("Отправка события пользователю {}", username);
                emitter.send(event);
            } catch (IOException e) {
                log.warn("Ошибка при рассылке событий: {}", e.getMessage());
                emitter.complete();
                emitters.remove(username);
            }
        });
    }
}