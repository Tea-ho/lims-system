import { apiService } from './api';
import { Product, ProductCreateDto, ProductUpdateDto, ProductSearchDto } from '../types';

export class ProductService {
  static async getAllProducts(): Promise<Product[]> {
    try {
      const response = await apiService.getProducts();
      console.log('API Response for getAllProducts:', response);
      return Array.isArray(response) ? response : [];
    } catch (error) {
      console.error('Error in getAllProducts:', error);
      return [];
    }
  }

  static async getProductById(id: number): Promise<Product> {
    try {
      console.log('Getting product by ID:', id);
      const response = await apiService.getProductById(id);
      console.log('Product data received:', response);
      return response;
    } catch (error) {
      console.error('Error in getProductById:', error);
      throw error;
    }
  }

  static async createProduct(productData: ProductCreateDto): Promise<Product> {
    return apiService.createProduct(productData);
  }

  static async updateProduct(id: number, productData: ProductUpdateDto): Promise<Product> {
    return apiService.updateProduct(id, productData);
  }

  static async deleteProduct(id: number): Promise<void> {
    return apiService.deleteProduct(id);
  }

  static async searchProducts(searchParams: ProductSearchDto): Promise<Product[]> {
    try {
      const response = await apiService.getProducts(searchParams);
      console.log('Search API Response:', response);
      return Array.isArray(response) ? response : [];
    } catch (error) {
      console.error('Error in searchProducts:', error);
      return [];
    }
  }
}