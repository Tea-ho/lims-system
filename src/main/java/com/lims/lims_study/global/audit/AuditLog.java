package com.lims.lims_study.global.audit;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuditLog {
    private Long id;
    private String entityType;
    private Long entityId;
    private String action;
    private String oldValues;
    private String newValues;
    private Long userId;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime createdAt;

    public AuditLog() {
        this.createdAt = LocalDateTime.now();
    }

    public AuditLog(String entityType, Long entityId, String action, Long userId) {
        this();
        this.entityType = entityType;
        this.entityId = entityId;
        this.action = action;
        this.userId = userId;
    }
}
