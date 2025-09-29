import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { User, UserRole } from '@/types';
import { apiService } from '@/services/api';
import toast from 'react-hot-toast';

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (username: string, password: string) => Promise<boolean>;
  logout: () => void;
  setUser: (user: User | null) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const isAuthenticated = !!user;

  // 초기화: 로컬스토리지에서 토큰과 사용자 정보 확인
  useEffect(() => {
    const initAuth = async () => {
      try {
        const token = localStorage.getItem('accessToken');
        const savedUser = localStorage.getItem('user');
        
        if (token && savedUser) {
          const userData = JSON.parse(savedUser);
          setUser(userData);
          
          // 토큰이 유효한지 서버에서 검증 (옵션)
          try {
            const currentUser = await apiService.getCurrentUser();
            setUser(currentUser);
            localStorage.setItem('user', JSON.stringify(currentUser));
          } catch (error) {
            // 토큰이 무효하면 초기화
            localStorage.removeItem('accessToken');
            localStorage.removeItem('user');
            setUser(null);
          }
        }
      } catch (error) {
        console.error('Auth initialization failed:', error);
      } finally {
        setIsLoading(false);
      }
    };

    initAuth();
  }, []);

  const login = async (username: string, password: string): Promise<boolean> => {
    try {
      setIsLoading(true);

      // 실제 로그인 API 호출
      const result = await apiService.login(username, password);

      // 백엔드 응답에서 사용자 객체 생성
      const user: User = {
        id: 0, // 임시 ID, 나중에 getCurrentUser로 실제 정보 가져오기
        username: result.username,
        name: result.username,
        email: `${result.username}@company.com`,
        role: result.authorities.replace('ROLE_', '') as UserRole,
        department: '',
        position: '',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
      };

      // 서버에서 받은 토큰과 사용자 정보 저장
      localStorage.setItem('accessToken', result.token);
      localStorage.setItem('user', JSON.stringify(user));
      setUser(user);

      toast.success(`${result.username}님, 환영합니다!`);
      return true;
    } catch (error: any) {
      const message = error.response?.data?.message || '로그인에 실패했습니다.';
      toast.error(message);
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  const logout = async () => {
    try {
      // 서버에 로그아웃 요청
      await apiService.logout();
    } catch (error) {
      console.error('Logout API failed:', error);
    } finally {
      // 클라이언트 상태 정리 (API 실패해도 로컬 정리는 진행)
      localStorage.removeItem('accessToken');
      localStorage.removeItem('user');
      setUser(null);

      toast.success('로그아웃되었습니다.');
    }
  };

  const value: AuthContextType = {
    user,
    isAuthenticated,
    isLoading,
    login,
    logout,
    setUser,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
