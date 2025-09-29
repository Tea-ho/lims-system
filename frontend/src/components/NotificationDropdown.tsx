import React, { useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import {
  Bell, CheckCircle2, AlertTriangle, Info, Activity, Clock
} from 'lucide-react';
import { Notification } from '@/types';
import { apiService } from '@/services/api';
import { useNavigate } from 'react-router-dom';

interface NotificationDropdownProps {
  className?: string;
}

const NotificationDropdown: React.FC<NotificationDropdownProps> = ({ className }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [loading, setLoading] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);
  const navigate = useNavigate();

  // Mock 데이터
  const getMockNotifications = (): Notification[] => [
    {
      id: 1,
      type: 'APPROVAL_REQUEST',
      title: '승인 요청',
      message: '새로운 시험 승인 요청이 있습니다.',
      isRead: false,
      createdAt: new Date(Date.now() - 30 * 60 * 1000).toISOString(),
      targetId: 123,
      targetType: 'approval',
      actionUrl: '/pending-approvals'
    },
    {
      id: 2,
      type: 'TEST_STATUS_CHANGE',
      title: '시험 상태 변경',
      message: 'TEST-2024-001 시험이 결과 입력 단계로 이동했습니다.',
      isRead: false,
      createdAt: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString(),
      targetId: 456,
      targetType: 'test',
      actionUrl: '/tests/456'
    },
    {
      id: 3,
      type: 'SYSTEM_ALERT',
      title: '시스템 알림',
      message: '시스템 점검이 오늘 밤 23:00에 예정되어 있습니다.',
      isRead: true,
      createdAt: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString()
    }
  ];

  const fetchNotifications = async () => {
    setLoading(true);
    try {
      const response = await apiService.getNotifications({ page: 1, limit: 10 });
      setNotifications(response.content || []);

      const count = await apiService.getUnreadNotificationsCount();
      setUnreadCount(count);
    } catch (error) {
      console.error('알림 조회 실패:', error);
      // 목업 데이터로 대체
      const mockNotifications = getMockNotifications();
      setNotifications(mockNotifications);
      setUnreadCount(mockNotifications.filter(n => !n.isRead).length);
    } finally {
      setLoading(false);
    }
  };

  const handleNotificationClick = async (notification: Notification) => {
    if (!notification.isRead) {
      try {
        await apiService.markNotificationAsRead(notification.id);
        setNotifications(prev =>
          prev.map(n => n.id === notification.id ? { ...n, isRead: true } : n)
        );
        setUnreadCount(prev => Math.max(0, prev - 1));
      } catch (error) {
        console.error('알림 읽음 처리 실패:', error);
        // 목업에서는 바로 처리
        setNotifications(prev =>
          prev.map(n => n.id === notification.id ? { ...n, isRead: true } : n)
        );
        setUnreadCount(prev => Math.max(0, prev - 1));
      }
    }

    if (notification.actionUrl) {
      navigate(notification.actionUrl);
      setIsOpen(false);
    }
  };

  const handleMarkAllAsRead = async () => {
    try {
      await apiService.markAllNotificationsAsRead();
      setNotifications(prev => prev.map(n => ({ ...n, isRead: true })));
      setUnreadCount(0);
    } catch (error) {
      console.error('모든 알림 읽음 처리 실패:', error);
      // 목업에서는 바로 처리
      setNotifications(prev => prev.map(n => ({ ...n, isRead: true })));
      setUnreadCount(0);
    }
  };

  const getNotificationIcon = (type: string) => {
    switch (type) {
      case 'APPROVAL_REQUEST': return <CheckCircle2 className="w-4 h-4 text-blue-500" />;
      case 'TEST_STATUS_CHANGE': return <Activity className="w-4 h-4 text-green-500" />;
      case 'SYSTEM_ALERT': return <AlertTriangle className="w-4 h-4 text-orange-500" />;
      case 'REMINDER': return <Clock className="w-4 h-4 text-purple-500" />;
      default: return <Info className="w-4 h-4 text-gray-500" />;
    }
  };

  const formatTimeAgo = (dateString: string) => {
    const now = new Date();
    const date = new Date(dateString);
    const diffInMinutes = Math.floor((now.getTime() - date.getTime()) / (1000 * 60));

    if (diffInMinutes < 1) return '방금 전';
    if (diffInMinutes < 60) return `${diffInMinutes}분 전`;

    const diffInHours = Math.floor(diffInMinutes / 60);
    if (diffInHours < 24) return `${diffInHours}시간 전`;

    const diffInDays = Math.floor(diffInHours / 24);
    return `${diffInDays}일 전`;
  };

  // 컴포넌트 마운트 시 알림 조회
  useEffect(() => {
    fetchNotifications();
  }, []);

  // 알림 드롭다운 외부 클릭 감지
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  return (
    <div className={`relative ${className}`} ref={dropdownRef}>
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="p-2.5 text-gray-600 hover:text-gray-900 hover:bg-gray-100 rounded-xl transition-all relative"
      >
        <Bell className="w-5 h-5" />
        {unreadCount > 0 && (
          <span className="absolute -top-1 -right-1 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">
            {unreadCount > 99 ? '99+' : unreadCount}
          </span>
        )}
      </button>

      {/* 알림 드롭다운 */}
      <AnimatePresence>
        {isOpen && (
          <motion.div
            initial={{ opacity: 0, scale: 0.95, y: -10 }}
            animate={{ opacity: 1, scale: 1, y: 0 }}
            exit={{ opacity: 0, scale: 0.95, y: -10 }}
            transition={{ duration: 0.2 }}
            className="absolute right-0 top-full mt-2 w-96 bg-white rounded-xl shadow-xl border border-gray-100 z-50 max-h-96 overflow-hidden"
          >
            {/* 헤더 */}
            <div className="p-4 border-b border-gray-100 flex items-center justify-between">
              <h3 className="font-semibold text-gray-900">알림</h3>
              {unreadCount > 0 && (
                <button
                  onClick={handleMarkAllAsRead}
                  className="text-sm text-blue-600 hover:text-blue-800 transition-colors"
                >
                  모두 읽음
                </button>
              )}
            </div>

            {/* 알림 목록 */}
            <div className="max-h-80 overflow-y-auto">
              {loading ? (
                <div className="p-4 text-center text-gray-500">
                  <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600 mx-auto mb-2"></div>
                  알림 로딩 중...
                </div>
              ) : notifications.length === 0 ? (
                <div className="p-8 text-center text-gray-500">
                  <Bell className="w-8 h-8 mx-auto mb-2 text-gray-300" />
                  <p>새로운 알림이 없습니다</p>
                </div>
              ) : (
                notifications.map((notification) => (
                  <div
                    key={notification.id}
                    onClick={() => handleNotificationClick(notification)}
                    className={`p-4 border-b border-gray-50 hover:bg-gray-50 cursor-pointer transition-colors ${
                      !notification.isRead ? 'bg-blue-50/30' : ''
                    }`}
                  >
                    <div className="flex items-start space-x-3">
                      <div className="flex-shrink-0 mt-0.5">
                        {getNotificationIcon(notification.type)}
                      </div>
                      <div className="flex-1 min-w-0">
                        <div className="flex items-center space-x-2 mb-1">
                          <p className="text-sm font-medium text-gray-900 truncate">
                            {notification.title}
                          </p>
                          {!notification.isRead && (
                            <div className="w-2 h-2 bg-blue-500 rounded-full flex-shrink-0"></div>
                          )}
                        </div>
                        <p className="text-sm text-gray-600 mb-1 line-clamp-2">
                          {notification.message}
                        </p>
                        <p className="text-xs text-gray-400">
                          {formatTimeAgo(notification.createdAt)}
                        </p>
                      </div>
                    </div>
                  </div>
                ))
              )}
            </div>

            {/* 푸터 */}
            {notifications.length > 0 && (
              <div className="p-3 border-t border-gray-100 text-center">
                <button
                  onClick={() => {
                    navigate('/notifications');
                    setIsOpen(false);
                  }}
                  className="text-sm text-blue-600 hover:text-blue-800 transition-colors"
                >
                  모든 알림 보기
                </button>
              </div>
            )}
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};

export default NotificationDropdown;