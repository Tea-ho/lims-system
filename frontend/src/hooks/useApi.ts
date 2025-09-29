import { useQuery, useMutation, useQueryClient, UseQueryOptions } from 'react-query';
import { apiService } from '@/services/api';
import { 
  Test, 
  TestCreateDto, 
  TestUpdateDto,
  TestSearchDto,
  PaginationParams, 
  FilterOptions,
  Product,
  ProductCreateDto,
  ProductUpdateDto,
  ProductSearchDto,
  User,
  TestStage,
  ReceiptCreateDto,
  ApprovalCreateDto,
  ApprovalSignUpdateDto,
  Approval,
  ApprovalStats
} from '@/types';
import toast from 'react-hot-toast';

// 시험 관련 훅
export const useTests = (params: PaginationParams & FilterOptions) => {
  return useQuery(
    ['tests', params],
    () => apiService.getTests(params),
    {
      keepPreviousData: true,
      staleTime: 5 * 60 * 1000, // 5분
    }
  );
};

export const useTest = (id: number, options?: UseQueryOptions<Test>) => {
  return useQuery(
    ['test', id],
    () => apiService.getTestById(id),
    {
      enabled: !!id,
      ...options,
    }
  );
};

export const useMyTests = (params: PaginationParams) => {
  return useQuery(
    ['myTests', params],
    () => apiService.getMyTests(params),
    {
      keepPreviousData: true,
    }
  );
};

export const useCreateTest = () => {
  const queryClient = useQueryClient();

  return useMutation(
    (dto: TestCreateDto) => apiService.createTest(dto),
    {
      onSuccess: (data) => {
        queryClient.invalidateQueries(['tests']);
        queryClient.invalidateQueries(['myTests']);
        queryClient.invalidateQueries(['dashboardData']);
        toast.success('시험 의뢰가 성공적으로 생성되었습니다.');
      },
      onError: (error: any) => {
        const message = error.response?.data?.message || '시험 의뢰 생성에 실패했습니다.';
        toast.error(message);
      },
    }
  );
};

export const useUpdateTest = () => {
  const queryClient = useQueryClient();

  return useMutation(
    ({ id, dto }: { id: number; dto: TestUpdateDto }) => apiService.updateTest(id, dto),
    {
      onSuccess: (data, variables) => {
        queryClient.invalidateQueries(['tests']);
        queryClient.invalidateQueries(['test', variables.id]);
        queryClient.invalidateQueries(['myTests']);
        toast.success('시험 정보가 수정되었습니다.');
      },
      onError: (error: any) => {
        const message = error.response?.data?.message || '시험 정보 수정에 실패했습니다.';
        toast.error(message);
      },
    }
  );
};

export const useDeleteTest = () => {
  const queryClient = useQueryClient();

  return useMutation(
    (id: number) => apiService.deleteTest(id),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['tests']);
        queryClient.invalidateQueries(['myTests']);
        queryClient.invalidateQueries(['dashboardData']);
        toast.success('시험이 삭제되었습니다.');
      },
      onError: (error: any) => {
        const message = error.response?.data?.message || '시험 삭제에 실패했습니다.';
        toast.error(message);
      },
    }
  );
};

// 제품 관련 훅
export const useProducts = (params?: ProductSearchDto) => {
  return useQuery(
    ['products', params],
    () => apiService.getProducts(params),
    {
      staleTime: 10 * 60 * 1000, // 10분
    }
  );
};

export const useProduct = (id: number, options?: UseQueryOptions<Product>) => {
  return useQuery(
    ['product', id],
    () => apiService.getProductById(id),
    {
      enabled: !!id,
      ...options,
    }
  );
};

export const useCreateProduct = () => {
  const queryClient = useQueryClient();

  return useMutation(
    (dto: ProductCreateDto) => apiService.createProduct(dto),
    {
      onSuccess: (data) => {
        queryClient.invalidateQueries(['products']);
        queryClient.invalidateQueries(['dashboardData']);
        toast.success('제품이 성공적으로 등록되었습니다.');
      },
      onError: (error: any) => {
        const message = error.response?.data?.message || '제품 등록에 실패했습니다.';
        toast.error(message);
      },
    }
  );
};

export const useUpdateProduct = () => {
  const queryClient = useQueryClient();

  return useMutation(
    ({ id, dto }: { id: number; dto: ProductUpdateDto }) => apiService.updateProduct(id, dto),
    {
      onSuccess: (data, variables) => {
        queryClient.invalidateQueries(['products']);
        queryClient.invalidateQueries(['product', variables.id]);
        toast.success('제품 정보가 수정되었습니다.');
      },
      onError: (error: any) => {
        const message = error.response?.data?.message || '제품 정보 수정에 실패했습니다.';
        toast.error(message);
      },
    }
  );
};

