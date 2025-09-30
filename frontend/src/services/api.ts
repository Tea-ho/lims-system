import axios, { AxiosInstance, AxiosResponse } from 'axios';
import { 
  Approval, 
  ApprovalCreateDto, 
  ApprovalSignUpdateDto, 
  ApiResponse, 
  PaginatedResponse, 
  PaginationParams,
  ApprovalStats,
  User,
  FilterOptions,
  Test,
  TestCreateDto,
  TestUpdateDto,
  TestSearchDto,
  Product,
  ProductCreateDto,
  ProductUpdateDto,
  ProductSearchDto,
  ReceiptCreateDto,
  TestStage,
  Notification
} from '@/types';

class ApiService {
  private api: AxiosInstance;

  constructor() {
    this.api = axios.create({
      baseURL: 'http://localhost:8089/api',
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json',
      },
    });
    console.log('🌐 API Service initialized with baseURL:', 'http://localhost:8089/api');

    this.setupInterceptors();
  }

  private setupInterceptors() {
    // 요청 인터셉터
    this.api.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem('accessToken');
        console.log('🔐 API Request:', config.url, 'Token:', token ? 'Present' : 'Missing');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    // 응답 인터셉터
    this.api.interceptors.response.use(
      (response: AxiosResponse<ApiResponse<any>>) => {
        console.log('✅ API Response:', response.config.url, 'Status:', response.status);
        return response;
      },
      async (error) => {
        console.error('❌ API Error:', error.config?.url, 'Status:', error.response?.status, 'Message:', error.message);
        if (error.response?.status === 401) {
          // 토큰 만료 처리
          localStorage.removeItem('accessToken');
          localStorage.removeItem('user');
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }
    );
  }

  // 시험 관련 API
  async getTests(params: PaginationParams & FilterOptions): Promise<Test[]> {
    const response = await this.api.get('/tests/search', { params });
    return response.data.data || response.data || [];
  }

  async getTestById(id: number): Promise<Test> {
    const response = await this.api.get(`/tests/${id}`);
    console.log('🔍 Raw API response for getTestById:', response.data);
    // 백엔드에서 TestResponseDto를 직접 반환하므로 response.data를 사용
    return response.data;
  }

  async createTest(dto: TestCreateDto): Promise<Test> {
    const response = await this.api.post('/tests', dto);
    return response.data.data;
  }

  async updateTest(id: number, dto: TestUpdateDto): Promise<Test> {
    const response = await this.api.put(`/tests/${id}`, dto);
    return response.data.data;
  }

  async deleteTest(id: number): Promise<void> {
    await this.api.delete(`/tests/${id}`);
  }

  async getMyTests(params: PaginationParams): Promise<PaginatedResponse<Test>> {
    const response = await this.api.get('/tests/my', { params });
    return response.data.data;
  }

  async moveToReceipt(testId: number, dto: ReceiptCreateDto): Promise<Test> {
    const response = await this.api.post(`/tests/${testId}/receipt`, dto);
    return response.data.data;
  }

  async moveBackToRequest(testId: number): Promise<Test> {
    const response = await this.api.post(`/tests/${testId}/back-to-request`);
    return response.data.data;
  }

  async moveToReceiptApproval(testId: number, approvalDto: ApprovalCreateDto): Promise<Test> {
    const response = await this.api.post(`/tests/${testId}/receipt-approval`, approvalDto);
    return response.data.data;
  }

  async moveToInputResult(testId: number, approvalId: number, updateDto: ApprovalSignUpdateDto): Promise<Test> {
    const response = await this.api.post(`/tests/${testId}/input-result?approvalId=${approvalId}`, updateDto);
    return response.data.data;
  }

  // 제품 관련 API
  async getProducts(params?: ProductSearchDto): Promise<Product[]> {
    const response = await this.api.get('/products', { params });
    return response.data.data;
  }

  async getProductById(id: number): Promise<Product> {
    const response = await this.api.get(`/products/${id}`);
    console.log('🔍 Raw API response for getProductById:', response.data);
    // 백엔드에서 ProductResponseDto를 직접 반환하므로 response.data를 사용
    return response.data;
  }

  async createProduct(dto: ProductCreateDto): Promise<Product> {
    const response = await this.api.post('/products', dto);
    return response.data.data;
  }

  async updateProduct(id: number, dto: ProductUpdateDto): Promise<Product> {
    const response = await this.api.put(`/products/${id}`, dto);
    return response.data.data;
  }

  async deleteProduct(id: number): Promise<void> {
    await this.api.delete(`/products/${id}`);
  }

  // 사용자 관리 API
  async getCurrentUser(): Promise<User> {
    const response = await this.api.get('/users/me');
    return response.data.data;
  }

  async getUsers(params?: { search?: string }): Promise<User[]> {
    const response = await this.api.get('/users', { params });
    return response.data.data;
  }

  async getUserById(id: number): Promise<User> {
    const response = await this.api.get(`/users/${id}`);
    return response.data.data;
  }

  // 인증 API
  async login(username: string, password: string): Promise<{ token: string; username: string; authorities: string; loginTime: string }> {
    const response = await this.api.post('/auth/login', { username, password });
    return response.data;
  }

  async logout(): Promise<void> {
    await this.api.post('/auth/logout');
    localStorage.removeItem('accessToken');
    localStorage.removeItem('user');
  }

  async refreshToken(): Promise<string> {
    const response = await this.api.post('/auth/refresh');
    return response.data.data.token;
  }

  // 승인 관련 API
  async getApprovals(params: PaginationParams & FilterOptions): Promise<PaginatedResponse<Approval>> {
    const response = await this.api.get('/approvals', { params });
    return response.data.data;
  }

  async getApprovalById(id: number): Promise<Approval> {
    const response = await this.api.get(`/approvals/${id}`);
    return response.data.data;
  }

  async getMyApprovals(params: PaginationParams): Promise<PaginatedResponse<Approval>> {
    const response = await this.api.get('/approvals/my', { params });
    return response.data.data;
  }

  async getPendingApprovals(params: PaginationParams): Promise<PaginatedResponse<Approval>> {
    const response = await this.api.get('/approvals/pending', { params });
    return response.data.data;
  }

  async getApprovalStats(): Promise<ApprovalStats> {
    const response = await this.api.get('/approvals/stats');
    return response.data.data;
  }

  async createApproval(dto: ApprovalCreateDto): Promise<Approval> {
    const response = await this.api.post('/approvals', dto);
    return response.data.data;
  }

  async updateApprovalSign(approvalId: number, signId: number, dto: ApprovalSignUpdateDto): Promise<Approval> {
    const response = await this.api.put(`/approvals/${approvalId}/signs/${signId}`, dto);
    return response.data.data;
  }

  async processApproval(approvalId: number, data: { signId: string; status: string; comment: string }): Promise<Approval> {
    const response = await this.api.put(`/approvals/${approvalId}/process`, data);
    return response.data.data;
  }

  // 검색 API
  async searchApprovals(query: string): Promise<Approval[]> {
    const response = await this.api.get('/approvals/search', { params: { q: query } });
    return response.data.data;
  }

  // 대시보드 API
  async getDashboardData(): Promise<{
    stats: {
      totalTests: number;
      pendingTests: number;
      completedTests: number;
      myTests: number;
      totalProducts: number;
      pendingApprovals: number;
    };
    recentTests: Test[];
    stageCounts: Record<TestStage, number>;
  }> {
    const response = await this.api.get('/dashboard');
    return response.data.data;
  }

  // 파일 업로드 API
  async uploadFile(file: File, approvalId: number): Promise<string> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('approvalId', approvalId.toString());

    const response = await this.api.post('/files/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    
    return response.data.data.fileUrl;
  }

  // WebSocket 연결용 토큰 API
  async getWebSocketToken(): Promise<string> {
    const response = await this.api.get('/auth/ws-token');
    return response.data.data.token;
  }

  // 알림 관련 API
  async getNotifications(params?: { page?: number; limit?: number; unreadOnly?: boolean }): Promise<PaginatedResponse<Notification>> {
    const response = await this.api.get('/notifications', { params });
    return response.data.data;
  }

  async getUnreadNotificationsCount(): Promise<number> {
    const response = await this.api.get('/notifications/unread-count');
    return response.data.data;
  }

  async markNotificationAsRead(notificationId: number): Promise<void> {
    await this.api.put(`/notifications/${notificationId}/read`);
  }

  async markAllNotificationsAsRead(): Promise<void> {
    await this.api.put('/notifications/read-all');
  }

  async deleteNotification(notificationId: number): Promise<void> {
    await this.api.delete(`/notifications/${notificationId}`);
  }
}

export const apiService = new ApiService();
export default apiService;
