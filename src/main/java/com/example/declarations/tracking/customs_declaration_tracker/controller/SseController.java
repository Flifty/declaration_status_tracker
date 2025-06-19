package com.example.declarations.tracking.customs_declaration_tracker.controller;

import com.example.declarations.tracking.customs_declaration_tracker.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class SseController {

    private static final Logger log = LoggerFactory.getLogger(SseController.class);

    private final SseService sseService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Пользователь '{}' подписывается на SSE", username);
        return sseService.subscribe(username);
    }
}