export const useDeleteProduct = () => {
  const queryClient = useQueryClient();

  return useMutation(
    (id: number) => apiService.deleteProduct(id),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['products']);
        queryClient.invalidateQueries(['dashboardData']);
        toast.success('제품이 삭제되었습니다.');
      },
      onError: (error: any) => {
        const message = error.response?.data?.message || '제품 삭제에 실패했습니다.';
        toast.error(message);
      },
    }
  );
};

// 사용자 관련 훅
export const useCurrentUser = () => {
  return useQuery(
    ['currentUser'],
    () => apiService.getCurrentUser(),
    {
      staleTime: 10 * 60 * 1000, // 10분
      retry: 1,
    }
  );
};

export const useUsers = (search?: string) => {
  return useQuery(
    ['users', { search }],
    () => apiService.getUsers({ search }),
    {
      enabled: search !== undefined,
      staleTime: 5 * 60 * 1000,
    }
  );
};

// 대시보드 관련 훅
export const useDashboardData = () => {
  return useQuery(
    ['dashboardData'],
    () => apiService.getDashboardData(),
    {
      staleTime: 2 * 60 * 1000,
      refetchInterval: 2 * 60 * 1000,
    }
  );
};

// 승인 관련 훅 (기존 유지)
export const useApprovals = (params: PaginationParams & FilterOptions) => {
  return useQuery(
    ['approvals', params],
    () => apiService.getApprovals(params),
    {
      keepPreviousData: true,
      staleTime: 5 * 60 * 1000,
    }
  );
};

export const useApproval = (id: number, options?: UseQueryOptions<Approval>) => {
  return useQuery(
    ['approval', id],
    () => apiService.getApprovalById(id),
    {
      enabled: !!id,
      ...options,
    }
  );
};

export const useMyApprovals = (params: PaginationParams) => {
  return useQuery(
    ['myApprovals', params],
    () => apiService.getMyApprovals(params),
    {
      keepPreviousData: true,
    }
  );
};

export const usePendingApprovals = (params: PaginationParams) => {
  return useQuery(
    ['pendingApprovals', params],
    () => apiService.getPendingApprovals(params),
    {
      keepPreviousData: true,
      refetchInterval: 30 * 1000, // 30초마다 자동 갱신
    }
  );
};

export const useApprovalStats = () => {
  return useQuery(
    ['approvalStats'],
    () => apiService.getApprovalStats(),
    {
      staleTime: 2 * 60 * 1000,
      refetchInterval: 60 * 1000,
    }
  );
};

export const useCreateApproval = () => {
  const queryClient = useQueryClient();

  return useMutation(
    (dto: ApprovalCreateDto) => apiService.createApproval(dto),
    {
      onSuccess: (data) => {
        queryClient.invalidateQueries(['approvals']);
        queryClient.invalidateQueries(['approvalStats']);
        queryClient.invalidateQueries(['myApprovals']);
        toast.success('승인 요청이 성공적으로 생성되었습니다.');
      },
      onError: (error: any) => {
        const message = error.response?.data?.message || '승인 요청 생성에 실패했습니다.';
        toast.error(message);
      },
    }
  );
};

export const useUpdateApprovalSign = () => {
  const queryClient = useQueryClient();

  return useMutation(
    ({ approvalId, signId, dto }: { 
      approvalId: number; 
      signId: number; 
      dto: ApprovalSignUpdateDto; 
    }) => apiService.updateApprovalSign(approvalId, signId, dto),
    {
      onSuccess: (data, variables) => {
        queryClient.invalidateQueries(['approvals']);
        queryClient.invalidateQueries(['approval', variables.approvalId]);
        queryClient.invalidateQueries(['pendingApprovals']);
        queryClient.invalidateQueries(['myApprovals']);
        queryClient.invalidateQueries(['approvalStats']);
        queryClient.invalidateQueries(['dashboardData']);
        
        const action = variables.dto.status === 'APPROVED' ? '승인' : '반려';
        toast.success(`${action} 처리가 완료되었습니다.`);
      },
      onError: (error: any, variables, context) => {
        let message = '처리 중 오류가 발생했습니다.';
        
        if (error.response?.status === 409) {
          message = '동시성 충돌이 발생했습니다. 페이지를 새로고침 후 다시 시도해주세요.';
        } else if (error.response?.data?.message) {
          message = error.response.data.message;
        }
        
        toast.error(message);
      },
      retry: (failureCount, error: any) => {
        if (error.response?.status === 409 && failureCount < 2) {
          return true;
        }
        return false;
      },
      retryDelay: (attemptIndex) => Math.min(1000 * 2 ** attemptIndex, 3000),
    }
  );
};