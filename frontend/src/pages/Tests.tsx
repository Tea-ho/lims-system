import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { Link, useNavigate } from 'react-router-dom';
import {
  FileText, Plus, Search, Filter, Eye, Clock, CheckCircle,
  XCircle, AlertCircle, Package, User, Calendar, ChevronRight,
  RefreshCw, Download, Upload
} from 'lucide-react';
import { TestStage, Test } from '@/types';
import { TestService } from '@/services/testService';
import toast from 'react-hot-toast';

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

const Tests: React.FC = () => {
  const navigate = useNavigate();
  const [tests, setTests] = useState<Test[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedStage, setSelectedStage] = useState<TestStage | 'ALL'>('ALL');
  const [sortBy, setSortBy] = useState<'date' | 'stage' | 'product'>('date');
  const [showFilters, setShowFilters] = useState(false);

  // 페이지네이션 상태 추가
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  // 실제 API에서 데이터 가져오기
  useEffect(() => {
    const fetchTests = async () => {
      setLoading(true);
      try {
        const testsData = await TestService.getAllTests();
        setTests(testsData);
        setTotalElements(testsData.length);
        setTotalPages(Math.ceil(testsData.length / size));
      } catch (error) {
        console.error('테스트 데이터를 가져오는 중 오류 발생:', error);
        toast.error('테스트 데이터를 불러오는데 실패했습니다.');
        setTests([]);
      } finally {
        setLoading(false);
      }
    };

    fetchTests();
  }, [size]);

  const refreshTests = async () => {
    try {
      setLoading(true);
      const testsData = await TestService.getAllTests();
      setTests(testsData);
      setTotalElements(testsData.length);
      setTotalPages(Math.ceil(testsData.length / size));
      toast.success('데이터를 새로고침했습니다.');
    } catch (error) {
      console.error('테스트 데이터 새로고침 중 오류:', error);
      toast.error('데이터 새로고침에 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  // 필터링 및 정렬
  const allFilteredTests = tests
    .filter(test => {
      const matchesSearch = !searchQuery ||
        test.product?.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        test.requestInfo?.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
        test.requestInfo?.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
        test.user?.username.toLowerCase().includes(searchQuery.toLowerCase());

      const matchesStage = selectedStage === 'ALL' || test.stage === selectedStage;

      return matchesSearch && matchesStage;
    })
    .sort((a, b) => {
      switch (sortBy) {
        case 'date':
          return new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime();
        case 'stage':
          return a.stage.localeCompare(b.stage);
        case 'product':
          return (a.product?.name || '').localeCompare(b.product?.name || '');
        default:
          return 0;
      }
    });

  // 페이지네이션 적용
  const filteredTests = allFilteredTests.slice(page * size, (page + 1) * size);

  // 필터링된 결과에 대한 페이지네이션 정보 업데이트
  React.useEffect(() => {
    setTotalElements(allFilteredTests.length);
    setTotalPages(Math.ceil(allFilteredTests.length / size));

    // 현재 페이지가 총 페이지 수를 초과하면 첫 페이지로 이동
    if (page >= Math.ceil(allFilteredTests.length / size) && allFilteredTests.length > 0) {
      setPage(0);
    }
  }, [allFilteredTests.length, size, page]);

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
              <h1 className="text-3xl font-bold text-gray-900">시험 관리</h1>
              <p className="text-gray-600 mt-1">실험실 시험을 관리하고 진행 상황을 추적합니다</p>
            </div>
          </div>

          <div className="flex items-center space-x-3">
            <button
              onClick={refreshTests}
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

        {/* 검색 및 필터 영역 */}
        <div className="flex items-center justify-between space-x-4">
          <div className="flex-1 max-w-md">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
              <input
                type="text"
                placeholder="제품명, 목적, 담당자로 검색..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-10 pr-4 py-2.5 border border-gray-200 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              />
            </div>
          </div>

          <div className="flex items-center space-x-4">
            {/* 단계 필터 */}
            <select
              value={selectedStage}
              onChange={(e) => setSelectedStage(e.target.value as TestStage | 'ALL')}
              className="px-3 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
            >
              <option value="ALL">모든 단계</option>
              <option value="REQUEST">의뢰</option>
              <option value="RECEIPT">접수</option>
              <option value="RECEIPT_APPROVAL">접수결재</option>
              <option value="RESULT_INPUT">결과입력</option>
              <option value="COMPLETED">완료</option>
            </select>

            {/* 정렬 */}
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value as 'date' | 'stage' | 'product')}
              className="px-3 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
            >
              <option value="date">날짜순</option>
              <option value="stage">단계순</option>
              <option value="product">제품순</option>
            </select>

            <div className="text-sm text-gray-500">
              총 {totalElements}개 시험
            </div>
          </div>
        </div>
      </div>

      {/* 시험 목록 */}
      <div className="space-y-4">
        {filteredTests.map((test) => (
          <motion.div
            key={test.id}
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            className="bg-white rounded-xl shadow-sm border border-gray-100 hover:shadow-md transition-shadow"
          >
            <div className="p-6">
              <div className="flex items-center justify-between mb-4">
                <div className="flex items-center space-x-4">
                  <div className="p-2 bg-indigo-100 rounded-lg">
                    {getStageIcon(test.stage)}
                  </div>

                  <div>
                    <div className="flex items-center space-x-2 mb-1">
                      <h3 className="font-semibold text-gray-900">
                        시험 #{test.id}
                      </h3>
                      <span className={`px-2 py-1 text-xs rounded-full ${stageBadgeColors[test.stage]}`}>
                        {stageLabels[test.stage]}
                      </span>
                    </div>
                    <p className="text-sm text-gray-600">
                      {test.requestInfo?.title || '제목 정보 없음'}
                    </p>
                  </div>
                </div>

                <button
                  onClick={() => navigate(`/tests/${test.id}`)}
                  className="flex items-center space-x-1 text-indigo-600 hover:text-indigo-700 transition-colors"
                >
                  <Eye className="w-4 h-4" />
                  <span>상세보기</span>
                  <ChevronRight className="w-4 h-4" />
                </button>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
                <div className="flex items-center space-x-2">
                  <Package className="w-4 h-4 text-gray-400" />
                  <span className="text-gray-600">제품:</span>
                  <span className="font-medium">{test.product?.name || '제품 정보 없음'}</span>
                </div>

                <div className="flex items-center space-x-2">
                  <User className="w-4 h-4 text-gray-400" />
                  <span className="text-gray-600">의뢰자:</span>
                  <span className="font-medium">{test.user?.username || '사용자 정보 없음'}</span>
                </div>

                <div className="flex items-center space-x-2">
                  <Calendar className="w-4 h-4 text-gray-400" />
                  <span className="text-gray-600">생성일:</span>
                  <span className="font-medium">{formatDate(test.createdAt)}</span>
                </div>
              </div>

              {test.stage !== 'REQUEST' && test.updatedAt !== test.createdAt && (
                <div className="mt-3 text-xs text-gray-500">
                  최종 업데이트: {formatDate(test.updatedAt)}
                </div>
              )}
            </div>
          </motion.div>
        ))}
      </div>

      {/* 데이터 없음 상태 */}
      {filteredTests.length === 0 && !loading && (
        <div className="bg-white rounded-xl shadow-sm p-12 text-center">
          <FileText className="w-16 h-16 mx-auto text-gray-300 mb-4" />
          <p className="text-lg text-gray-500 mb-2">
            {searchQuery || selectedStage !== 'ALL' ? '검색 조건에 맞는 시험이 없습니다' : '등록된 시험이 없습니다'}
          </p>
          <p className="text-sm text-gray-400">
            {searchQuery || selectedStage !== 'ALL' ? '다른 조건으로 검색해보세요' : '새 시험을 의뢰해보세요'}
          </p>
        </div>
      )}

      {/* 페이지네이션 */}
      {totalPages > 1 && (
        <div className="bg-white rounded-xl shadow-sm p-4">
          <div className="flex items-center justify-between">
            <div className="text-sm text-gray-500">
              {page * size + 1}-{Math.min((page + 1) * size, totalElements)}개 / 총 {totalElements}개
            </div>

            <div className="flex items-center space-x-2">
              <button
                onClick={() => setPage(Math.max(0, page - 1))}
                disabled={page === 0}
                className="px-3 py-2 text-sm border border-gray-200 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                이전
              </button>

              <div className="flex items-center space-x-1">
                {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
                  const pageNum = Math.max(0, Math.min(totalPages - 5, page - 2)) + i;
                  return (
                    <button
                      key={pageNum}
                      onClick={() => setPage(pageNum)}
                      className={`px-3 py-2 text-sm rounded-lg ${
                        page === pageNum
                          ? 'bg-indigo-600 text-white'
                          : 'text-gray-600 hover:bg-gray-100'
                      }`}
                    >
                      {pageNum + 1}
                    </button>
                  );
                })}
              </div>

              <button
                onClick={() => setPage(Math.min(totalPages - 1, page + 1))}
                disabled={page === totalPages - 1}
                className="px-3 py-2 text-sm border border-gray-200 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                다음
              </button>
            </div>
          </div>
        </div>
      )}
    </motion.div>
  );
};

export default Tests;