package com.codearqui.servicenotify.services;

import com.codearqui.servicenotify.dto.NotifyResponse;
import com.codearqui.servicenotify.models.dto.ListerNotify;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotifyOrderService {
    Mono<Boolean> saveNotify(ListerNotify listerNotify);
    Mono<NotifyResponse> getNotify(String id);
    Flux<NotifyResponse> getAllNotify();
}
