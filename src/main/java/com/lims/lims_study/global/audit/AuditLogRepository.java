package com.lims.lims_study.global.audit;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AuditLogRepository {
    
    void insert(AuditLog auditLog);
    
    List<AuditLog> findByEntityTypeAndEntityId(@Param("entityType") String entityType, @Param("entityId") Long entityId);
    
    List<AuditLog> findByUserId(@Param("userId") Long userId, @Param("limit") int limit);
    
    List<AuditLog> findByAction(@Param("action") String action, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    List<AuditLog> findSecurityEvents(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    long countByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    void deleteOldLogs(@Param("beforeDate") LocalDateTime beforeDate);
}
