import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import { Plus, Package, FileText, ArrowLeft, Loader2 } from 'lucide-react';
import { Product, TestCreateDto } from '@/types';
import { TestService } from '@/services/testService';
import { apiService } from '@/services/api';
import toast from 'react-hot-toast';

const CreateTest: React.FC = () => {
  const navigate = useNavigate();
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  const [formData, setFormData] = useState({
    productId: '',
    purpose: '',
    expectedDate: '',
    notes: ''
  });

  // Load products on component mount
  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setLoading(true);
        console.log('🔍 제품 데이터를 가져오는 중...');
        const productsData = await apiService.getProducts();
        console.log('📦 받아온 제품 데이터:', productsData);
        setProducts(productsData);
        console.log('✅ 제품 상태 업데이트 완료');
      } catch (error) {
        console.error('❌ 제품 데이터를 가져오는 중 오류 발생:', error);
        toast.error('제품 목록을 불러오는데 실패했습니다.');
        setProducts([]);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData.productId || !formData.purpose) {
      toast.error('제품과 시험 목적을 입력해주세요.');
      return;
    }

    setSubmitting(true);
    try {
      const testData: TestCreateDto = {
        productId: parseInt(formData.productId),
        title: formData.purpose,
        description: formData.notes || '시험 의뢰',
        requiresApproval: false
      };

      const newTest = await TestService.createTest(testData);
      console.log('🎯 새로 생성된 시험 데이터:', newTest);

      if (newTest && newTest.id) {
        toast.success('시험 의뢰가 성공적으로 생성되었습니다.');
        navigate(`/tests/${newTest.id}`);
      } else {
        console.warn('⚠️ 생성된 시험 데이터에 ID가 없습니다:', newTest);
        toast.success('시험 의뢰가 생성되었습니다.');
        navigate('/tests');
      }
    } catch (error) {
      console.error('시험 생성 중 오류:', error);
      toast.error('시험 의뢰 생성 중 오류가 발생했습니다.');
    } finally {
      setSubmitting(false);
    }
  };

  const handleCancel = () => {
    navigate('/tests');
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="max-w-2xl mx-auto"
    >
      {/* Header with back button */}
      <div className="flex items-center mb-6">
        <button
          onClick={handleCancel}
          className="flex items-center text-gray-600 hover:text-gray-900 transition-colors"
        >
          <ArrowLeft className="w-5 h-5 mr-2" />
          <span>시험 목록으로 돌아가기</span>
        </button>
      </div>

      <div className="bg-white rounded-xl shadow-sm p-8">
        <div className="text-center mb-8">
          <div className="w-16 h-16 bg-indigo-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <Plus className="w-8 h-8 text-indigo-600" />
          </div>
          <h1 className="text-2xl font-bold text-gray-900 mb-2">새 시험 의뢰</h1>
          <p className="text-gray-600">새로운 시험을 의뢰합니다</p>
        </div>

        {loading ? (
          <div className="flex items-center justify-center py-8">
            <Loader2 className="w-8 h-8 animate-spin text-indigo-600" />
            <span className="ml-3 text-gray-600">제품 목록을 불러오는 중...</span>
          </div>
        ) : (
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                시험 제품 <span className="text-red-500">*</span>
              </label>
              <select
                name="productId"
                value={formData.productId}
                onChange={handleInputChange}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              >
                <option value="">제품을 선택하세요</option>
                {products && products.length > 0 ? products.map(product => (
                  <option key={product.id} value={product.id}>
                    {product.name}
                  </option>
                )) : console.log('🚫 제품이 없거나 로딩 중:', { products, loading })}
              </select>
              {products && products.length === 0 && !loading && (
                <p className="mt-1 text-sm text-gray-500">등록된 제품이 없습니다.</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                시험 목적 <span className="text-red-500">*</span>
              </label>
              <input
                type="text"
                name="purpose"
                value={formData.purpose}
                onChange={handleInputChange}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                placeholder="시험 목적을 입력하세요"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                완료 예정일
              </label>
              <input
                type="datetime-local"
                name="expectedDate"
                value={formData.expectedDate}
                onChange={handleInputChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                특이사항
              </label>
              <textarea
                name="notes"
                value={formData.notes}
                onChange={handleInputChange}
                rows={4}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                placeholder="특별히 주의할 사항이나 요청사항을 입력하세요"
              />
            </div>

            <div className="flex justify-end space-x-3 pt-4">
              <button
                type="button"
                onClick={handleCancel}
                className="px-6 py-2 text-gray-700 bg-gray-100 rounded-lg hover:bg-gray-200 transition-colors"
                disabled={submitting}
              >
                취소
              </button>
              <button
                type="submit"
                disabled={submitting}
                className="px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center"
              >
                {submitting ? (
                  <>
                    <Loader2 className="w-4 h-4 animate-spin mr-2" />
                    처리 중...
                  </>
                ) : (
                  '시험 의뢰'
                )}
              </button>
            </div>
          </form>
        )}
      </div>
    </motion.div>
  );
};

export default CreateTest;