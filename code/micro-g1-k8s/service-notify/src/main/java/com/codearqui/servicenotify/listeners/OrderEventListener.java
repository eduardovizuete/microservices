package com.codearqui.servicenotify.listeners;

import com.codearqui.servicenotify.models.dto.ListenerNotify;
import com.codearqui.servicenotify.services.NotifyOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class OrderEventListener {
    private final NotifyOrderService notifyOrderService;

    @KafkaListener(topics = "orders-topic", groupId = "micro-order")
    public Mono<Void> listen(String message){
        log.info("Order received: {}", message);

        var data = new ListenerNotify(0, message, true);
        return notifyOrderService.saveNotify(data)
                .doOnSubscribe(x -> log.info("Init kafka data: {}", x))
                .doOnSuccess(x -> log.info("Success kafka data: {}", x))
                .doOnError(x -> log.error("Error kafka data", x))
                .then();
    }

}
