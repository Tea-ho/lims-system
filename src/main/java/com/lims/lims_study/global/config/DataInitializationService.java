package com.lims.lims_study.global.config;

import com.lims.lims_study.domain.approval.model.Approval;
import com.lims.lims_study.domain.approval.model.ApprovalStatus;
import com.lims.lims_study.domain.approval.repository.ApprovalRepository;
import com.lims.lims_study.domain.product.model.Product;
import com.lims.lims_study.domain.product.repository.ProductRepository;
import com.lims.lims_study.domain.test.model.RequestInfo;
import com.lims.lims_study.domain.test.model.Test;
import com.lims.lims_study.domain.test.repository.RequestInfoRepository;
import com.lims.lims_study.domain.test.repository.TestRepository;
import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.domain.user.repository.UserRepository;
import com.lims.lims_study.global.audit.AuditLog;
import com.lims.lims_study.global.audit.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile({"local"})
public class DataInitializationService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ApprovalRepository approvalRepository;
    private final TestRepository testRepository;
    private final RequestInfoRepository requestInfoRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeData() {
        log.info("=== 로컬 환경 샘플 데이터 초기화 시작 ===");

        try {
            // 데이터가 이미 존재하는지 확인
            if (isDataAlreadyExists()) {
                log.info("샘플 데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
                return;
            }

            // 새로운 샘플 데이터 생성
            log.info("새로운 샘플 데이터 생성 중...");
            createSampleUsers();
            createSampleProducts();
            createSampleApprovals();
            createSampleRequestInfos();
            createSampleAuditLogs();

            log.info("=== 로컬 환경 샘플 데이터 초기화 완료 ===");
        } catch (Exception e) {
            log.error("샘플 데이터 초기화 중 오류 발생", e);
        }
    }

    private void deleteAllData() {
        log.info("모든 데이터 삭제 중...");

        log.info("데이터 삭제는 로컬 환경에서 자동 수행됨");
    }

    private boolean isDataAlreadyExists() {
        // 사용자 데이터가 하나라도 있으면 이미 데이터가 존재한다고 판단
        return userRepository.countAllUsers() > 0;
    }

    private void createSampleUsers() {
        log.info("샘플 사용자 생성 중...");

        String encodedPassword = passwordEncoder.encode("password123");

        // 관리자 사용자 - 중복 체크 후 삽입
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User(null, "admin", encodedPassword, "ROLE_ADMIN");
            userRepository.insert(admin);
        }

        // 일반 사용자들 - 중복 체크 후 삽입
        if (!userRepository.existsByUsername("user1")) {
            User user1 = new User(null, "user1", encodedPassword, "ROLE_USER");
            userRepository.insert(user1);
        }

        if (!userRepository.existsByUsername("user2")) {
            User user2 = new User(null, "user2", encodedPassword, "ROLE_USER");
            userRepository.insert(user2);
        }

        if (!userRepository.existsByUsername("user3")) {
            User user3 = new User(null, "user3", encodedPassword, "ROLE_USER");
            userRepository.insert(user3);
        }

        log.info("샘플 사용자 생성 완료");
    }

    private void createSampleProducts() {
        log.info("샘플 제품 생성 중...");

        try {
            Product product1 = new Product(null, "아스피린 정제", "해열, 진통, 소염 효과가 있는 의약품");
            productRepository.insert(product1);

            Product product2 = new Product(null, "비타민 C 캡슐", "면역력 강화를 위한 비타민 C 보충제");
            productRepository.insert(product2);

            Product product3 = new Product(null, "오메가-3 소프트겔", "심혈관 건강을 위한 오메가-3 지방산 보충제");
            productRepository.insert(product3);

            Product product4 = new Product(null, "유산균 분말", "장내 미생물 균형을 위한 프로바이오틱스");
            productRepository.insert(product4);

            Product product5 = new Product(null, "칼슘 마그네슘 정제", "뼈 건강을 위한 칼슘과 마그네슘 복합제");
            productRepository.insert(product5);

            log.info("샘플 제품 5개 생성 완료");
        } catch (Exception e) {
            log.error("제품 생성 중 오류 발생: {}", e.getMessage());
            log.info("제품 테이블 스키마 문제로 건너뜀");
        }
    }

    private void createSampleApprovals() {
        log.info("샘플 승인 데이터 생성 중...");

        try {
            // 대기 중인 승인들
            Approval pendingApproval1 = new Approval(ApprovalStatus.PENDING);
            approvalRepository.insert(pendingApproval1);

            Approval pendingApproval2 = new Approval(ApprovalStatus.PENDING);
            approvalRepository.insert(pendingApproval2);

            Approval pendingApproval3 = new Approval(ApprovalStatus.PENDING);
            approvalRepository.insert(pendingApproval3);

            // 완료된 승인들
            Approval approvedApproval1 = new Approval(ApprovalStatus.APPROVED);
            approvalRepository.insert(approvedApproval1);

            Approval approvedApproval2 = new Approval(ApprovalStatus.APPROVED);
            approvalRepository.insert(approvedApproval2);

            // 반려된 승인
            Approval rejectedApproval = new Approval(ApprovalStatus.REJECTED);
            approvalRepository.insert(rejectedApproval);

            log.info("샘플 승인 데이터 6개 생성 완료");
        } catch (Exception e) {
            log.error("승인 데이터 생성 중 오류 발생: {}", e.getMessage());
        }
    }

    private void createSampleRequestInfos() {
        log.info("샘플 요청 정보 생성 중...");

        RequestInfo request1 = new RequestInfo("아스피린 품질 검사", "아스피린 정제에 대한 정기 품질 검사입니다.");
        requestInfoRepository.insert(request1);

        RequestInfo request2 = new RequestInfo("비타민 C 안전성 테스트", "비타민 C의 안전성을 확인하기 위한 테스트입니다.");
        requestInfoRepository.insert(request2);

        RequestInfo request3 = new RequestInfo("오메가-3 성능 평가", "오메가-3의 성능을 평가합니다.");
        requestInfoRepository.insert(request3);

        log.info("샘플 요청 정보 3개 생성 완료");
    }

    private void createSampleAuditLogs() {
        log.info("샘플 감사 로그 생성 중...");

        AuditLog log1 = new AuditLog("User", 1L, "CREATE", 1L);
        log1.setIpAddress("192.168.1.100");
        log1.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        auditLogRepository.insert(log1);

        AuditLog log2 = new AuditLog("Product", 1L, "UPDATE", 1L);
        log2.setIpAddress("192.168.1.101");
        log2.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36");
        auditLogRepository.insert(log2);

        AuditLog log3 = new AuditLog("Approval", 1L, "CREATE", 2L);
        log3.setIpAddress("192.168.1.102");
        log3.setUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36");
        auditLogRepository.insert(log3);

        AuditLog log4 = new AuditLog("Test", 1L, "DELETE", 1L);
        log4.setIpAddress("192.168.1.103");
        log4.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:91.0) Gecko/20100101");
        auditLogRepository.insert(log4);

        log.info("샘플 감사 로그 4개 생성 완료");
    }
}