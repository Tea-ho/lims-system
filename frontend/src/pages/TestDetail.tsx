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
  Edit
} from 'lucide-react';
import { Test, TestStage } from '@/types';
import { TestService } from '@/services/testService';
import LoadingSpinner from '@/components/LoadingSpinner';

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
        {test.receiptInfo && (
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
    </motion.div>
  );
};

export default TestDetail;