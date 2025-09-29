import React, { Suspense, useEffect } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { AnimatePresence } from 'framer-motion';
import Layout from '@/components/Layout';
import LoadingSpinner from '@/components/LoadingSpinner';
import ErrorBoundary from '@/components/ErrorBoundary';
import { useAuth } from '@/contexts/AuthContext';

// 페이지 컴포넌트들 (lazy loading)
const Dashboard = React.lazy(() => import('@/pages/Dashboard'));

// Test Management
const Tests = React.lazy(() => import('@/pages/Tests'));
const TestDetail = React.lazy(() => import('@/pages/TestDetail'));
const MyTests = React.lazy(() => import('@/pages/MyTests'));
const CreateTest = React.lazy(() => import('@/pages/CreateTest'));

// Product Management
const Products = React.lazy(() => import('@/pages/Products'));
const ProductDetail = React.lazy(() => import('@/pages/ProductDetail'));

// Approval System
const Approvals = React.lazy(() => import('@/pages/Approvals'));
const MyApprovals = React.lazy(() => import('@/pages/MyApprovals'));
const PendingApprovals = React.lazy(() => import('@/pages/PendingApprovals'));
const ApprovalDetail = React.lazy(() => import('@/pages/ApprovalDetail'));

// Analytics & Reports
const Statistics = React.lazy(() => import('@/pages/Statistics'));
const Reports = React.lazy(() => import('@/pages/Reports'));

// System Management
const Users = React.lazy(() => import('@/pages/Users'));
const Settings = React.lazy(() => import('@/pages/Settings'));
const AuditLogs = React.lazy(() => import('@/pages/AuditLogs'));

const Login = React.lazy(() => import('@/pages/Login'));

const App: React.FC = () => {
  const { user, isAuthenticated, isLoading } = useAuth();

  useEffect(() => {
    // 앱 시작 시 초기화 로직
    document.title = 'LIMS 시스템';
  }, []);

  // 로딩 중
  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  // 인증되지 않은 사용자
  if (!isAuthenticated) {
    return (
      <ErrorBoundary>
        <Suspense fallback={<LoadingSpinner />}>
          <Login />
        </Suspense>
      </ErrorBoundary>
    );
  }

  // 인증된 사용자
  return (
    <ErrorBoundary>
      <Layout user={user}>
        <AnimatePresence mode="wait">
          <Suspense fallback={<LoadingSpinner />}>
            <Routes>
              <Route path="/" element={<Navigate to="/dashboard" replace />} />
              
              {/* 대시보드 */}
              <Route path="/dashboard" element={<Dashboard />} />
              
              {/* 시험 관리 */}
              <Route path="/tests" element={<Tests />} />
              <Route path="/tests/new" element={<CreateTest />} />
              <Route path="/tests/:id" element={<TestDetail />} />
              <Route path="/my-tests" element={<MyTests />} />
              
              {/* 제품 관리 */}
              <Route path="/products" element={<Products />} />
              <Route path="/products/:id" element={<ProductDetail />} />
              
              {/* 승인 시스템 */}
              <Route path="/approvals" element={<Approvals />} />
              <Route path="/approvals/:id" element={<ApprovalDetail />} />
              <Route path="/my-approvals" element={<MyApprovals />} />
              <Route path="/pending-approvals" element={<PendingApprovals />} />
              
              {/* 통계 및 분석 */}
              <Route path="/statistics" element={<Statistics />} />
              <Route path="/reports" element={<Reports />} />
              
              {/* 시스템 관리 */}
              <Route path="/users" element={<Users />} />
              <Route path="/settings" element={<Settings />} />
              <Route path="/audit-logs" element={<AuditLogs />} />
              
              <Route path="*" element={<Navigate to="/dashboard" replace />} />
            </Routes>
          </Suspense>
        </AnimatePresence>
      </Layout>
    </ErrorBoundary>
  );
};

export default App;
