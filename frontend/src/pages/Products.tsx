import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { Link, useNavigate } from 'react-router-dom';
import {
  Package, Plus, Search, Filter, Eye, Edit, Trash2,
  Calendar, FileText, MoreVertical, RefreshCw
} from 'lucide-react';
import { Product, ProductCreateDto, ProductUpdateDto } from '@/types';
import toast from 'react-hot-toast';

// API Service Class
class ProductService {
  private static readonly API_BASE_URL = 'http://localhost:3001';

  private static async request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
    const url = `${this.API_BASE_URL}${endpoint}`;
    const config: RequestInit = {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      ...options,
    };

    try {
      const response = await fetch(url, config);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error('API request failed:', error);
      throw error;
    }
  }

  static async getAllProducts(): Promise<Product[]> {
    const response = await this.request<{ data: Product[] }>('/api/products');
    return response.data;
  }

  static async createProduct(productData: ProductCreateDto): Promise<Product> {
    const response = await this.request<{ data: Product }>('/api/products', {
      method: 'POST',
      body: JSON.stringify(productData),
    });
    return response.data;
  }

  static async updateProduct(id: number, productData: ProductUpdateDto): Promise<Product> {
    const response = await this.request<{ data: Product }>(`/api/products/${id}`, {
      method: 'PUT',
      body: JSON.stringify(productData),
    });
    return response.data;
  }

  static async deleteProduct(id: number): Promise<void> {
    await this.request<void>(`/api/products/${id}`, { method: 'DELETE' });
  }
}

