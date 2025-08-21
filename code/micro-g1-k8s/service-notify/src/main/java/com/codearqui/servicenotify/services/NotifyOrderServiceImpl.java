package com.codearqui.servicenotify.services;

import com.codearqui.servicenotify.dto.NotifyResponse;
import com.codearqui.servicenotify.models.dto.ListenerNotify;
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

    private final NotifyOrderRepository notifyOrderRep;

    @Override
    public Mono<Boolean> saveNotify(ListenerNotify listenerNotify) {
        var data = new NotifyOrder();
        data.setOwner(listenerNotify.owner());
        data.setStatus(listenerNotify.status());
        data.setDateString(LocalDateTime.now().toString());

        return notifyOrderRep.save(data)
                .map(notify -> notify.getId() != null ? true: false)
                .switchIfEmpty(Mono.just(false))
                .doOnSuccess(notify -> log.info("Notify saved: " + notify))
                .doOnSubscribe(sub -> log.info("Saving notify ..."))
                .doOnError(ex -> log.error("Error saving notify: {}", ex.getMessage()));
    }

    @Override
    public Mono<NotifyResponse> getNotify(String id) {
        return notifyOrderRep.findById(id)
                .map(notify -> new NotifyResponse()
                        .id(notify.getId())
                        .owner(notify.getOwner())
                        .status(notify.isStatus())
                        .dateString(notify.getDateString()));
    }

    @Override
    public Flux<NotifyResponse> getAllNotify() {
        return notifyOrderRep.findAll()
                .map(notify -> new NotifyResponse()
                        .id(notify.getId())
                        .owner(notify.getOwner())
                        .status(notify.isStatus())
                        .dateString(notify.getDateString()));
    }

}
