package com.codearqui.servicenotify.repository;

import com.codearqui.servicenotify.models.entity.NotifyOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyOrderRepository extends ReactiveCrudRepository<NotifyOrder, String> {
}