# 🚀 **LIMS 시스템 2.0 - 전원 승인제 + 동시성 제어**

## 📋 **주요 개선사항**

### **🔄 전원 승인제 구현**
- **기존**: 한 명만 승인해도 통과 (단일 승인제)
- **개선**: 모든 승인자가 승인해야 통과 (전원 승인제)
- **즉시 반려**: 한 명이라도 반려하면 전체 반려

### **⚡ 동시성 문제 해결**
- **Optimistic Locking**: 버전 기반 동시성 제어
- **재시도 메커니즘**: 버전 충돌 시 자동 재시도 (최대 3회)
- **데이터 무결성**: 동시 승인 시에도 안전한 상태 관리

---

## 🏗️ **시스템 아키텍처**

### **엔티티 관계**
```
Approval (승인 마스터)
├── version: BIGINT          # 동시성 제어
├── status: ENUM            # PENDING/PARTIAL_APPROVED/APPROVED/REJECTED
└── ApprovalRequest (1:1)   # 승인 요청 정보
    └── ApprovalSign (1:N)  # 개별 승인자 서명
```

### **승인 상태 흐름**
```
PENDING (초기)
    ↓
PARTIAL_APPROVED (일부 승인)
    ↓
APPROVED (전체 승인) / REJECTED (반려)
```

---

## 💡 **핵심 비즈니스 로직**

### **1. 전원 승인제 규칙**
```java
// 한 명이라도 반려 → 전체 반려
if (rejectedCount > 0) {
    approval.updateStatus(ApprovalStatus.REJECTED);
}
// 모든 승인자가 승인 → 전체 승인
else if (approvedCount == totalSigns) {
    approval.updateStatus(ApprovalStatus.APPROVED);
}
// 일부만 승인 → 부분 승인
else if (approvedCount > 0) {
    approval.updateStatus(ApprovalStatus.PARTIAL_APPROVED);
}
```

### **2. 동시성 제어**
```java
// 버전 기반 업데이트
int updatedRows = approvalRepository.updateWithVersion(approval, currentVersion);

if (updatedRows == 1) {
    // 성공: 업데이트 완료
    return buildResponseDto(updatedApproval);
} else {
    // 버전 충돌: 재시도
    retryCount++;
}
```

---

## 🗄️ **데이터베이스 스키마**

### **승인 마스터 테이블**
```sql
CREATE TABLE approvals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status ENUM('PENDING', 'PARTIAL_APPROVED', 'APPROVED', 'REJECTED'),
    version BIGINT NOT NULL DEFAULT 0,  -- 동시성 제어
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### **승인 서명 테이블**
```sql
CREATE TABLE approval_signs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    approval_id BIGINT NOT NULL,
    approver_id BIGINT NOT NULL,
    target_id BIGINT NOT NULL,      -- 승인 대상 ID
    stage ENUM('REQUEST', 'RECEIPT', 'RECEIPT_APPROVAL', 'RESULT_INPUT', 'COMPLETED'),
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    comment TEXT,
    
    -- 중복 승인 방지
    UNIQUE KEY uk_approval_target_stage (approval_id, target_id, stage)
);
```

---

## 🔧 **API 사용법**

### **승인 서명 업데이트**
```http
PUT /api/approvals/{approvalId}/signs/{signId}
Content-Type: application/json

{
    "status": "APPROVED",
    "comment": "승인합니다"
}
```

### **응답 예제**
```json
{
    "success": true,
    "data": {
        "id": 1,
        "status": "PARTIAL_APPROVED",
        "version": 3,
        "request": {
            "requesterId": 100,
            "comment": "승인 요청드립니다"
        },
        "signs": [
            {
                "id": 1,
                "approverId": 200,
                "status": "APPROVED",
                "comment": "승인합니다"
            },
            {
                "id": 2,
                "approverId": 201,
                "status": "PENDING",
                "comment": null
            }
        ]
    },
    "message": "승인이 성공적으로 처리되었습니다",
    "timestamp": "2025-09-18 14:30:00"
}
```

---

## 🧪 **테스트 시나리오**

### **전원 승인제 테스트**
- ✅ 부분 승인: 3명 중 2명 승인 → `PARTIAL_APPROVED`
- ✅ 전체 승인: 3명 모두 승인 → `APPROVED`
- ✅ 즉시 반려: 1명 반려 → `REJECTED`

### **동시성 제어 테스트**
- ✅ 버전 충돌 시 재시도 성공
- ✅ 최대 재시도 초과 시 예외 발생
- ✅ 버전 필드 자동 증가

---

## 🚀 **성능 최적화**

### **데이터베이스 인덱스**
```sql
-- 승인 상태별 조회 최적화
INDEX idx_status (status)
INDEX idx_status_created (status, created_at)

-- 승인자별 업무량 조회 최적화  
INDEX idx_approver_id (approver_id)
INDEX idx_approval_status (approval_id, status)
```

### **동시성 성능**
- **재시도 간격**: 50ms (부하 분산)
- **최대 재시도**: 3회 (응답성 보장)
- **데드락 방지**: 버전 기반 낙관적 잠금

---

## 📊 **모니터링 및 통계**

### **승인 통계 뷰**
```sql
CREATE VIEW approval_statistics AS
SELECT 
    status,
    COUNT(*) as approval_count,
    AVG(TIMESTAMPDIFF(HOUR, created_at, updated_at)) as avg_processing_hours
FROM approvals 
GROUP BY status;
```

### **승인자 워크로드 뷰**
```sql
CREATE VIEW approver_workload AS
SELECT 
    u.username,
    COUNT(s.id) as total_signs,
    COUNT(CASE WHEN s.status = 'PENDING' THEN 1 END) as pending_signs
FROM users u
LEFT JOIN approval_signs s ON u.id = s.approver_id
GROUP BY u.id, u.username;
```

---

## ⚠️ **주의사항**

### **업그레이드 시 고려사항**
1. **데이터 마이그레이션**: 기존 승인 데이터 백업 필수
2. **API 호환성**: 응답 구조에 `version` 필드 추가
3. **성능 영향**: 재시도 로직으로 인한 약간의 응답 시간 증가

### **운영 가이드라인**
- 동시성 충돌 빈도 모니터링 권장
- 승인자 수가 많을 경우 성능 테스트 필요
- 버전 필드 오버플로우 대비 (BIGINT 사용)

---

## 🔄 **향후 개선 계획**

1. **단계별 승인**: TestStage 기반 순차 승인
2. **승인 권한 관리**: 역할 기반 승인 권한 체크  
3. **승인 기한 관리**: 시간 제한 승인 시스템
4. **알림 시스템**: 승인 요청/완료 알림
5. **승인 이력 추적**: 상세한 승인 로그