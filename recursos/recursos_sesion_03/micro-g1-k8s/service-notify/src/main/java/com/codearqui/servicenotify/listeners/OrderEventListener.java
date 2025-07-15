package com.codearqui.servicenotify.listeners;

import com.codearqui.servicenotify.models.dto.ListerNotify;
import com.codearqui.servicenotify.services.NotifyOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {
    private final NotifyOrderService notifyOrderService;

    @KafkaListener(topics = "orders-topic", groupId = "micro-order")
    public Mono<Void> listen(String message) {
        log.info("Received message: {}", message);

        var data = new ListerNotify(0, message, true);
        return notifyOrderService.saveNotify(data)
                .then();
    }
}
