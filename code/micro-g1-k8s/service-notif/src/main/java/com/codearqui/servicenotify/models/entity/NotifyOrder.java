package com.codearqui.servicenotify.models.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "notify_orders")
public class NotifyOrder {
    @Id
    private String id;
    private String owner;
    private boolean status;
    private String dateString;
}
