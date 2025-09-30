import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import {
  ArrowLeft,
  FileText,
  Inbox,
  Beaker,
  CheckCircle,
  Clock,
  AlertTriangle,
  Eye,
  Edit,
  X
} from 'lucide-react';
import { Test, TestStage } from '@/types';
import { TestService } from '@/services/testService';
import LoadingSpinner from '@/components/LoadingSpinner';
import toast from 'react-hot-toast';

// 스테이지별 색상 및 아이콘 매핑 (Lucide React 사용)
const getStageInfo = (stage: TestStage) => {
  switch (stage) {
    case 'REQUEST':
      return {
        color: 'bg-blue-100 text-blue-800',
        icon: FileText,
        label: '의뢰'
      };
    case 'RECEIPT':
      return {
        color: 'bg-yellow-100 text-yellow-800',
        icon: Inbox,
        label: '접수'
      };
    case 'RECEIPT_APPROVAL':
      return {
        color: 'bg-orange-100 text-orange-800',
        icon: AlertTriangle,
        label: '접수 승인'
      };
    case 'RESULT_INPUT':
      return {
        color: 'bg-purple-100 text-purple-800',
        icon: Beaker,
        label: '결과 입력'
      };
    case 'COMPLETED':
      return {
        color: 'bg-green-100 text-green-800',
        icon: CheckCircle,
        label: '완료'
      };
    default:
      return {
        color: 'bg-gray-100 text-gray-800',
        icon: Clock,
        label: '알 수 없음'
      };
  }
};

const TestDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [test, setTest] = useState<Test | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // 접수 모달 상태
  const [showReceiptModal, setShowReceiptModal] = useState(false);
  const [receiptDetails, setReceiptDetails] = useState('');
  const [requiresApproval, setRequiresApproval] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  // 접수승인 요청 모달 상태
  const [showApprovalModal, setShowApprovalModal] = useState(false);
  const [approvalTitle, setApprovalTitle] = useState('');
  const [approvalDescription, setApprovalDescription] = useState('');
  const [selectedApprovers, setSelectedApprovers] = useState<number[]>([]);
  const [users, setUsers] = useState<any[]>([]);

  useEffect(() => {
    const fetchTest = async () => {
      if (!id) {
        setError('시험 ID가 없습니다.');
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        const testData = await TestService.getTestById(Number(id));
        console.log('📋 Test Data:', testData);
        console.log('📋 Stage:', testData.stage);
        console.log('📋 Receipt Info:', testData.receiptInfo);
        setTest(testData);
        setError(null);
      } catch (err) {
        console.error('시험 상세 정보 로드 실패:', err);
        setError('시험 상세 정보를 불러오는데 실패했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchTest();
  }, [id]);

  // 사용자 목록 조회 (ROLE_ADMIN, ROLE_MANAGER만)
  useEffect(() => {
    const fetchUsers = async () => {
      try {
        // ROLE_ADMIN과 ROLE_MANAGER를 각각 조회
        const [adminResponse, managerResponse] = await Promise.all([
          fetch('http://localhost:8089/api/users?authorities=ROLE_ADMIN&size=100', {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
          }),
          fetch('http://localhost:8089/api/users?authorities=ROLE_MANAGER&size=100', {
            headers: {
              'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
          })
        ]);

        const adminData = await adminResponse.json();
        const managerData = await managerResponse.json();

        console.log('👥 Admin Response:', adminData);
        console.log('👥 Manager Response:', managerData);

        // ApiResponse 구조 처리: data.data.content가 실제 배열
        const adminList = adminData?.data?.content || [];
        const managerList = managerData?.data?.content || [];

        // 중복 제거 (id 기준)
        const allUsers = [...adminList, ...managerList];
        const uniqueUsers = Array.from(new Map(allUsers.map(user => [user.id, user])).values());

        console.log('👥 Combined User List:', uniqueUsers);
        setUsers(uniqueUsers);
      } catch (error) {
        console.error('사용자 목록 조회 실패:', error);
        setUsers([]);
      }
    };

    fetchUsers();
  }, []);

  // 접수 처리 함수
  const handleReceipt = async () => {
    if (!receiptDetails.trim()) {
      toast.error('접수 상세 내용을 입력해주세요.');
      return;
    }

    if (!test?.id) {
      toast.error('시험 ID가 없습니다.');
      return;
    }

    try {
      setSubmitting(true);
      const updatedTest = await TestService.receiptTest(test.id, {
        receiptDetails: receiptDetails.trim(),
        requiresApproval
      });

      setTest(updatedTest);
      setShowReceiptModal(false);
      setReceiptDetails('');
      setRequiresApproval(false);
      toast.success('시험이 접수되었습니다.');
    } catch (error) {
      console.error('접수 처리 실패:', error);
      toast.error('접수 처리에 실패했습니다.');
    } finally {
      setSubmitting(false);
    }
  };

  // 접수승인 요청 처리 함수
  const handleReceiptApproval = async () => {
    if (!approvalTitle.trim()) {
      toast.error('승인 제목을 입력해주세요.');
      return;
    }

    if (!approvalDescription.trim()) {
      toast.error('승인 설명을 입력해주세요.');
      return;
    }

    if (selectedApprovers.length === 0) {
      toast.error('최소 1명의 승인자를 선택해주세요.');
      return;
    }

    if (!test?.id) {
      toast.error('시험 ID가 없습니다.');
      return;
    }

    // 현재 사용자 정보 가져오기
    const userStr = localStorage.getItem('user');
    const currentUser = userStr ? JSON.parse(userStr) : null;

    if (!currentUser?.id) {
      toast.error('사용자 정보를 찾을 수 없습니다.');
      return;
    }

    try {
      setSubmitting(true);

      // ApprovalCreateDto 형식에 맞게 데이터 구성
      // 백엔드가 기대하는 형식: { requesterId, comment, signs: [{ approverId, targetId, stage }] }
      const approvalData = {
        requesterId: currentUser.id,
        comment: `${approvalTitle.trim()}\n\n${approvalDescription.trim()}`,
        signs: selectedApprovers.map(approverId => ({
          approverId: approverId,
          targetId: test.id,
          stage: 'RECEIPT_APPROVAL' as const
        }))
      };

      console.log('📋 Sending approval data:', approvalData);

      const updatedTest = await TestService.receiptApproval(test.id, approvalData);

      setTest(updatedTest);
      setShowApprovalModal(false);
      setApprovalTitle('');
      setApprovalDescription('');
      setSelectedApprovers([]);
      toast.success('접수승인 요청이 완료되었습니다.');
    } catch (error) {
      console.error('접수승인 요청 실패:', error);
      toast.error('접수승인 요청에 실패했습니다.');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-96">
        <LoadingSpinner />
      </div>
    );
  }

  if (error) {
    return (
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="p-6"
      >
        <div className="text-center py-12">
          <AlertTriangle className="mx-auto h-12 w-12 text-red-400" />
          <h3 className="mt-2 text-sm font-medium text-gray-900">오류 발생</h3>
          <p className="mt-1 text-sm text-gray-500">{error}</p>
          <div className="mt-6">
            <button
              type="button"
              onClick={() => navigate('/tests')}
              className="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              <ArrowLeft className="-ml-1 mr-2 h-4 w-4" />
              시험 목록으로 돌아가기
            </button>
          </div>
        </div>
      </motion.div>
    );
  }

  if (!test) {
    return (
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="p-6"
      >
        <div className="text-center py-12">
          <FileText className="mx-auto h-12 w-12 text-gray-400" />
          <h3 className="mt-2 text-sm font-medium text-gray-900">시험을 찾을 수 없습니다</h3>
          <p className="mt-1 text-sm text-gray-500">요청하신 시험이 존재하지 않습니다.</p>
        </div>
      </motion.div>
    );
  }

  const stageInfo = getStageInfo(test.stage);
  const StageIcon = stageInfo.icon;

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="p-6 max-w-7xl mx-auto"
    >
      {/* Header */}
      <div className="mb-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <button
              onClick={() => navigate('/tests')}
              className="inline-flex items-center px-3 py-2 border border-gray-300 shadow-sm text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              <ArrowLeft className="-ml-1 mr-2 h-4 w-4" />
              뒤로
            </button>
            <div>
              <h1 className="text-2xl font-bold text-gray-900">시험 상세 정보</h1>
              <p className="text-sm text-gray-500">시험 ID: {test.id}</p>
            </div>
          </div>
          <div className="flex items-center space-x-3">
            <span className={`inline-flex items-center px-3 py-1 rounded-full text-sm font-medium ${stageInfo.color}`}>
              <StageIcon className="w-4 h-4 mr-1.5" />
              {stageInfo.label}
            </span>
            {test.stage === 'REQUEST' && (
              <button
                onClick={() => setShowReceiptModal(true)}
                className="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-yellow-600 hover:bg-yellow-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-yellow-500"
              >
                <Inbox className="-ml-1 mr-2 h-4 w-4" />
                접수
              </button>
            )}
            {test.stage === 'RECEIPT' && test.receiptInfo?.requiresApproval && (
              <button
                onClick={() => setShowApprovalModal(true)}
                className="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-purple-600 hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-purple-500"
              >
                <CheckCircle className="-ml-1 mr-2 h-4 w-4" />
                접수승인 요청
              </button>
            )}
            <button
              onClick={() => {/* 수정 기능 추가 */}}
              className="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              <Edit className="-ml-1 mr-2 h-4 w-4" />
              수정
            </button>
          </div>
        </div>
      </div>

      {/* 시험 기본 정보 */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
        <div className="lg:col-span-2">
          <div className="bg-white shadow rounded-lg">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-lg font-medium text-gray-900">기본 정보</h2>
            </div>
            <div className="px-6 py-4">
              <dl className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <dt className="text-sm font-medium text-gray-500">생성일시</dt>
                  <dd className="mt-1 text-sm text-gray-900">
                    {new Date(test.createdAt).toLocaleString('ko-KR')}
                  </dd>
                </div>
                <div>
                  <dt className="text-sm font-medium text-gray-500">최종 수정일시</dt>
                  <dd className="mt-1 text-sm text-gray-900">
                    {test.updatedAt ? new Date(test.updatedAt).toLocaleString('ko-KR') : '-'}
                  </dd>
                </div>
                <div>
                  <dt className="text-sm font-medium text-gray-500">의뢰자</dt>
                  <dd className="mt-1 text-sm text-gray-900">
                    {test.user?.username || '알 수 없는 사용자'}
                  </dd>
                </div>
                <div>
                  <dt className="text-sm font-medium text-gray-500">제품</dt>
                  <dd className="mt-1 text-sm text-gray-900">
                    {test.product?.name || '알 수 없는 제품'}
                  </dd>
                </div>
                {test.product?.description && (
                  <div className="sm:col-span-2">
                    <dt className="text-sm font-medium text-gray-500">제품 설명</dt>
                    <dd className="mt-1 text-sm text-gray-900">{test.product.description}</dd>
                  </div>
                )}
              </dl>
            </div>
          </div>
        </div>

        {/* 진행 상태 */}
        <div>
          <div className="bg-white shadow rounded-lg">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-lg font-medium text-gray-900">진행 상태</h2>
            </div>
            <div className="px-6 py-4">
              <div className="space-y-4">
                {(['REQUEST', 'RECEIPT', 'RECEIPT_APPROVAL', 'RESULT_INPUT', 'COMPLETED'] as TestStage[]).map((stage, index) => {
                  const isActive = test.stage === stage;
                  const isPassed = (['REQUEST', 'RECEIPT', 'RECEIPT_APPROVAL', 'RESULT_INPUT', 'COMPLETED'] as TestStage[]).indexOf(test.stage) > index;
                  const stageConfig = getStageInfo(stage);
                  const StageIconComponent = stageConfig.icon;

                  return (
                    <div key={stage} className={`flex items-center space-x-3 ${isActive ? 'text-blue-600' : isPassed ? 'text-green-600' : 'text-gray-400'}`}>
                      <div className={`flex-shrink-0 w-8 h-8 rounded-full flex items-center justify-center ${
                        isActive ? 'bg-blue-100' : isPassed ? 'bg-green-100' : 'bg-gray-100'
                      }`}>
                        {isPassed ? (
                          <CheckCircle className="w-5 h-5 text-green-600" />
                        ) : (
                          <StageIconComponent className={`w-5 h-5 ${isActive ? 'text-blue-600' : 'text-gray-400'}`} />
                        )}
                      </div>
                      <div className="flex-1">
                        <p className={`text-sm font-medium ${isActive ? 'text-blue-900' : isPassed ? 'text-green-900' : 'text-gray-500'}`}>
                          {stageConfig.label}
                        </p>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* 상세 정보 섹션들 */}
      <div className="space-y-6">
        {/* 의뢰 정보 */}
        {test.requestInfo && (
          <div className="bg-white shadow rounded-lg">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-lg font-medium text-gray-900 flex items-center">
                <FileText className="w-5 h-5 mr-2 text-blue-500" />
                의뢰 정보
              </h2>
            </div>
            <div className="px-6 py-4">
              <dl className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div className="sm:col-span-2">
                  <dt className="text-sm font-medium text-gray-500">제목</dt>
                  <dd className="mt-1 text-sm text-gray-900">{test.requestInfo.title}</dd>
                </div>
                <div className="sm:col-span-2">
                  <dt className="text-sm font-medium text-gray-500">설명</dt>
                  <dd className="mt-1 text-sm text-gray-900 whitespace-pre-wrap">{test.requestInfo.description}</dd>
                </div>
                <div>
                  <dt className="text-sm font-medium text-gray-500">승인 필요 여부</dt>
                  <dd className="mt-1">
                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                      test.requestInfo.requiresApproval
                        ? 'bg-yellow-100 text-yellow-800'
                        : 'bg-green-100 text-green-800'
                    }`}>
                      {test.requestInfo.requiresApproval ? '승인 필요' : '승인 불필요'}
                    </span>
                  </dd>
                </div>
              </dl>
            </div>
          </div>
        )}

        {/* 접수 정보 */}
        {test.receiptInfo && (test.stage === 'RECEIPT' || test.stage === 'RECEIPT_APPROVAL' || test.stage === 'RESULT_INPUT' || test.stage === 'COMPLETED') && (
          <div className="bg-white shadow rounded-lg">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-lg font-medium text-gray-900 flex items-center">
                <Inbox className="w-5 h-5 mr-2 text-yellow-500" />
                접수 정보
              </h2>
            </div>
            <div className="px-6 py-4">
              <dl className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div className="sm:col-span-2">
                  <dt className="text-sm font-medium text-gray-500">접수 상세</dt>
                  <dd className="mt-1 text-sm text-gray-900 whitespace-pre-wrap">{test.receiptInfo.receiptDetails}</dd>
                </div>
                <div>
                  <dt className="text-sm font-medium text-gray-500">승인 필요 여부</dt>
                  <dd className="mt-1">
                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                      test.receiptInfo.requiresApproval
                        ? 'bg-yellow-100 text-yellow-800'
                        : 'bg-green-100 text-green-800'
                    }`}>
                      {test.receiptInfo.requiresApproval ? '승인 필요' : '승인 불필요'}
                    </span>
                  </dd>
                </div>
              </dl>
            </div>
          </div>
        )}

        {/* 결과 정보 */}
        {test.resultInfo && (
          <div className="bg-white shadow rounded-lg">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-lg font-medium text-gray-900 flex items-center">
                <Beaker className="w-5 h-5 mr-2 text-purple-500" />
                결과 정보
              </h2>
            </div>
            <div className="px-6 py-4">
              <dl className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div className="sm:col-span-2">
                  <dt className="text-sm font-medium text-gray-500">결과 데이터</dt>
                  <dd className="mt-1 text-sm text-gray-900 whitespace-pre-wrap">{test.resultInfo.resultData}</dd>
                </div>
                <div>
                  <dt className="text-sm font-medium text-gray-500">승인 필요 여부</dt>
                  <dd className="mt-1">
                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                      test.resultInfo.requiresApproval
                        ? 'bg-yellow-100 text-yellow-800'
                        : 'bg-green-100 text-green-800'
                    }`}>
                      {test.resultInfo.requiresApproval ? '승인 필요' : '승인 불필요'}
                    </span>
                  </dd>
                </div>
              </dl>
            </div>
          </div>
        )}

        {/* 완료 정보 */}
        {test.completionInfo && (
          <div className="bg-white shadow rounded-lg">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-lg font-medium text-gray-900 flex items-center">
                <CheckCircle className="w-5 h-5 mr-2 text-green-500" />
                완료 정보
              </h2>
            </div>
            <div className="px-6 py-4">
              <dl className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div className="sm:col-span-2">
                  <dt className="text-sm font-medium text-gray-500">완료 노트</dt>
                  <dd className="mt-1 text-sm text-gray-900 whitespace-pre-wrap">{test.completionInfo.completionNotes}</dd>
                </div>
                <div>
                  <dt className="text-sm font-medium text-gray-500">승인 필요 여부</dt>
                  <dd className="mt-1">
                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                      test.completionInfo.requiresApproval
                        ? 'bg-yellow-100 text-yellow-800'
                        : 'bg-green-100 text-green-800'
                    }`}>
                      {test.completionInfo.requiresApproval ? '승인 필요' : '승인 불필요'}
                    </span>
                  </dd>
                </div>
              </dl>
            </div>
          </div>
        )}
      </div>

      {/* 접수 모달 */}
      {showReceiptModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <motion.div
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            className="bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto"
          >
            {/* 모달 헤더 */}
            <div className="sticky top-0 bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between">
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-yellow-100 rounded-lg">
                  <Inbox className="w-5 h-5 text-yellow-600" />
                </div>
                <div>
                  <h3 className="text-lg font-semibold text-gray-900">시험 접수</h3>
                  <p className="text-sm text-gray-500">시험 #{test.id} - {test.requestInfo?.title}</p>
                </div>
              </div>
              <button
                onClick={() => {
                  setShowReceiptModal(false);
                  setReceiptDetails('');
                  setRequiresApproval(false);
                }}
                className="text-gray-400 hover:text-gray-600 transition-colors"
                disabled={submitting}
              >
                <X className="w-6 h-6" />
              </button>
            </div>

            {/* 모달 본문 */}
            <div className="px-6 py-4 space-y-4">
              {/* 시험 정보 요약 */}
              <div className="bg-gray-50 rounded-lg p-4 space-y-2">
                <div className="flex items-center justify-between text-sm">
                  <span className="text-gray-600">제품</span>
                  <span className="font-medium text-gray-900">{test.product?.name}</span>
                </div>
                <div className="flex items-center justify-between text-sm">
                  <span className="text-gray-600">의뢰자</span>
                  <span className="font-medium text-gray-900">{test.user?.username}</span>
                </div>
                <div className="flex items-start justify-between text-sm">
                  <span className="text-gray-600">의뢰 내용</span>
                  <span className="font-medium text-gray-900 text-right max-w-xs">
                    {test.requestInfo?.description}
                  </span>
                </div>
              </div>

              {/* 접수 상세 입력 */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  접수 상세 내용 <span className="text-red-500">*</span>
                </label>
                <textarea
                  value={receiptDetails}
                  onChange={(e) => setReceiptDetails(e.target.value)}
                  placeholder="접수 시 확인한 사항, 특이사항 등을 입력하세요..."
                  rows={6}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-yellow-500 focus:border-transparent resize-none"
                  disabled={submitting}
                />
                <p className="mt-1 text-xs text-gray-500">
                  샘플 상태, 수량, 보관 조건 등 접수 시 확인한 내용을 작성해주세요.
                </p>
              </div>

              {/* 승인 필요 여부 */}
              <div className="flex items-start space-x-3">
                <input
                  type="checkbox"
                  id="requiresApproval"
                  checked={requiresApproval}
                  onChange={(e) => setRequiresApproval(e.target.checked)}
                  className="mt-1 h-4 w-4 text-yellow-600 focus:ring-yellow-500 border-gray-300 rounded"
                  disabled={submitting}
                />
                <div>
                  <label htmlFor="requiresApproval" className="text-sm font-medium text-gray-700 cursor-pointer">
                    접수 승인 필요
                  </label>
                  <p className="text-xs text-gray-500 mt-0.5">
                    체크하면 접수 후 승인 단계를 거치게 됩니다.
                  </p>
                </div>
              </div>
            </div>

            {/* 모달 푸터 */}
            <div className="sticky bottom-0 bg-gray-50 border-t border-gray-200 px-6 py-4 flex items-center justify-end space-x-3">
              <button
                onClick={() => {
                  setShowReceiptModal(false);
                  setReceiptDetails('');
                  setRequiresApproval(false);
                }}
                className="px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-100 transition-colors"
                disabled={submitting}
              >
                취소
              </button>
              <button
                onClick={handleReceipt}
                disabled={submitting || !receiptDetails.trim()}
                className="px-4 py-2 bg-yellow-600 text-white rounded-lg hover:bg-yellow-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors flex items-center space-x-2"
              >
                {submitting ? (
                  <>
                    <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
                    <span>접수 처리 중...</span>
                  </>
                ) : (
                  <>
                    <Inbox className="w-4 h-4" />
                    <span>접수 완료</span>
                  </>
                )}
              </button>
            </div>
          </motion.div>
        </div>
      )}

      {/* 접수승인 요청 모달 */}
      {showApprovalModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <motion.div
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            className="bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto"
          >
            {/* 모달 헤더 */}
            <div className="sticky top-0 bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between">
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-purple-100 rounded-lg">
                  <CheckCircle className="w-5 h-5 text-purple-600" />
                </div>
                <div>
                  <h3 className="text-lg font-semibold text-gray-900">접수승인 요청</h3>
                  <p className="text-sm text-gray-500">시험 #{test.id} - {test.requestInfo?.title}</p>
                </div>
              </div>
              <button
                onClick={() => {
                  setShowApprovalModal(false);
                  setApprovalTitle('');
                  setApprovalDescription('');
                  setSelectedApprovers([]);
                }}
                className="text-gray-400 hover:text-gray-600 transition-colors"
                disabled={submitting}
              >
                <X className="w-6 h-6" />
              </button>
            </div>

            {/* 모달 본문 */}
            <div className="px-6 py-4 space-y-4">
              {/* 접수 정보 요약 */}
              <div className="bg-gray-50 rounded-lg p-4 space-y-2">
                <h4 className="text-sm font-medium text-gray-900 mb-2">접수 정보</h4>
                <div className="text-sm text-gray-600 whitespace-pre-wrap">
                  {test.receiptInfo?.receiptDetails}
                </div>
              </div>

              {/* 승인 제목 */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  승인 제목 <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  value={approvalTitle}
                  onChange={(e) => setApprovalTitle(e.target.value)}
                  placeholder="승인 제목을 입력하세요"
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
                  disabled={submitting}
                />
              </div>

              {/* 승인 설명 */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  승인 설명 <span className="text-red-500">*</span>
                </label>
                <textarea
                  value={approvalDescription}
                  onChange={(e) => setApprovalDescription(e.target.value)}
                  placeholder="승인 요청 사유 및 상세 내용을 입력하세요..."
                  rows={6}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent resize-none"
                  disabled={submitting}
                />
              </div>

              {/* 승인자 선택 */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  승인자 선택 <span className="text-red-500">*</span>
                </label>
                <div className="border border-gray-300 rounded-lg max-h-48 overflow-y-auto">
                  {Array.isArray(users) && users.length > 0 ? (
                    users.map((user) => (
                      <label
                        key={user.id}
                        className="flex items-center px-3 py-2 hover:bg-gray-50 cursor-pointer"
                      >
                        <input
                          type="checkbox"
                          checked={selectedApprovers.includes(user.id)}
                          onChange={(e) => {
                            if (e.target.checked) {
                              setSelectedApprovers([...selectedApprovers, user.id]);
                            } else {
                              setSelectedApprovers(selectedApprovers.filter(id => id !== user.id));
                            }
                          }}
                          className="h-4 w-4 text-purple-600 focus:ring-purple-500 border-gray-300 rounded"
                          disabled={submitting}
                        />
                        <span className="ml-3 text-sm text-gray-900">{user.username}</span>
                      </label>
                    ))
                  ) : (
                    <div className="px-3 py-2 text-sm text-gray-500 text-center">
                      사용자 목록을 불러오는 중...
                    </div>
                  )}
                </div>
                <p className="mt-1 text-xs text-gray-500">
                  선택된 승인자: {selectedApprovers.length}명
                </p>
              </div>
            </div>

            {/* 모달 푸터 */}
            <div className="sticky bottom-0 bg-gray-50 border-t border-gray-200 px-6 py-4 flex items-center justify-end space-x-3">
              <button
                onClick={() => {
                  setShowApprovalModal(false);
                  setApprovalTitle('');
                  setApprovalDescription('');
                  setSelectedApprovers([]);
                }}
                className="px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-100 transition-colors"
                disabled={submitting}
              >
                취소
              </button>
              <button
                onClick={handleReceiptApproval}
                disabled={submitting || !approvalTitle.trim() || !approvalDescription.trim() || selectedApprovers.length === 0}
                className="px-4 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors flex items-center space-x-2"
              >
                {submitting ? (
                  <>
                    <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
                    <span>요청 처리 중...</span>
                  </>
                ) : (
                  <>
                    <CheckCircle className="w-4 h-4" />
                    <span>승인 요청</span>
                  </>
                )}
              </button>
            </div>
          </motion.div>
        </div>
      )}
    </motion.div>
  );
};

export default TestDetail;