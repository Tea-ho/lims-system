-- 샘플 데이터 삽입
-- Version: 7.0

-- 사용자 샘플 데이터
INSERT INTO users (username, password, email, role) VALUES
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'admin@lims.com', 'ADMIN'),
('김철수', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'kim.cheolsu@lims.com', 'APPROVER'),
('박영희', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'park.younghee@lims.com', 'APPROVER'),
('이민수', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'lee.minsu@lims.com', 'USER'),
('최지영', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'choi.jiyoung@lims.com', 'USER'),
('정우진', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'jung.woojin@lims.com', 'APPROVER'),
('한소희', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'han.sohee@lims.com', 'USER'),
('윤태호', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'yoon.taeho@lims.com', 'USER'),
('서지혜', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'seo.jihye@lims.com', 'APPROVER'),
('강동원', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'kang.dongwon@lims.com', 'USER');

-- 제품 샘플 데이터
INSERT INTO products (name, description) VALUES
('아스피린 정제', '해열, 진통, 소염 효과가 있는 의약품'),
('비타민 C 캡슐', '면역력 강화를 위한 비타민 C 보충제'),
('오메가-3 소프트겔', '심혈관 건강을 위한 오메가-3 지방산 보충제'),
('유산균 분말', '장내 미생물 균형을 위한 프로바이오틱스'),
('칼슘 마그네슘 정제', '뼈 건강을 위한 칼슘과 마그네슘 복합제'),
('종합비타민 정제', '일일 필수 비타민과 미네랄 복합체'),
('단백질 파우더', '근육 발달을 위한 고단백 보충제'),
('철분 캡슐', '철결핍성 빈혈 예방을 위한 철분 보충제'),
('콜라겐 분말', '피부와 관절 건강을 위한 콜라겐 보충제'),
('루테인 캡슐', '눈 건강을 위한 루테인 보충제'),
('글루코사민 정제', '관절 건강을 위한 글루코사민 보충제'),
('코엔자임 Q10 캡슐', '세포 에너지 생산을 위한 코엔자임 Q10'),
('마그네슘 분말', '근육과 신경 기능을 위한 마그네슘 보충제'),
('비타민 D3 캡슐', '뼈 건강과 면역력을 위한 비타민 D3'),
('셀레늄 정제', '항산화 효과를 위한 셀레늄 보충제');

-- 시험 샘플 데이터
INSERT INTO tests (product_id, requester_id, stage) VALUES
(1, 4, 'COMPLETED'),
(2, 4, 'RESULT_INPUT'),
(3, 5, 'RECEIPT_APPROVAL'),
(4, 5, 'RECEIPT'),
(5, 7, 'REQUEST'),
(6, 7, 'COMPLETED'),
(7, 8, 'RESULT_INPUT'),
(8, 8, 'RECEIPT_APPROVAL'),
(9, 10, 'RECEIPT'),
(10, 10, 'REQUEST'),
(11, 4, 'COMPLETED'),
(12, 5, 'RESULT_INPUT'),
(13, 7, 'RECEIPT_APPROVAL'),
(14, 8, 'RECEIPT'),
(15, 10, 'REQUEST');

-- 시험 요청 정보 샘플 데이터
INSERT INTO request_info (test_id, test_items, priority, requested_date, due_date, special_instructions) VALUES
(1, '성분 함량, 순도 시험, 용출 시험', 'HIGH', '2024-01-01', '2024-01-15', '우선 처리 요청'),
(2, '비타민 C 함량, 수분 함량, 미생물 시험', 'MEDIUM', '2024-01-02', '2024-01-16', NULL),
(3, '오메가-3 함량, 과산화물가, 산가', 'MEDIUM', '2024-01-03', '2024-01-17', NULL),
(4, '유산균 수, 생존율, 수분 함량', 'LOW', '2024-01-04', '2024-01-18', '냉장 보관 필수'),
(5, '칼슘 마그네슘 함량, 용출 시험', 'MEDIUM', '2024-01-05', '2024-01-19', NULL),
(6, '비타민 함량, 미네랄 함량, 붕해 시험', 'HIGH', '2024-01-06', '2024-01-20', NULL),
(7, '단백질 함량, 아미노산 조성, 수분 함량', 'MEDIUM', '2024-01-07', '2024-01-21', NULL),
(8, '철분 함량, 용출 시험, 붕해 시험', 'URGENT', '2024-01-08', '2024-01-22', '긴급 처리 요청'),
(9, '콜라겐 함량, 분자량 분포, 수분 함량', 'LOW', '2024-01-09', '2024-01-23', NULL),
(10, '루테인 함량, 순도 시험', 'MEDIUM', '2024-01-10', '2024-01-24', NULL),
(11, '글루코사민 함량, 순도 시험', 'HIGH', '2024-01-11', '2024-01-25', NULL),
(12, '코엔자임 Q10 함량, 안정성 시험', 'MEDIUM', '2024-01-12', '2024-01-26', NULL),
(13, '마그네슘 함량, 용출 시험', 'LOW', '2024-01-13', '2024-01-27', NULL),
(14, '비타민 D3 함량, 순도 시험', 'MEDIUM', '2024-01-14', '2024-01-28', NULL),
(15, '셀레늄 함량, 중금속 시험', 'HIGH', '2024-01-15', '2024-01-29', NULL);

