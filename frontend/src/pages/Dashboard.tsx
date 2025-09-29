import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { Link } from 'react-router-dom';
import { 
  FileText, Package, Users, Clock, CheckCircle, AlertTriangle,
  TrendingUp, Activity, Calendar, BarChart3, ArrowRight,
  RefreshCw, Plus, Bell, Beaker, FlaskConical
} from 'lucide-react';
import { useAuth } from '@/contexts/AuthContext';
import { Test, Product, TestStage } from '@/types';
import { useDashboardData } from '@/hooks/useApi';

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

const Dashboard: React.FC = () => {
  const { user } = useAuth();
  
  // API 호출
  const { data: dashboardData, isLoading, error, refetch } = useDashboardData();
  
  const stats = dashboardData?.stats || {
    totalTests: 0,
    pendingTests: 0,
    completedTests: 0,
    myTests: 0,
    totalProducts: 0,
    pendingApprovals: 0
  };
  
  const recentTests = dashboardData?.recentTests || [];
  const stageCounts = dashboardData?.stageCounts || {
    REQUEST: 0,
    RECEIPT: 0,
    RECEIPT_APPROVAL: 0,
    RESULT_INPUT: 0,
    COMPLETED: 0
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('ko-KR', {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const getStageIcon = (stage: TestStage) => {
    switch (stage) {
      case 'REQUEST': return <FileText className="w-4 h-4" />;
      case 'RECEIPT': return <Package className="w-4 h-4" />;
      case 'RECEIPT_APPROVAL': return <CheckCircle className="w-4 h-4" />;
      case 'RESULT_INPUT': return <Beaker className="w-4 h-4" />;
      case 'COMPLETED': return <CheckCircle className="w-4 h-4" />;
    }
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center py-12">
        <RefreshCw className="w-8 h-8 animate-spin text-indigo-600" />
        <span className="ml-3 text-lg text-gray-600">대시보드 로딩중...</span>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex items-center justify-center py-12">
        <div className="text-center">
          <AlertTriangle className="w-16 h-16 mx-auto text-red-500 mb-4" />
          <p className="text-lg text-gray-900 mb-2">대시보드 데이터를 불러오는 중 오류가 발생했습니다.</p>
          <button
            onClick={() => refetch()}
            className="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700"
          >
            다시 시도
          </button>
        </div>
      </div>
    );
  }

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="space-y-6"
    >
      {/* 환영 메시지 */}
      <div className="bg-gradient-to-r from-indigo-600 to-purple-600 rounded-xl p-6 text-white">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold mb-2">안녕하세요, {user?.name}님! 👋</h1>
            <p className="text-indigo-100">오늘도 정확한 실험실 관리로 품질을 향상시켜보세요</p>
          </div>
          <div className="hidden md:block">
            <FlaskConical className="w-24 h-24 text-indigo-200" />
          </div>
        </div>
      </div>

      {/* 주요 통계 카드 */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <motion.div
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ delay: 0.1 }}
          className="bg-white rounded-xl shadow-sm p-6 border border-gray-100"
        >
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600 mb-1">전체 시험</p>
              <p className="text-3xl font-bold text-gray-900">{stats.totalTests}</p>
              <p className="text-sm text-green-600 mt-1">+12% 이번 달</p>
            </div>
            <div className="w-12 h-12 bg-blue-100 rounded-xl flex items-center justify-center">
              <FileText className="w-6 h-6 text-blue-600" />
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ delay: 0.2 }}
          className="bg-white rounded-xl shadow-sm p-6 border border-gray-100"
        >
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600 mb-1">처리 대기</p>
              <p className="text-3xl font-bold text-gray-900">{stats.pendingTests}</p>
              <p className="text-sm text-orange-600 mt-1">확인 필요</p>
            </div>
            <div className="w-12 h-12 bg-orange-100 rounded-xl flex items-center justify-center">
              <Clock className="w-6 h-6 text-orange-600" />
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ delay: 0.3 }}
          className="bg-white rounded-xl shadow-sm p-6 border border-gray-100"
        >
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600 mb-1">완료된 시험</p>
              <p className="text-3xl font-bold text-gray-900">{stats.completedTests}</p>
              <p className="text-sm text-green-600 mt-1">+8 어제</p>
            </div>
            <div className="w-12 h-12 bg-green-100 rounded-xl flex items-center justify-center">
              <CheckCircle className="w-6 h-6 text-green-600" />
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ delay: 0.4 }}
          className="bg-white rounded-xl shadow-sm p-6 border border-gray-100"
        >
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600 mb-1">등록 제품</p>
              <p className="text-3xl font-bold text-gray-900">{stats.totalProducts}</p>
              <p className="text-sm text-blue-600 mt-1">활성 상태</p>
            </div>
            <div className="w-12 h-12 bg-purple-100 rounded-xl flex items-center justify-center">
              <Package className="w-6 h-6 text-purple-600" />
            </div>
          </div>
        </motion.div>
      </div>

      {/* 시험 단계별 현황 */}
      <div className="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-xl font-bold text-gray-900">시험 단계별 현황</h2>
          <Link
            to="/tests"
            className="text-indigo-600 hover:text-indigo-700 flex items-center space-x-1 text-sm"
          >
            <span>전체 보기</span>
            <ArrowRight className="w-4 h-4" />
          </Link>
        </div>
        
        <div className="grid grid-cols-2 lg:grid-cols-5 gap-4">
          {Object.entries(stageLabels).map(([stage, label]) => {
            const count = stageCounts[stage as TestStage];
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

      {/* 최근 활동 및 빠른 액션 */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* 최근 시험 활동 */}
        <div className="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-xl font-bold text-gray-900">최근 시험 활동</h2>
            <Link
              to="/tests"
              className="text-indigo-600 hover:text-indigo-700 flex items-center space-x-1 text-sm"
            >
              <span>더 보기</span>
              <ArrowRight className="w-4 h-4" />
            </Link>
          </div>
          
          <div className="space-y-4">
            {recentTests.map((test) => (
              <motion.div
                key={test.id}
                whileHover={{ scale: 1.02 }}
                className="flex items-center space-x-4 p-3 bg-gray-50 rounded-lg cursor-pointer hover:bg-gray-100 transition-colors"
              >
                <div className={`p-2 rounded-lg ${stageBadgeColors[test.stage]}`}>
                  {getStageIcon(test.stage)}
                </div>
                <div className="flex-1">
                  <h4 className="font-medium text-gray-900">
                    #{test.id.toString().padStart(4, '0')} - {test.requestInfo?.purpose}
                  </h4>
                  <p className="text-sm text-gray-600">
                    {test.product?.name} • {formatDate(test.updatedAt)}
                  </p>
                </div>
                <span className={`px-2 py-1 rounded-full text-xs font-medium ${stageBadgeColors[test.stage]}`}>
                  {stageLabels[test.stage]}
                </span>
              </motion.div>
            ))}
          </div>
        </div>

        {/* 빠른 액션 */}
        <div className="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
          <h2 className="text-xl font-bold text-gray-900 mb-6">빠른 액션</h2>
          
          <div className="space-y-4">
            <Link
              to="/tests/new"
              className="flex items-center space-x-4 p-4 bg-indigo-50 rounded-lg hover:bg-indigo-100 transition-colors group"
            >
              <div className="w-12 h-12 bg-indigo-100 rounded-xl flex items-center justify-center group-hover:bg-indigo-200">
                <Plus className="w-6 h-6 text-indigo-600" />
              </div>
              <div>
                <h3 className="font-medium text-gray-900">새 시험 의뢰</h3>
                <p className="text-sm text-gray-600">새로운 시험을 의뢰합니다</p>
              </div>
            </Link>
            
            <Link
              to="/products"
              className="flex items-center space-x-4 p-4 bg-purple-50 rounded-lg hover:bg-purple-100 transition-colors group"
            >
              <div className="w-12 h-12 bg-purple-100 rounded-xl flex items-center justify-center group-hover:bg-purple-200">
                <Package className="w-6 h-6 text-purple-600" />
              </div>
              <div>
                <h3 className="font-medium text-gray-900">제품 관리</h3>
                <p className="text-sm text-gray-600">제품 정보를 관리합니다</p>
              </div>
            </Link>
            
            <Link
              to="/pending-approvals"
              className="flex items-center space-x-4 p-4 bg-orange-50 rounded-lg hover:bg-orange-100 transition-colors group"
            >
              <div className="w-12 h-12 bg-orange-100 rounded-xl flex items-center justify-center group-hover:bg-orange-200">
                <Clock className="w-6 h-6 text-orange-600" />
              </div>
              <div className="flex-1">
                <div className="flex items-center justify-between">
                  <h3 className="font-medium text-gray-900">승인 대기</h3>
                  {stats.pendingApprovals > 0 && (
                    <span className="bg-red-100 text-red-800 px-2 py-1 rounded-full text-xs font-medium">
                      {stats.pendingApprovals}
                    </span>
                  )}
                </div>
                <p className="text-sm text-gray-600">대기 중인 승인을 처리합니다</p>
              </div>
            </Link>
            
            <Link
              to="/statistics"
              className="flex items-center space-x-4 p-4 bg-green-50 rounded-lg hover:bg-green-100 transition-colors group"
            >
              <div className="w-12 h-12 bg-green-100 rounded-xl flex items-center justify-center group-hover:bg-green-200">
                <BarChart3 className="w-6 h-6 text-green-600" />
              </div>
              <div>
                <h3 className="font-medium text-gray-900">통계 및 분석</h3>
                <p className="text-sm text-gray-600">실험실 성과를 분석합니다</p>
              </div>
            </Link>
          </div>
        </div>
      </div>

      {/* 알림 영역 */}
      <div className="bg-gradient-to-r from-amber-50 to-orange-50 border border-amber-200 rounded-xl p-6">
        <div className="flex items-start space-x-3">
          <Bell className="w-6 h-6 text-amber-600 mt-1" />
          <div>
            <h3 className="font-semibold text-amber-900 mb-2">중요 알림</h3>
            <div className="space-y-1 text-sm text-amber-800">
              <p>• 정기 장비 점검이 내일(1/16) 예정되어 있습니다.</p>
              <p>• 신규 시약 입고가 완료되었습니다. 재고를 확인하세요.</p>
              <p>• 품질관리 회의가 금요일 오후 2시에 예정되어 있습니다.</p>
            </div>
          </div>
        </div>
      </div>
    </motion.div>
  );
};

export default Dashboard;