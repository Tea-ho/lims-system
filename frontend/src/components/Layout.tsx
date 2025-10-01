import React, { useState, useEffect, useRef } from 'react';
import { NavLink, useLocation, useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import {
  Home, FileText, User, Clock, BarChart3, Users, Settings, Package,
  Bell, Search, Plus, LogOut, Activity, Menu, X, Shield, CheckCircle2, AlertTriangle, Info
} from 'lucide-react';
import { User as UserType } from '@/types';
import { useAuth } from '@/contexts/AuthContext';
import Button from '@/components/Button';
import NotificationDropdown from '@/components/NotificationDropdown';

interface LayoutProps {
  children: React.ReactNode;
  user: UserType;
}

const Layout: React.FC<LayoutProps> = ({ children, user }) => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const location = useLocation();
  const navigate = useNavigate();
  const { logout } = useAuth();

  const handleLogout = () => {
    if (window.confirm('로그아웃 하시겠습니까?')) {
      logout();
    }
  };

  const handleSearch = () => {
    if (searchQuery.trim()) {
      navigate(`/tests?search=${encodeURIComponent(searchQuery)}`);
    }
  };

  const handleSearchKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  const navigation = [
    { name: '대시보드', href: '/dashboard', icon: Home },
    { name: '시험 관리', href: '/tests', icon: FileText },
    { name: '제품 관리', href: '/products', icon: Package },
    { name: '내 시험 의뢰', href: '/my-tests', icon: User },
    { name: '승인 대기', href: '/pending-approvals', icon: Clock, badge: 3 },
    { name: '통계 및 분석', href: '/statistics', icon: BarChart3 },
  ];

  const adminNavigation = [
    { name: '사용자 관리', href: '/users', icon: Users },
    { name: '시스템 설정', href: '/settings', icon: Settings },
    { name: '감사 로그', href: '/audit-logs', icon: Shield },
  ];

  const getPageTitle = (pathname: string) => {
    const page = navigation.find(nav => nav.href === pathname) || 
                 adminNavigation.find(nav => nav.href === pathname);
    return page?.name || '승인 시스템';
  };

  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* 모바일 사이드바 오버레이 */}
      {sidebarOpen && (
        <div 
          className="fixed inset-0 z-40 lg:hidden bg-black bg-opacity-50"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      {/* 사이드바 */}
      <motion.nav
        initial={false}
        animate={{
          x: sidebarOpen ? 0 : '-100%',
          transition: { duration: 0.3, ease: 'easeInOut' }
        }}
        className={`
          fixed inset-y-0 left-0 z-50 w-64 bg-white shadow-xl border-r border-gray-100 
          transform transition-transform duration-300 ease-in-out lg:translate-x-0 lg:static lg:inset-0
        `}
      >
        {/* 로고 */}
        <div className="flex items-center justify-between p-6 border-b border-gray-100">
          <div className="flex items-center space-x-3">
            <div className="w-10 h-10 bg-gradient-to-r from-blue-600 to-purple-600 rounded-xl flex items-center justify-center">
              <Activity className="w-6 h-6 text-white" />
            </div>
            <div>
              <h1 className="text-xl font-bold text-gray-900">LIMS</h1>
              <p className="text-sm text-gray-500">승인 시스템</p>
            </div>
          </div>
          <button
            onClick={() => setSidebarOpen(false)}
            className="lg:hidden p-2 rounded-lg hover:bg-gray-100"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* 네비게이션 */}
        <div className="flex-1 py-6 overflow-y-auto">
          <div className="px-4 space-y-2">
            {navigation.map((item) => {
              const isActive = location.pathname === item.href;
              return (
                <NavLink
                  key={item.name}
                  to={item.href}
                  className={`
                    flex items-center justify-between px-4 py-3 rounded-xl font-medium transition-all duration-200
                    ${isActive 
                      ? 'text-blue-600 bg-blue-50 border border-blue-100' 
                      : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
                    }
                  `}
                  onClick={() => setSidebarOpen(false)}
                >
                  <div className="flex items-center space-x-3">
                    <item.icon className="w-5 h-5" />
                    <span>{item.name}</span>
                  </div>
                  {item.badge && (
                    <span className="bg-red-500 text-white text-xs px-2 py-1 rounded-full">
                      {item.badge}
                    </span>
                  )}
                </NavLink>
              );
            })}
          </div>

          <div className="mt-8 px-4">
            <div className="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2">
              관리
            </div>
            <div className="space-y-2">
              {adminNavigation.map((item) => {
                const isActive = location.pathname === item.href;
                return (
                  <NavLink
                    key={item.name}
                    to={item.href}
                    className={`
                      flex items-center space-x-3 px-4 py-3 rounded-xl font-medium transition-all duration-200
                      ${isActive 
                        ? 'text-blue-600 bg-blue-50 border border-blue-100' 
                        : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
                      }
                    `}
                    onClick={() => setSidebarOpen(false)}
                  >
                    <item.icon className="w-5 h-5" />
                    <span>{item.name}</span>
                  </NavLink>
                );
              })}
            </div>
          </div>
        </div>

        {/* 사용자 정보 */}
        <div className="p-4 border-t border-gray-100">
          <div 
            onClick={handleLogout}
            className="flex items-center space-x-3 p-2 rounded-xl hover:bg-gray-50 cursor-pointer group transition-colors"
          >
            <div className="w-10 h-10 bg-gradient-to-r from-blue-500 to-purple-600 rounded-full flex items-center justify-center">
              <span className="text-white font-semibold">
                {user?.name?.charAt(0) || 'U'}
              </span>
            </div>
            <div className="flex-1">
              <p className="font-medium text-gray-900">{user?.name || 'Unknown User'}</p>
              <p className="text-sm text-gray-500">{user?.department || 'No Department'}</p>
            </div>
            <LogOut className="w-4 h-4 text-gray-400 group-hover:text-red-500 transition-colors" />
          </div>
        </div>
      </motion.nav>

      {/* 메인 콘텐츠 */}
      <div className="flex-1 flex flex-col lg:ml-0">
        {/* 헤더 */}
        <header className="bg-white shadow-sm border-b border-gray-100 px-4 lg:px-8 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-4">
              <button
                onClick={() => setSidebarOpen(true)}
                className="lg:hidden p-2 rounded-lg hover:bg-gray-100"
              >
                <Menu className="w-5 h-5" />
              </button>
              
              <div>
                <h2 className="text-2xl font-bold text-gray-900">
                  {getPageTitle(location.pathname)}
                </h2>
                <p className="text-gray-600 mt-1">
                  Laboratory Information Management System
                </p>
              </div>
            </div>

            <div className="flex items-center space-x-4">
              {/* 검색 */}
              <div className="hidden md:block relative">
                <input
                  type="text"
                  placeholder="시험 검색..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  onKeyPress={handleSearchKeyPress}
                  className="w-80 pl-10 pr-12 py-2.5 border border-gray-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition-all"
                />
                <Search className="w-5 h-5 text-gray-400 absolute left-3 top-3" />
                <button
                  onClick={handleSearch}
                  className="absolute right-2 top-2 p-1 text-gray-400 hover:text-blue-500 transition-colors"
                  title="검색"
                >
                  <Search className="w-4 h-4" />
                </button>
              </div>

              {/* 알림 */}
              <NotificationDropdown />

              {/* 새 시험 의뢰 버튼 */}
              <Button
                variant="primary"
                icon={Plus}
                onClick={() => {
                  navigate('/tests/new');
                }}
                className="hidden sm:flex"
              >
                새 시험 의뢰
              </Button>

              {/* 대시보드 바로가기 버튼 */}
              <button
                onClick={() => navigate('/dashboard')}
                className="p-2.5 text-gray-600 hover:text-blue-600 hover:bg-blue-50 rounded-xl transition-all"
                title="대시보드로 이동"
              >
                <Home className="w-5 h-5" />
              </button>

              {/* 로그아웃 버튼 */}
              <button
                onClick={handleLogout}
                className="p-2.5 text-gray-600 hover:text-red-600 hover:bg-red-50 rounded-xl transition-all"
                title="로그아웃"
              >
                <LogOut className="w-5 h-5" />
              </button>
            </div>
          </div>
        </header>

        {/* 메인 콘텐츠 */}
        <main className="flex-1 p-4 lg:p-8">
          {children}
        </main>
      </div>
    </div>
  );
};

export default Layout;
