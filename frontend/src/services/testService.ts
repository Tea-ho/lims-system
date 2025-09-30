import { apiService } from './api';
import { Test, TestCreateDto, TestUpdateDto, TestSearchDto, PaginationParams, FilterOptions } from '../types';

export class TestService {
  static async getAllTests(): Promise<Test[]> {
    try {
      const response = await apiService.getTests({
        page: 0,
        size: 100
      } as PaginationParams & FilterOptions);
      console.log('🔍 API Response for getAllTests:', response);
      return Array.isArray(response) ? response : [];
    } catch (error) {
      console.error('❌ Error in getAllTests:', error);
      return [];
    }
  }

  static async getTestById(id: number): Promise<Test> {
    try {
      console.log('🔍 Getting test by ID:', id);
      const response = await apiService.getTestById(id);
      console.log('✅ Test data received:', response);
      return response;
    } catch (error) {
      console.error('❌ Error in getTestById:', error);
      throw error;
    }
  }

  static async createTest(testData: TestCreateDto): Promise<Test> {
    return apiService.createTest(testData);
  }

  static async updateTest(id: number, testData: TestUpdateDto): Promise<Test> {
    return apiService.updateTest(id, testData);
  }

  static async deleteTest(id: number): Promise<void> {
    return apiService.deleteTest(id);
  }

  static async searchTests(searchParams: TestSearchDto): Promise<Test[]> {
    try {
      const response = await apiService.getTests({
        page: 0,
        size: 100,
        ...searchParams
      } as PaginationParams & FilterOptions);
      console.log('🔍 Search API Response:', response);
      return Array.isArray(response) ? response : [];
    } catch (error) {
      console.error('❌ Error in searchTests:', error);
      return [];
    }
  }

  static async receiptTest(id: number, receiptData: { receiptDetails: string; requiresApproval: boolean }): Promise<Test> {
    return apiService.moveToReceipt(id, receiptData);
  }

  static async backToRequest(id: number): Promise<Test> {
    return apiService.moveBackToRequest(id);
  }

  static async backToReceipt(id: number): Promise<Test> {
    // API에 해당 메서드가 없으므로 임시로 backToRequest 사용
    return apiService.moveBackToRequest(id);
  }

  static async receiptApproval(id: number, approvalData: { title: string; description: string; requiresApproval: boolean; approverIds: number[] }): Promise<Test> {
    return apiService.moveToReceiptApproval(id, approvalData);
  }

  static async inputResult(id: number, approvalId: number, resultData: { decision: string; comments?: string }): Promise<Test> {
    return apiService.moveToInputResult(id, approvalId, resultData);
  }
}