-- 시험 접수 정보 샘플 데이터 (접수된 시험들만)
INSERT INTO receipt_info (test_id, received_by, received_date, sample_condition, storage_location, notes) VALUES
(1, 2, '2024-01-02 09:30:00', 'GOOD', 'A-101', '정상 접수'),
(2, 2, '2024-01-03 10:15:00', 'GOOD', 'A-102', '정상 접수'),
(3, 3, '2024-01-04 14:20:00', 'GOOD', 'B-201', '정상 접수'),
(4, 2, '2024-01-05 11:45:00', 'GOOD', 'A-103', '냉장 보관 완료'),
(6, 3, '2024-01-07 08:30:00', 'GOOD', 'B-202', '정상 접수'),
(7, 2, '2024-01-08 13:15:00', 'GOOD', 'A-104', '정상 접수'),
(8, 3, '2024-01-09 09:00:00', 'GOOD', 'B-203', '긴급 처리'),
(9, 2, '2024-01-10 15:30:00', 'GOOD', 'A-105', '정상 접수'),
(11, 3, '2024-01-12 10:00:00', 'GOOD', 'B-204', '정상 접수'),
(12, 2, '2024-01-13 14:45:00', 'GOOD', 'A-106', '정상 접수'),
(13, 3, '2024-01-14 11:30:00', 'GOOD', 'B-205', '정상 접수'),
(14, 2, '2024-01-15 16:00:00', 'GOOD', 'A-107', '정상 접수');

-- 시험 결과 정보 샘플 데이터 (결과 입력 이상 진행된 시험들)
INSERT INTO result_info (test_id, tested_by, test_started_date, test_completed_date, results, pass_fail, comments) VALUES
(1, 6, '2024-01-03 09:00:00', '2024-01-10 17:00:00', '{"성분함량": "99.8%", "순도": "적합", "용출": "95.2%"}', 'PASS', '모든 시험 항목 적합'),
(2, 6, '2024-01-04 10:00:00', NULL, '{"비타민C함량": "진행중", "수분함량": "2.1%", "미생물": "적합"}', 'PENDING', '비타민 C 함량 시험 진행 중'),
(6, 9, '2024-01-08 09:00:00', '2024-01-12 16:30:00', '{"비타민함량": "적합", "미네랄함량": "적합", "붕해": "15분"}', 'PASS', '모든 항목 적합'),
(7, 6, '2024-01-09 11:00:00', NULL, '{"단백질함량": "85.2%", "아미노산": "진행중", "수분": "4.5%"}', 'PENDING', '아미노산 조성 분석 중'),
(11, 9, '2024-01-13 08:30:00', '2024-01-18 14:20:00', '{"글루코사민함량": "98.5%", "순도": "적합"}', 'PASS', '품질 기준 적합'),
(12, 6, '2024-01-14 13:00:00', NULL, '{"코엔자임함량": "100.2%", "안정성": "진행중"}', 'PENDING', '안정성 시험 진행 중');

-- 시험 완료 정보 샘플 데이터 (완료된 시험들만)
INSERT INTO completion_info (test_id, completed_by, completion_date, report_path, final_approval_status, final_comments) VALUES
(1, 2, '2024-01-11 09:00:00', '/reports/2024/01/test_001_report.pdf', 'APPROVED', '최종 승인 완료'),
(6, 3, '2024-01-13 11:00:00', '/reports/2024/01/test_006_report.pdf', 'APPROVED', '최종 승인 완료'),
(11, 2, '2024-01-19 10:30:00', '/reports/2024/01/test_011_report.pdf', 'APPROVED', '최종 승인 완료');

