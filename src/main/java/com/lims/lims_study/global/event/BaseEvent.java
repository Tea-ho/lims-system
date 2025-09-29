package com.lims.lims_study.global.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public abstract class BaseEvent extends ApplicationEvent {
    private final LocalDateTime occurredAt;
    private final String eventType;

    protected BaseEvent(Object source, String eventType) {
        super(source);
        this.eventType = eventType;
        this.occurredAt = LocalDateTime.now();
    }
}
