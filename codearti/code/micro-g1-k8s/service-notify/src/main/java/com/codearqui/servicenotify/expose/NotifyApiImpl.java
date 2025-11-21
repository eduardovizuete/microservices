package com.codearqui.servicenotify.expose;

import com.codearqui.servicenotify.api.NotifyApiDelegate;
import com.codearqui.servicenotify.dto.NotifyResponse;
import com.codearqui.servicenotify.services.NotifyOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotifyApiImpl implements NotifyApiDelegate {
    private final NotifyOrderService notifyOrderService;

    /**
     * GET /notify : Lista las notificaciones procesadas
     * Lista las notificaciones procesadas
     *
     * @return Operación exitosa (status code 200)
     *         or Invalid status value (status code 400)
     * @see NotifyApi#findAllNotify
     */
    @Override
    public Flux<NotifyResponse> findAllNotify(ServerWebExchange exchange) {
        return notifyOrderService.getAllNotify()
                .doOnSubscribe(value -> log.info("Request init findAllNotify()"))
                .doOnComplete(() -> log.info("Request success findAllNotify()"))
                .doOnError(value -> log.error("Request Error findAllNotify()", value));
    }

    /**
     * GET /notify/{notifyId} : Busca una notificación por ID
     * Busca una notificación por ID
     *
     * @param notifyId ID de la notificación (required)
     * @return successful operation (status code 200)
     *         or Invalid ID supplied (status code 400)
     *         or Notifición no encontrada (status code 404)
     * @see NotifyApi#getNotifyById
     */
    @Override
    public Mono<NotifyResponse> getNotifyById(String notifyId,
                                              ServerWebExchange exchange) {
        return notifyOrderService.getNotify(notifyId);
    }
}