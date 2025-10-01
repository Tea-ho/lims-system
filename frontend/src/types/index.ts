export type ApprovalStatus = 'PENDING' | 'PARTIAL_APPROVED' | 'APPROVED' | 'REJECTED';
export type TestStage = 'REQUEST' | 'RECEIPT' | 'RECEIPT_APPROVAL' | 'RESULT_INPUT' | 'RESULT_APPROVAL' | 'COMPLETED';
export type UserRole = 'ADMIN' | 'MANAGER' | 'USER' | 'ANALYST' | 'REVIEWER';
export type NotificationType = 'APPROVAL_REQUEST' | 'TEST_STATUS_CHANGE' | 'SYSTEM_ALERT' | 'REMINDER';

// User Types
export interface User {
  id: number;
  username: string;
  name: string;
  email: string;
  role: UserRole;
  department: string;
  position: string;
  createdAt: string;
  updatedAt: string;
  avatar?: string;
}

export interface UserCreateDto {
  username: string;
  name: string;
  email: string;
  password: string;
  role: UserRole;
  department: string;
  position: string;
}

export interface UserUpdateDto {
  name: string;
  email: string;
  role: UserRole;
  department: string;
  position: string;
}

export interface UserSearchDto {
  search?: string;
  role?: UserRole;
  department?: string;
}

// Product Types
export interface Product {
  id: number;
  name: string;
  description?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ProductCreateDto {
  name: string;
  description?: string;
}

export interface ProductUpdateDto {
  name: string;
  description?: string;
}

export interface ProductSearchDto {
  search?: string;
  name?: string;
}

// Test Types
export interface RequestInfo {
  id: number;
  requesterId: number;
  requesterName: string;
  purpose: string;
  requestDate: string;
  expectedDate?: string;
  notes?: string;
}

export interface ReceiptInfo {
  id: number;
  receiverId: number;
  receiverName: string;
  receiptDate: string;
  condition: string;
  notes?: string;
}

export interface ResultInfo {
  id: number;
  analystId: number;
  analystName: string;
  testMethod: string;
  result: string;
  conclusion: string;
  testDate: string;
  notes?: string;
}

export interface CompletionInfo {
  id: number;
  reviewerId: number;
  reviewerName: string;
  completionDate: string;
  finalComment?: string;
}

export interface Test {
  id: number;
  userId: number;
  productId: number;
  stage: TestStage;
  createdAt: string;
  updatedAt: string;
  // Related objects
  requestInfo?: RequestInfo;
  receiptInfo?: ReceiptInfo;
  resultInfo?: ResultInfo;
  completionInfo?: CompletionInfo;
  product?: Product;
  user?: User;
}

export interface TestCreateDto {
  productId: number;
  title: string;
  description: string;
  requiresApproval: boolean;
}

export interface TestUpdateDto {
  requestInfo?: {
    purpose: string;
    expectedDate?: string;
    notes?: string;
  };
}

export interface TestSearchDto {
  search?: string;
  stage?: TestStage;
  userId?: number;
  productId?: number;
  dateRange?: {
    start: string;
    end: string;
  };
}

export interface ReceiptCreateDto {
  condition: string;
  notes?: string;
}

export interface ResultCreateDto {
  testMethod: string;
  result: string;
  conclusion: string;
  notes?: string;
}

export interface CompletionCreateDto {
  finalComment?: string;
}

// Approval Types (기존 유지)
export interface ApprovalRequest {
  id: number;
  requesterId: number;
  requesterName: string;
  comment: string;
  targetType: string;
  targetId: string;
  createdAt: string;
}

export interface ApprovalSign {
  id: number;
  approvalId: number;
  approverId: number;
  approverName: string;
  targetId: number;
  stage: TestStage;
  status: ApprovalStatus;
  comment: string | null;
  signedAt?: string;
  avatar?: string;
}

export interface Approval {
  id: number;
  status: ApprovalStatus;
  version: number;
  createdAt: string;
  updatedAt?: string;
  request: ApprovalRequest;
  signs: ApprovalSign[];
}

export interface ApprovalCreateDto {
  requesterId: number;
  comment: string;
  signs: {
    approverId: number;
    targetId: number;
    stage: TestStage;
  }[];
}

export interface ApprovalSignUpdateDto {
  status: ApprovalStatus;
  comment: string;
}

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message: string;
  timestamp: string;
}

export interface PaginationParams {
  page: number;
  size: number;
  sort?: string;
  direction?: 'ASC' | 'DESC';
}

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface ApprovalStats {
  total: number;
  pending: number;
  partialApproved: number;
  approved: number;
  rejected: number;
  myPendingApprovals: number;
}

// Notification Types
export interface Notification {
  id: number;
  type: NotificationType;
  title: string;
  message: string;
  isRead: boolean;
  createdAt: string;
  targetId?: number;
  targetType?: 'test' | 'approval' | 'user';
  actionUrl?: string;
}

export interface NotificationSettings {
  emailNotifications: boolean;
  pushNotifications: boolean;
  approvalRequests: boolean;
  statusUpdates: boolean;
  reminders: boolean;
}

export interface FilterOptions {
  status?: ApprovalStatus[];
  dateRange?: {
    start: string;
    end: string;
  };
  requesterId?: number[];
  approverId?: number[];
  targetType?: string[];
}
