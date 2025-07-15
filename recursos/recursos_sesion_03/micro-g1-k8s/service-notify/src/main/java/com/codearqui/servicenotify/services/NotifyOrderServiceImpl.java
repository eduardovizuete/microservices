package com.codearqui.servicenotify.services;

import com.codearqui.servicenotify.dto.NotifyResponse;
import com.codearqui.servicenotify.models.dto.ListerNotify;
import com.codearqui.servicenotify.models.entity.NotifyOrder;
import com.codearqui.servicenotify.repository.NotifyOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyOrderServiceImpl implements NotifyOrderService {
    private final NotifyOrderRepository notify;

    @Override
    public Mono<Boolean> saveNotify(ListerNotify listerNotify) {
        var data = new NotifyOrder();
        data.setOwner(listerNotify.owner());
        data.setStatus(listerNotify.status());
        data.setDateString(LocalDateTime.now().toString());

        return notify.save(data)
                .map(x -> x.getId() != null ? true : false)
                .switchIfEmpty(Mono.just(false))
                .doOnSuccess(x -> log.info("Notify saved: " + x))
                .doOnSubscribe(x -> log.info("Saving notify..."))
                .doOnError(x -> log.error("Error saving notify: " + x.getMessage()));
    }

    @Override
    public Mono<NotifyResponse> getNotify(String id) {
        return notify.findById(id)
                .map(x -> new NotifyResponse()
                        .id(x.getId())
                        .owner(x.getOwner())
                        .status(x.isStatus())
                        .dateString(x.getDateString()));
    }

    @Override
    public Flux<NotifyResponse> getAllNotify() {
        return notify.findAll()
                .map(x -> new NotifyResponse()
                        .id(x.getId())
                        .owner(x.getOwner())
                        .status(x.isStatus())
                        .dateString(x.getDateString()));
    }
}
