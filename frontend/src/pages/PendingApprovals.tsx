import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import {
  CheckCircle, XCircle, Clock, Search, Filter,
  User, Calendar, FileText, ChevronLeft, ChevronRight
} from 'lucide-react';
import { Approval, ApprovalStatus, PaginationParams } from '@/types';
import { apiService } from '@/services/api';
import toast from 'react-hot-toast';

const PendingApprovals: React.FC = () => {
  const [approvals, setApprovals] = useState<Approval[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState<ApprovalStatus | 'ALL'>('PENDING');
  const [processing, setProcessing] = useState<Set<number>>(new Set());
  const [pagination, setPagination] = useState<PaginationParams>({
    page: 1,
    limit: 10,
    total: 0,
    totalPages: 0
  });

  useEffect(() => {
    fetchApprovals();
  }, [pagination.page, statusFilter]);

  const fetchApprovals = async () => {
    setLoading(true);
    try {
      const params = {
        page: pagination.page,
        limit: pagination.limit,
        status: statusFilter === 'ALL' ? undefined : statusFilter,
        search: searchQuery || undefined
      };

      const response = await apiService.getPendingApprovals(params);
      console.log('👥 Pending Approvals API Response:', response);
      console.log('👥 First approval:', response[0]);
      console.log('👥 Request info:', response[0]?.request);
      console.log('👥 Requester Name:', response[0]?.request?.requesterName);
      console.log('👥 Request full object:', JSON.stringify(response[0]?.request, null, 2));

      // ApiResponse 구조: { success, message, data }
      // data가 List<ApprovalResponseDto>이므로 배열임
      const approvalsData = Array.isArray(response) ? response : (response?.data || []);
      console.log('👥 Final approvals data:', approvalsData);

      setApprovals(Array.isArray(approvalsData) ? approvalsData : []);
      setPagination(prev => ({
        ...prev,
        total: Array.isArray(approvalsData) ? approvalsData.length : 0,
        totalPages: Math.ceil((Array.isArray(approvalsData) ? approvalsData.length : 0) / prev.limit)
      }));
    } catch (error) {
      console.error('승인 목록 조회 실패:', error);
      toast.error('승인 목록을 불러오는데 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  const handleApproval = async (approvalId: number, signId: number, action: 'APPROVED' | 'REJECTED', comment?: string) => {
    if (processing.has(approvalId)) return;

    setProcessing(prev => new Set(prev).add(approvalId));

    try {
      await apiService.processApproval(approvalId, {
        signId: signId.toString(),
        status: action,
        comment: comment || ''
      });

      // 승인 목록 새로고침
      await fetchApprovals();

      toast.success(`성공적으로 ${action === 'APPROVED' ? '승인' : '거부'}되었습니다.`);
    } catch (error) {
      console.error('승인 처리 실패:', error);
      toast.error('승인 처리 중 오류가 발생했습니다.');
    } finally {
      setProcessing(prev => {
        const newSet = new Set(prev);
        newSet.delete(approvalId);
        return newSet;
      });
    }
  };

  const handleSearch = () => {
    setPagination(prev => ({ ...prev, page: 1 }));
    fetchApprovals();
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

  const getStatusColor = (status: ApprovalStatus) => {
    switch (status) {
      case 'PENDING': return 'text-yellow-600 bg-yellow-100';
      case 'APPROVED': return 'text-green-600 bg-green-100';
      case 'REJECTED': return 'text-red-600 bg-red-100';
      default: return 'text-gray-600 bg-gray-100';
    }
  };

  const getStatusIcon = (status: ApprovalStatus) => {
    switch (status) {
      case 'PENDING': return <Clock className="w-4 h-4" />;
      case 'APPROVED': return <CheckCircle className="w-4 h-4" />;
      case 'REJECTED': return <XCircle className="w-4 h-4" />;
      default: return <Clock className="w-4 h-4" />;
    }
  };

  if (loading && approvals.length === 0) {
    return (
      <div className="flex items-center justify-center py-12">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
        <span className="ml-3 text-lg text-gray-600">승인 목록 로딩중...</span>
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
            <CheckCircle className="w-8 h-8 text-indigo-600" />
            <div>
              <h1 className="text-3xl font-bold text-gray-900">승인 관리</h1>
              <p className="text-gray-600 mt-1">대기 중인 승인 요청을 관리합니다</p>
            </div>
          </div>
        </div>

        {/* 필터 및 검색 영역 */}
        <div className="flex items-center justify-between space-x-4">
          <div className="flex items-center space-x-4">
            <div className="flex items-center space-x-2">
              <Filter className="w-5 h-5 text-gray-400" />
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value as ApprovalStatus | 'ALL')}
                className="px-3 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              >
                <option value="ALL">전체</option>
                <option value="PENDING">대기중</option>
                <option value="APPROVED">승인됨</option>
                <option value="REJECTED">거부됨</option>
              </select>
            </div>

            <div className="flex items-center space-x-2">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type="text"
                  placeholder="검색어를 입력하세요..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
                  className="pl-10 pr-4 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                />
              </div>
              <button
                onClick={handleSearch}
                className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors"
              >
                검색
              </button>
            </div>
          </div>

          <div className="text-sm text-gray-500">
            총 {pagination.total}개 항목
          </div>
        </div>
      </div>

      {/* 승인 목록 */}
      <div className="space-y-4">
        {Array.isArray(approvals) && approvals.map((approval) => (
          <motion.div
            key={approval.id}
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            className="bg-white rounded-xl shadow-sm border border-gray-100 p-6"
          >
            <div className="flex items-start justify-between">
              <div className="flex-1">
                <div className="flex items-center space-x-3 mb-3">
                  <span className={`inline-flex items-center space-x-1 px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(approval.status)}`}>
                    {getStatusIcon(approval.status)}
                    <span>{approval.status === 'PENDING' ? '대기중' : approval.status === 'APPROVED' ? '승인됨' : '거부됨'}</span>
                  </span>
                  <span className="text-sm text-gray-500">#{approval.id}</span>
                </div>

                <h3 className="text-lg font-semibold text-gray-900 mb-2">
                  승인 요청 #{approval.id}
                </h3>

                <div className="grid grid-cols-2 gap-4 mb-4">
                  <div className="flex items-center space-x-2 text-sm text-gray-600">
                    <User className="w-4 h-4" />
                    <span>요청자: {approval.request?.requesterName || 'N/A'}</span>
                  </div>
                  <div className="flex items-center space-x-2 text-sm text-gray-600">
                    <Calendar className="w-4 h-4" />
                    <span>요청일: {approval.request?.createdAt ? formatDate(approval.request.createdAt) : 'N/A'}</span>
                  </div>
                </div>

                {approval.request?.comment && (
                  <div className="flex items-start space-x-2 text-sm text-gray-600 mb-4">
                    <FileText className="w-4 h-4 mt-0.5" />
                    <p>{approval.request.comment}</p>
                  </div>
                )}

                {approval.comment && (
                  <div className="bg-gray-50 rounded-lg p-3 mt-4">
                    <p className="text-sm text-gray-700">
                      <span className="font-medium">처리 의견: </span>
                      {approval.comment}
                    </p>
                    {approval.processedAt && (
                      <p className="text-xs text-gray-500 mt-1">
                        처리일: {formatDate(approval.processedAt)}
                      </p>
                    )}
                  </div>
                )}
              </div>

              {approval.status === 'PENDING' && (() => {
                // 현재 사용자 정보 가져오기
                const userStr = localStorage.getItem('user');
                const currentUser = userStr ? JSON.parse(userStr) : null;

                // 현재 사용자가 승인해야 하는 sign 찾기
                const mySign = approval.signs?.find(sign =>
                  sign.approverId === currentUser?.id && sign.status === 'PENDING'
                );

                if (!mySign) {
                  return null; // 현재 사용자가 승인할 sign이 없으면 버튼 표시 안함
                }

                return (
                  <div className="flex items-center space-x-2 ml-4">
                    <button
                      onClick={() => handleApproval(approval.id, mySign.id, 'APPROVED')}
                      disabled={processing.has(approval.id)}
                      className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors disabled:opacity-50 flex items-center space-x-1"
                    >
                      <CheckCircle className="w-4 h-4" />
                      <span>승인</span>
                    </button>
                    <button
                      onClick={() => {
                        const comment = window.prompt('거부 사유를 입력하세요:');
                        if (comment !== null) {
                          handleApproval(approval.id, mySign.id, 'REJECTED', comment);
                        }
                      }}
                      disabled={processing.has(approval.id)}
                      className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors disabled:opacity-50 flex items-center space-x-1"
                    >
                      <XCircle className="w-4 h-4" />
                      <span>거부</span>
                    </button>
                  </div>
                );
              })()}
            </div>
          </motion.div>
        ))}
      </div>

      {Array.isArray(approvals) && approvals.length === 0 && !loading && (
        <div className="bg-white rounded-xl shadow-sm p-12 text-center">
          <CheckCircle className="w-16 h-16 mx-auto text-gray-300 mb-4" />
          <p className="text-lg text-gray-500 mb-2">
            {searchQuery || statusFilter !== 'ALL' ? '검색 결과가 없습니다' : '승인 요청이 없습니다'}
          </p>
          <p className="text-sm text-gray-400">
            {searchQuery || statusFilter !== 'ALL' ? '다른 조건으로 검색해보세요' : '새로운 승인 요청을 기다리고 있습니다'}
          </p>
        </div>
      )}

      {/* 페이지네이션 */}
      {pagination.totalPages > 1 && (
        <div className="bg-white rounded-xl shadow-sm p-4">
          <div className="flex items-center justify-between">
            <div className="text-sm text-gray-500">
              {pagination.total}개 중 {((pagination.page - 1) * pagination.limit) + 1}-{Math.min(pagination.page * pagination.limit, pagination.total)}개 표시
            </div>

            <div className="flex items-center space-x-2">
              <button
                onClick={() => setPagination(prev => ({ ...prev, page: Math.max(1, prev.page - 1) }))}
                disabled={pagination.page === 1}
                className="p-2 text-gray-500 hover:text-gray-700 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <ChevronLeft className="w-5 h-5" />
              </button>

              <div className="flex items-center space-x-1">
                {Array.from({ length: Math.min(5, pagination.totalPages) }, (_, i) => {
                  const page = i + Math.max(1, pagination.page - 2);
                  if (page > pagination.totalPages) return null;

                  return (
                    <button
                      key={page}
                      onClick={() => setPagination(prev => ({ ...prev, page }))}
                      className={`px-3 py-1 rounded text-sm ${
                        page === pagination.page
                          ? 'bg-indigo-600 text-white'
                          : 'text-gray-500 hover:text-gray-700'
                      }`}
                    >
                      {page}
                    </button>
                  );
                })}
              </div>

              <button
                onClick={() => setPagination(prev => ({ ...prev, page: Math.min(prev.totalPages, prev.page + 1) }))}
                disabled={pagination.page === pagination.totalPages}
                className="p-2 text-gray-500 hover:text-gray-700 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <ChevronRight className="w-5 h-5" />
              </button>
            </div>
          </div>
        </div>
      )}
    </motion.div>
  );
};

export default PendingApprovals;