const Products: React.FC = () => {
  const navigate = useNavigate();
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);

  // 실제 API에서 데이터 가져오기
  useEffect(() => {
    const fetchProducts = async () => {
      setLoading(true);
      try {
        const productsData = await ProductService.getAllProducts();
        console.log('받아온 제품 데이터:', productsData);
        // 데이터가 배열인지 확인
        if (Array.isArray(productsData)) {
          setProducts(productsData);
        } else {
          console.error('제품 데이터가 배열이 아닙니다:', productsData);
          setProducts([]);
          toast.error('제품 데이터 형식이 올바르지 않습니다.');
        }
      } catch (error) {
        console.error('제품 데이터를 가져오는 중 오류 발생:', error);
        toast.error('제품 데이터를 불러오는데 실패했습니다.');
        // 백엔드 연결 실패 시 빈 배열로 설정
        setProducts([]);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  const filteredProducts = Array.isArray(products) ? products.filter(product =>
    !searchQuery ||
    product.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    (product.description && product.description.toLowerCase().includes(searchQuery.toLowerCase()))
  ) : [];

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const handleDelete = async (product: Product) => {
    if (!window.confirm(`${product.name} 제품을 삭제하시겠습니까?`)) return;

    try {
      await ProductService.deleteProduct(product.id);
      setProducts(prev => prev.filter(p => p.id !== product.id));
      toast.success('제품이 삭제되었습니다.');
    } catch (error) {
      console.error('제품 삭제 중 오류:', error);
      toast.error('제품 삭제 중 오류가 발생했습니다.');
    }
  };

  const ProductModal = ({ isOpen, onClose, product }: { isOpen: boolean; onClose: () => void; product?: Product | null }) => {
    const [formData, setFormData] = useState({
      name: product?.name || '',
      description: product?.description || ''
    });
    const [saving, setSaving] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
      e.preventDefault();
      setSaving(true);

      try {
        if (product) {
          // 수정
          const updatedProduct = await ProductService.updateProduct(product.id, formData);
          setProducts(prev => prev.map(p =>
            p.id === product.id ? updatedProduct : p
          ));
          toast.success('제품이 수정되었습니다.');
        } else {
          // 생성
          const newProduct = await ProductService.createProduct(formData);
          setProducts(prev => [newProduct, ...prev]);
          toast.success('제품이 생성되었습니다.');
        }

        onClose();
      } catch (error) {
        console.error('제품 저장 중 오류:', error);
        toast.error('제품 저장 중 오류가 발생했습니다.');
      } finally {
        setSaving(false);
      }
    };

    if (!isOpen) return null;

    return (
      <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
        <motion.div
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          className="bg-white rounded-xl p-6 w-full max-w-md mx-4"
        >
          <h3 className="text-lg font-semibold text-gray-900 mb-4">
            {product ? '제품 수정' : '새 제품 생성'}
          </h3>
          
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                제품명
              </label>
              <input
                type="text"
                required
                value={formData.name}
                onChange={(e) => setFormData(prev => ({ ...prev, name: e.target.value }))}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                placeholder="제품명을 입력하세요"
              />
            </div>
            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                제품 설명
              </label>
              <textarea
                value={formData.description}
                onChange={(e) => setFormData(prev => ({ ...prev, description: e.target.value }))}
                rows={3}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                placeholder="제품에 대한 설명을 입력하세요"
              />
            </div>
            
            <div className="flex justify-end space-x-3 pt-4">
              <button
                type="button"
                onClick={onClose}
                className="px-4 py-2 text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors"
              >
                취소
              </button>
              <button
                type="submit"
                disabled={saving}
                className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors disabled:opacity-50"
              >
                {saving ? '저장 중...' : (product ? '수정' : '생성')}
              </button>
            </div>
          </form>
        </motion.div>
      </div>
    );
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center py-12">
        <RefreshCw className="w-8 h-8 animate-spin text-indigo-600" />
        <span className="ml-3 text-lg text-gray-600">제품 데이터 로딩중...</span>
      </div>
    );
  }

  return (
    <>
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="space-y-6"
      >
        {/* 헤더 영역 */}
        <div className="bg-white rounded-xl shadow-sm p-6">
          <div className="flex items-center justify-between mb-6">
            <div className="flex items-center space-x-3">
              <Package className="w-8 h-8 text-indigo-600" />
              <div>
                <h1 className="text-3xl font-bold text-gray-900">제품 관리</h1>
                <p className="text-gray-600 mt-1">시험 대상 제품을 관리합니다</p>
              </div>
            </div>
            
            <div className="flex items-center space-x-3">
              <button
                onClick={() => window.location.reload()}
                className="p-2 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-lg transition-colors"
              >
                <RefreshCw className="w-5 h-5" />
              </button>
              
              <button
                onClick={() => setShowCreateModal(true)}
                className="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 flex items-center space-x-2 transition-colors"
              >
                <Plus className="w-5 h-5" />
                <span>새 제품 등록</span>
              </button>
            </div>
          </div>

          {/* 검색 영역 */}
          <div className="flex items-center justify-between space-x-4">
            <div className="flex-1 max-w-md">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type="text"
                  placeholder="제품명이나 설명으로 검색..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full pl-10 pr-4 py-2.5 border border-gray-200 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                />
              </div>
            </div>
            
            <div className="text-sm text-gray-500">
              총 {filteredProducts.length}개 제품
            </div>
          </div>
        </div>

        {/* 제품 목록 */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredProducts.map((product) => (
            <motion.div
              key={product.id}
              initial={{ opacity: 0, scale: 0.95 }}
              animate={{ opacity: 1, scale: 1 }}
              className="bg-white rounded-xl shadow-sm border border-gray-100 hover:shadow-md transition-shadow"
            >
              <div className="p-6">
                <div className="flex items-start justify-between mb-4">
                  <div className="flex items-center space-x-3">
                    <div className="p-2 bg-indigo-100 rounded-lg">
                      <Package className="w-6 h-6 text-indigo-600" />
                    </div>
                    <div>
                      <h3 className="font-semibold text-gray-900">{product.name}</h3>
                      <p className="text-sm text-gray-500">ID: {product.id}</p>
                    </div>
                  </div>
                  
                  <div className="relative">
                    <button className="p-1 text-gray-400 hover:text-gray-600 rounded">
                      <MoreVertical className="w-4 h-4" />
                    </button>
                  </div>
                </div>
                
                <p className="text-gray-600 text-sm mb-4 line-clamp-2">
                  {product.description || '설명이 없습니다.'}
                </p>
                
                <div className="space-y-2 mb-4">
                  <div className="flex items-center text-xs text-gray-500">
                    <Calendar className="w-3 h-3 mr-1" />
                    <span>생성: {formatDate(product.createdAt)}</span>
                  </div>
                  <div className="flex items-center text-xs text-gray-500">
                    <FileText className="w-3 h-3 mr-1" />
                    <span>수정: {formatDate(product.updatedAt)}</span>
                  </div>
                </div>
                
                <div className="flex items-center justify-between">
                  <button
                    onClick={() => navigate(`/products/${product.id}`)}
                    className="text-indigo-600 hover:text-indigo-700 flex items-center space-x-1 text-sm"
                  >
                    <Eye className="w-4 h-4" />
                    <span>상세보기</span>
                  </button>
                  
                  <div className="flex items-center space-x-2">
                    <button
                      onClick={() => setEditingProduct(product)}
                      className="p-1 text-gray-400 hover:text-indigo-600 transition-colors"
                    >
                      <Edit className="w-4 h-4" />
                    </button>
                    <button
                      onClick={() => handleDelete(product)}
                      className="p-1 text-gray-400 hover:text-red-600 transition-colors"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              </div>
            </motion.div>
          ))}
        </div>

        {filteredProducts.length === 0 && (
          <div className="bg-white rounded-xl shadow-sm p-12 text-center">
            <Package className="w-16 h-16 mx-auto text-gray-300 mb-4" />
            <p className="text-lg text-gray-500 mb-2">
              {searchQuery ? '검색 결과가 없습니다' : '등록된 제품이 없습니다'}
            </p>
            <p className="text-sm text-gray-400">
              {searchQuery ? '다른 검색어를 시도해보세요' : '새 제품을 등록해보세요'}
            </p>
          </div>
        )}
      </motion.div>

      {/* 제품 생성/수정 모달 */}
      <ProductModal 
        isOpen={showCreateModal || !!editingProduct}
        onClose={() => {
          setShowCreateModal(false);
          setEditingProduct(null);
        }}
        product={editingProduct}
      />
    </>
  );
};

export default Products;