-- 승인 시스템 샘플 데이터
INSERT INTO approvals (status) VALUES
('APPROVED'),
('APPROVED'),
('APPROVED'),
('PENDING'),
('PARTIAL_APPROVED'),
('PENDING');

-- 승인 요청 샘플 데이터
INSERT INTO approval_requests (approval_id, requester_id, comment) VALUES
(1, 4, '시험 결과 최종 승인 요청'),
(2, 4, '시험 완료 승인 요청'),
(3, 5, '글루코사민 시험 승인 요청'),
(4, 5, '비타민 C 시험 승인 요청'),
(5, 7, '단백질 파우더 시험 승인 요청'),
(6, 8, '철분 캡슐 긴급 승인 요청');

-- 승인 서명 샘플 데이터
INSERT INTO approval_signs (approval_id, approver_id, target_id, stage, status, comment) VALUES
(1, 2, 1, 'COMPLETED', 'APPROVED', '최종 승인'),
(1, 3, 1, 'COMPLETED', 'APPROVED', '품질 기준 적합'),
(2, 2, 6, 'COMPLETED', 'APPROVED', '승인 완료'),
(2, 6, 6, 'COMPLETED', 'APPROVED', '시험 결과 적합'),
(3, 3, 11, 'COMPLETED', 'APPROVED', '최종 승인'),
(3, 9, 11, 'COMPLETED', 'APPROVED', '품질 검증 완료'),
(4, 2, 2, 'RESULT_INPUT', 'PENDING', '결과 검토 중'),
(4, 6, 2, 'RESULT_INPUT', 'APPROVED', '시험 결과 적합'),
(5, 3, 7, 'RESULT_INPUT', 'PENDING', '추가 검토 필요'),
(5, 6, 7, 'RESULT_INPUT', 'APPROVED', '1차 승인 완료'),
(6, 2, 8, 'RECEIPT_APPROVAL', 'PENDING', '접수 승인 대기'),
(6, 3, 8, 'RECEIPT_APPROVAL', 'PENDING', '검토 중');

-- 감사 로그 샘플 데이터
INSERT INTO audit_logs (user_id, action, entity_type, entity_id, old_values, new_values, ip_address, user_agent) VALUES
(1, 'CREATE', 'TEST', 1, NULL, '{"product_id": 1, "requester_id": 4, "stage": "REQUEST"}', '192.168.1.100', 'Mozilla/5.0'),
(2, 'UPDATE', 'TEST', 1, '{"stage": "REQUEST"}', '{"stage": "RECEIPT"}', '192.168.1.101', 'Mozilla/5.0'),
(6, 'UPDATE', 'TEST', 1, '{"stage": "RECEIPT"}', '{"stage": "RESULT_INPUT"}', '192.168.1.102', 'Mozilla/5.0'),
(2, 'UPDATE', 'TEST', 1, '{"stage": "RESULT_INPUT"}', '{"stage": "COMPLETED"}', '192.168.1.101', 'Mozilla/5.0'),
(4, 'CREATE', 'TEST', 2, NULL, '{"product_id": 2, "requester_id": 4, "stage": "REQUEST"}', '192.168.1.103', 'Mozilla/5.0'),
(3, 'UPDATE', 'APPROVAL', 1, '{"status": "PENDING"}', '{"status": "APPROVED"}', '192.168.1.104', 'Mozilla/5.0'),
(9, 'CREATE', 'RESULT_INFO', 1, NULL, '{"test_id": 1, "pass_fail": "PASS"}', '192.168.1.105', 'Mozilla/5.0'),
(1, 'UPDATE', 'USER', 4, '{"role": "USER"}', '{"role": "USER"}', '192.168.1.100', 'Mozilla/5.0'),
(2, 'DELETE', 'TEMP_DATA', 999, '{"temp": "data"}', NULL, '192.168.1.101', 'Mozilla/5.0'),
(6, 'CREATE', 'COMPLETION_INFO', 1, NULL, '{"test_id": 1, "final_approval_status": "APPROVED"}', '192.168.1.102', 'Mozilla/5.0');