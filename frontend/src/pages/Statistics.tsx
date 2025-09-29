import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { 
  BarChart3, TrendingUp, Calendar, Filter, Download,
  FileText, Package, Clock, CheckCircle, Users,
  Activity, Target, Zap, Beaker
} from 'lucide-react';

const Statistics: React.FC = () => {
  const [selectedPeriod, setSelectedPeriod] = useState('month');

  // Mock 데이터 - 실제로는 API에서 가져옴
  const overallStats = {
    totalTests: 1247,
    completedTests: 956,
    avgProcessingTime: 2.3,
    successRate: 96.8,
    activeProducts: 45,
    totalUsers: 23
  };

  const monthlyTrends = [
    { month: '8월', tests: 89, completed: 85, pending: 4 },
    { month: '9월', tests: 102, completed: 96, pending: 6 },
    { month: '10월', tests: 95, completed: 88, pending: 7 },
    { month: '11월', tests: 118, completed: 112, pending: 6 },
    { month: '12월', tests: 134, completed: 128, pending: 6 },
    { month: '1월', tests: 156, completed: 142, pending: 14 }
  ];

  const stageDistribution = [
    { stage: '의뢰', count: 15, percentage: 12.1 },
    { stage: '접수', count: 8, percentage: 6.5 },
    { stage: '접수결재', count: 5, percentage: 4.0 },
    { stage: '결과입력', count: 12, percentage: 9.7 },
    { stage: '완료', count: 84, percentage: 67.7 }
  ];

  const topProducts = [
    { name: 'ABC-123 화합물', tests: 45, success: 98.2 },
    { name: 'XYZ-456 원료', tests: 38, success: 95.1 },
    { name: 'DEF-789 완제품', tests: 32, success: 97.8 },
    { name: 'GHI-101 중간체', tests: 28, success: 94.3 },
    { name: 'JKL-202 첨가제', tests: 25, success: 96.5 }
  ];

  const performanceMetrics = [
    { metric: '평균 처리시간', value: '2.3일', change: '-12%', trend: 'down' },
    { metric: '성공률', value: '96.8%', change: '+2.1%', trend: 'up' },
    { metric: '재처리율', value: '3.2%', change: '-0.8%', trend: 'down' },
    { metric: '사용자 만족도', value: '4.6/5', change: '+0.3', trend: 'up' }
  ];

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
            <BarChart3 className="w-8 h-8 text-indigo-600" />
            <div>
              <h1 className="text-3xl font-bold text-gray-900">통계 및 분석</h1>
              <p className="text-gray-600 mt-1">LIMS 시스템 성과를 분석합니다</p>
            </div>
          </div>
          
          <div className="flex items-center space-x-3">
            <select
              value={selectedPeriod}
              onChange={(e) => setSelectedPeriod(e.target.value)}
              className="px-3 py-2 border border-gray-300 rounded-lg"
            >
              <option value="week">주간</option>
              <option value="month">월간</option>
              <option value="quarter">분기</option>
              <option value="year">연간</option>
            </select>
            
            <button className="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 flex items-center space-x-2">
              <Download className="w-5 h-5" />
              <span>보고서 다운로드</span>
            </button>
          </div>
        </div>
      </div>

      {/* 주요 지표 카드 */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
          className="bg-white rounded-xl shadow-sm p-6 border border-gray-100"
        >
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600 mb-1">전체 시험</p>
              <p className="text-3xl font-bold text-gray-900">{overallStats.totalTests.toLocaleString()}</p>
              <p className="text-sm text-blue-600 mt-1">누적 총계</p>
            </div>
            <div className="w-12 h-12 bg-blue-100 rounded-xl flex items-center justify-center">
              <FileText className="w-6 h-6 text-blue-600" />
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
          className="bg-white rounded-xl shadow-sm p-6 border border-gray-100"
        >
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600 mb-1">완료된 시험</p>
              <p className="text-3xl font-bold text-gray-900">{overallStats.completedTests.toLocaleString()}</p>
              <p className="text-sm text-green-600 mt-1">{((overallStats.completedTests / overallStats.totalTests) * 100).toFixed(1)}% 완료율</p>
            </div>
            <div className="w-12 h-12 bg-green-100 rounded-xl flex items-center justify-center">
              <CheckCircle className="w-6 h-6 text-green-600" />
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
          className="bg-white rounded-xl shadow-sm p-6 border border-gray-100"
        >
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600 mb-1">평균 처리시간</p>
              <p className="text-3xl font-bold text-gray-900">{overallStats.avgProcessingTime}일</p>
              <p className="text-sm text-orange-600 mt-1">목표: 3일 이내</p>
            </div>
            <div className="w-12 h-12 bg-orange-100 rounded-xl flex items-center justify-center">
              <Clock className="w-6 h-6 text-orange-600" />
            </div>
          </div>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.4 }}
          className="bg-white rounded-xl shadow-sm p-6 border border-gray-100"
        >
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600 mb-1">성공률</p>
              <p className="text-3xl font-bold text-gray-900">{overallStats.successRate}%</p>
              <p className="text-sm text-green-600 mt-1">목표 달성</p>
            </div>
            <div className="w-12 h-12 bg-purple-100 rounded-xl flex items-center justify-center">
              <Target className="w-6 h-6 text-purple-600" />
            </div>
          </div>
        </motion.div>
      </div>

      {/* 월별 트렌드 */}
      <div className="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-xl font-bold text-gray-900">월별 시험 현황</h2>
        </div>
        
        <div className="space-y-4">
          {monthlyTrends.map((trend) => (
            <div key={trend.month} className="flex items-center space-x-4">
              <div className="w-12 text-sm font-medium text-gray-600">
                {trend.month}
              </div>
              <div className="flex-1">
                <div className="flex items-center space-x-4 mb-2">
                  <span className="text-sm text-gray-900 w-16">시험: {trend.tests}</span>
                  <span className="text-sm text-green-600 w-16">완료: {trend.completed}</span>
                  <span className="text-sm text-orange-600 w-16">대기: {trend.pending}</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div 
                    className="bg-green-500 h-2 rounded-full"
                    style={{ width: `${(trend.completed / trend.tests) * 100}%` }}
                  />
                </div>
              </div>
              <div className="text-sm font-medium text-gray-900 w-12">
                {((trend.completed / trend.tests) * 100).toFixed(0)}%
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* 추가 분석 섹션 */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* 시험 단계별 분포 */}
        <div className="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
          <h2 className="text-xl font-bold text-gray-900 mb-6">현재 시험 단계별 분포</h2>
          <div className="space-y-4">
            {stageDistribution.map((stage) => (
              <div key={stage.stage} className="flex items-center justify-between">
                <div className="flex items-center space-x-3">
                  <div className="w-4 h-4 bg-indigo-500 rounded-full"></div>
                  <span className="text-sm font-medium text-gray-900">{stage.stage}</span>
                </div>
                <div className="flex items-center space-x-2">
                  <span className="text-sm text-gray-600">{stage.count}건</span>
                  <span className="text-sm font-medium text-gray-900">{stage.percentage}%</span>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* 주요 제품별 성과 */}
        <div className="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
          <h2 className="text-xl font-bold text-gray-900 mb-6">주요 제품별 시험 성과</h2>
          <div className="space-y-4">
            {topProducts.map((product, index) => (
              <div key={product.name} className="flex items-center justify-between">
                <div className="flex items-center space-x-3">
                  <div className="w-8 h-8 bg-purple-100 rounded-lg flex items-center justify-center">
                    <span className="text-xs font-bold text-purple-600">{index + 1}</span>
                  </div>
                  <div>
                    <p className="text-sm font-medium text-gray-900">{product.name}</p>
                    <p className="text-xs text-gray-500">{product.tests}건 시험</p>
                  </div>
                </div>
                <div className="text-right">
                  <p className="text-sm font-medium text-green-600">{product.success}%</p>
                  <p className="text-xs text-gray-500">성공률</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* 성과 지표 */}
      <div className="bg-white rounded-xl shadow-sm p-6 border border-gray-100">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-xl font-bold text-gray-900">핵심 성과 지표 (KPI)</h2>
          <button className="text-indigo-600 hover:text-indigo-700 flex items-center space-x-1 text-sm">
            <Download className="w-4 h-4" />
            <span>보고서 다운로드</span>
          </button>
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {performanceMetrics.map((metric) => (
            <div key={metric.metric} className="text-center">
              <div className={`inline-flex items-center space-x-1 text-sm font-medium ${
                metric.trend === 'up' ? 'text-green-600' : 
                metric.trend === 'down' ? 'text-red-600' : 'text-gray-600'
              }`}>
                {metric.trend === 'up' ? (
                  <TrendingUp className="w-4 h-4" />
                ) : (
                  <TrendingUp className="w-4 h-4 transform rotate-180" />
                )}
                <span>{metric.change}</span>
              </div>
              <p className="text-2xl font-bold text-gray-900 mt-1">{metric.value}</p>
              <p className="text-sm text-gray-600 mt-1">{metric.metric}</p>
            </div>
          ))}
        </div>
      </div>

      {/* 추가 인사이트 */}
      <div className="bg-gradient-to-r from-blue-50 to-indigo-50 border border-blue-200 rounded-xl p-6">
        <div className="flex items-start space-x-3">
          <Activity className="w-6 h-6 text-blue-600 mt-1" />
          <div>
            <h3 className="font-semibold text-blue-900 mb-2">인사이트 및 권장사항</h3>
            <div className="space-y-2 text-sm text-blue-800">
              <p>• 이번 달 시험 완료율이 91%로 목표치를 달성했습니다.</p>
              <p>• 평균 처리시간이 12% 단축되어 효율성이 개선되었습니다.</p>
              <p>• ABC-123 화합물의 성공률이 98%로 가장 높습니다.</p>
              <p>• 접수결재 단계에서 약간의 지연이 발생하고 있으니 검토가 필요합니다.</p>
            </div>
          </div>
        </div>
      </div>
    </motion.div>
  );
};

export default Statistics;