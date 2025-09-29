import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import { 
  FileText, Plus, Search, Eye, Clock, CheckCircle, 
  AlertCircle, Package, Calendar, RefreshCw
} from 'lucide-react';
import { TestStage, Test } from '@/types';
import { useAuth } from '@/contexts/AuthContext';

const stageBadgeColors = {
  REQUEST: 'bg-blue-100 text-blue-800',
  RECEIPT: 'bg-yellow-100 text-yellow-800',
  RECEIPT_APPROVAL: 'bg-purple-100 text-purple-800',
  RESULT_INPUT: 'bg-orange-100 text-orange-800',
  COMPLETED: 'bg-green-100 text-green-800'
};

const stageLabels = {
  REQUEST: '의뢰',
  RECEIPT: '접수',
  RECEIPT_APPROVAL: '접수결재',
  RESULT_INPUT: '결과입력',
  COMPLETED: '완료'
};

const MyTests: React.FC = () => {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [tests, setTests] = useState<Test[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedStage, setSelectedStage] = useState<TestStage | 'ALL'>('ALL');

  useEffect(() => {
    const fetchMyTests = async () => {
      setLoading(true);
      await new Promise(resolve => setTimeout(resolve, 800));
      
      // 현재 사용자의 시험만 필터링 (Mock data)
      const allTests: Test[] = [
        {
          id: 1,
          userId: 1,
          productId: 1,
          stage: 'REQUEST',
          createdAt: '2024-01-15T09:00:00',
          updatedAt: '2024-01-15T09:00:00',
          requestInfo: {
            id: 1,
            requesterId: 1,
            requesterName: '김연구',
            purpose: '신제품 품질검사',
            requestDate: '2024-01-15T09:00:00',
            expectedDate: '2024-01-20T17:00:00',
            notes: '긴급 검사 필요'
          },
          product: {
            id: 1,
            name: 'ABC-123 화합물',
            description: '신약 후보물질',
            createdAt: '2024-01-01T00:00:00',
            updatedAt: '2024-01-01T00:00:00'
          }
        },
        {
          id: 3,
          userId: 1,
          productId: 3,
          stage: 'COMPLETED',
          createdAt: '2024-01-10T11:15:00',
          updatedAt: '2024-01-13T15:45:00',
          requestInfo: {
            id: 3,
            requesterId: 1,
            requesterName: '김연구',
            purpose: '최종 제품 안정성 검사',
            requestDate: '2024-01-10T11:15:00',
            expectedDate: '2024-01-15T17:00:00'
          },
          receiptInfo: {
            id: 2,
            receiverId: 3,
            receiverName: '박접수',
            receiptDate: '2024-01-10T14:00:00',
            condition: '양호'
          },
          resultInfo: {
            id: 1,
            analystId: 4,
            analystName: '정시험',
            testMethod: 'HPLC 분석',
            result: '순도 99.8%',
            conclusion: '적합',
            testDate: '2024-01-12T16:30:00',
            notes: '모든 규격 만족'
          },
          completionInfo: {
            id: 1,
            reviewerId: 5,
            reviewerName: '최검토',
            completionDate: '2024-01-13T15:45:00',
            finalComment: '검사 완료, 출고 승인'
          },
          product: {
            id: 3,
            name: 'DEF-789 완제품',
            description: '최종 의약품',
            createdAt: '2024-01-01T00:00:00',
            updatedAt: '2024-01-01T00:00:00'
          }
        }
      ];
      
      // 현재 사용자의 시험만 필터링
      const myTests = allTests.filter(test => test.userId === user?.id);
      setTests(myTests);
      setLoading(false);
    };

    fetchMyTests();
  }, [user?.id]);

  const filteredTests = tests
    .filter(test => {
      const matchesSearch = !searchQuery || 
        test.product?.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        test.requestInfo?.purpose.toLowerCase().includes(searchQuery.toLowerCase());
      
      const matchesStage = selectedStage === 'ALL' || test.stage === selectedStage;
      
      return matchesSearch && matchesStage;
    })
    .sort((a, b) => new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime());

  const getStageIcon = (stage: TestStage) => {
    switch (stage) {
      case 'REQUEST': return <FileText className="w-4 h-4" />;
      case 'RECEIPT': return <Package className="w-4 h-4" />;
      case 'RECEIPT_APPROVAL': return <CheckCircle className="w-4 h-4" />;
      case 'RESULT_INPUT': return <AlertCircle className="w-4 h-4" />;
      case 'COMPLETED': return <CheckCircle className="w-4 h-4" />;
      default: return <Clock className="w-4 h-4" />;
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center py-12">
        <RefreshCw className="w-8 h-8 animate-spin text-indigo-600" />
        <span className="ml-3 text-lg text-gray-600">시험 데이터 로딩중...</span>
      </div>
    );
  }

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="space-y-6"
    >
      {/* 헤더 영역 */}
      <div className="bg-white rounded-xl shadow-sm p-6">
        <div className="flex items-center justify-between mb-6">
          <div className="flex items-center space-x-3">
            <FileText className="w-8 h-8 text-indigo-600" />
            <div>
              <h1 className="text-3xl font-bold text-gray-900">내 시험 의뢰</h1>
              <p className="text-gray-600 mt-1">내가 의뢰한 시험 현황을 확인합니다</p>
            </div>
          </div>
          
          <div className="flex items-center space-x-3">
            <button
              onClick={() => window.location.reload()}
              className="p-2 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-lg transition-colors"
            >
              <RefreshCw className="w-5 h-5" />
            </button>
            
            <button
              onClick={() => navigate('/tests/new')}
              className="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 flex items-center space-x-2 transition-colors"
            >
              <Plus className="w-5 h-5" />
              <span>새 시험 의뢰</span>
            </button>
          </div>
        </div>

        {/* 검색 및 필터 */}
        <div className="flex items-center justify-between space-x-4">
          <div className="flex-1 max-w-md">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
              <input
                type="text"
                placeholder="제품명이나 목적으로 검색..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-10 pr-4 py-2.5 border border-gray-200 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              />
            </div>
          </div>

          <select
            value={selectedStage}
            onChange={(e) => setSelectedStage(e.target.value as TestStage | 'ALL')}
            className="px-3 py-2.5 border border-gray-200 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
          >
            <option value="ALL">모든 단계</option>
            <option value="REQUEST">의뢰</option>
            <option value="RECEIPT">접수</option>
            <option value="RECEIPT_APPROVAL">접수결재</option>
            <option value="RESULT_INPUT">결과입력</option>
            <option value="COMPLETED">완료</option>
          </select>
        </div>

        {/* 통계 카드 */}
        <div className="grid grid-cols-2 lg:grid-cols-5 gap-4 mt-6">
          {Object.entries(stageLabels).map(([stage, label]) => {
            const count = tests.filter(test => test.stage === stage).length;
            const color = stageBadgeColors[stage as TestStage];
            
            return (
              <div key={stage} className="bg-gray-50 rounded-lg p-4">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm text-gray-600">{label}</p>
                    <p className="text-2xl font-bold text-gray-900">{count}</p>
                  </div>
                  <div className={`p-2 rounded-lg ${color}`}>
                    {getStageIcon(stage as TestStage)}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* 시험 목록 */}
      <div className="space-y-4">
        {filteredTests.map((test) => (
          <motion.div
            key={test.id}
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 hover:shadow-md transition-shadow cursor-pointer"
            onClick={() => navigate(`/tests/${test.id}`)}
          >
            <div className="flex items-start justify-between">
              <div className="flex-1">
                <div className="flex items-center space-x-3 mb-3">
                  <div className={`p-2 rounded-lg ${stageBadgeColors[test.stage]}`}>
                    {getStageIcon(test.stage)}
                  </div>
                  <div>
                    <h3 className="font-semibold text-gray-900">
                      #{test.id.toString().padStart(4, '0')} - {test.requestInfo?.purpose}
                    </h3>
                    <p className="text-sm text-gray-500">
                      제품: {test.product?.name}
                    </p>
                  </div>
                  <span className={`inline-flex items-center px-2.5 py-1 rounded-full text-xs font-medium ${stageBadgeColors[test.stage]}`}>
                    {stageLabels[test.stage]}
                  </span>
                </div>
                
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
                  <div className="flex items-center text-gray-600">
                    <Calendar className="w-4 h-4 mr-2" />
                    <span>의뢰일: {formatDate(test.createdAt)}</span>
                  </div>
                  {test.requestInfo?.expectedDate && (
                    <div className="flex items-center text-gray-600">
                      <Clock className="w-4 h-4 mr-2" />
                      <span>예정일: {formatDate(test.requestInfo.expectedDate)}</span>
                    </div>
                  )}
                  <div className="flex items-center text-gray-600">
                    <Package className="w-4 h-4 mr-2" />
                    <span>업데이트: {formatDate(test.updatedAt)}</span>
                  </div>
                </div>

                {test.requestInfo?.notes && (
                  <div className="mt-3 p-3 bg-gray-50 rounded-lg">
                    <p className="text-sm text-gray-600">
                      <strong>메모:</strong> {test.requestInfo.notes}
                    </p>
                  </div>
                )}
              </div>
              
              <button
                onClick={(e) => {
                  e.stopPropagation();
                  navigate(`/tests/${test.id}`);
                }}
                className="ml-4 text-indigo-600 hover:text-indigo-700 flex items-center space-x-1"
              >
                <Eye className="w-4 h-4" />
                <span>상세보기</span>
              </button>
            </div>
          </motion.div>
        ))}
      </div>

      {filteredTests.length === 0 && (
        <div className="bg-white rounded-xl shadow-sm p-12 text-center">
          <FileText className="w-16 h-16 mx-auto text-gray-300 mb-4" />
          <p className="text-lg text-gray-500 mb-2">
            {searchQuery ? '검색 결과가 없습니다' : '의뢰한 시험이 없습니다'}
          </p>
          <p className="text-sm text-gray-400 mb-6">
            {searchQuery ? '다른 검색어를 시도해보세요' : '새로운 시험을 의뢰해보세요'}
          </p>
          {!searchQuery && (
            <button
              onClick={() => navigate('/tests/new')}
              className="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 inline-flex items-center space-x-2"
            >
              <Plus className="w-5 h-5" />
              <span>새 시험 의뢰</span>
            </button>
          )}
        </div>
      )}
    </motion.div>
  );
};

export default MyTests;