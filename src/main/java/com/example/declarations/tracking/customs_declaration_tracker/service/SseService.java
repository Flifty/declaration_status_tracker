package com.example.declarations.tracking.customs_declaration_tracker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String username) {
        SseEmitter emitter = new SseEmitter(120_000L);
        emitters.put(username, emitter);
        emitter.onCompletion(() -> emitters.remove(username));
        return emitter;
    }

    public void sendEventToUser(String username, Object event) {
        SseEmitter emitter = emitters.get(username);
        if (emitter != null) {
            try {
                emitter.send(event);
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(username);
            }
        }
    }
}