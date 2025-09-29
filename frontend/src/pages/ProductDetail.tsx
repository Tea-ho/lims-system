import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import {
  ArrowLeft,
  Package,
  Calendar,
  User,
  FileText,
  Edit,
  AlertTriangle,
  CheckCircle,
  Clock,
  Plus
} from 'lucide-react';
import { Product, Test } from '@/types';
import { ProductService } from '@/services/productService';
import { TestService } from '@/services/testService';
import LoadingSpinner from '@/components/LoadingSpinner';

const ProductDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [product, setProduct] = useState<Product | null>(null);
  const [relatedTests, setRelatedTests] = useState<Test[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [testsLoading, setTestsLoading] = useState(false);

  useEffect(() => {
    const fetchProductAndTests = async () => {
      if (!id) {
        setError('제품 ID가 없습니다.');
        setLoading(false);
        return;
      }

      try {
        setLoading(true);

        // 제품 정보 조회
        const productData = await ProductService.getProductById(Number(id));
        setProduct(productData);

        // 관련 시험 조회 - 백엔드에서 직접 제품별 필터링
        setTestsLoading(true);
        const testsData = await TestService.searchTests({ productId: Number(id) });
        setRelatedTests(testsData);

        setError(null);
      } catch (err) {
        console.error('제품 상세 정보 로드 실패:', err);
        setError('제품 상세 정보를 불러오는데 실패했습니다.');
      } finally {
        setLoading(false);
        setTestsLoading(false);
      }
    };

    fetchProductAndTests();
  }, [id]);

  const getTestStageInfo = (stage: string) => {
    switch (stage) {
      case 'REQUEST':
        return { color: 'bg-blue-100 text-blue-800', label: '의뢰' };
      case 'RECEIPT':
        return { color: 'bg-yellow-100 text-yellow-800', label: '접수' };
      case 'RECEIPT_APPROVAL':
        return { color: 'bg-orange-100 text-orange-800', label: '접수 승인' };
      case 'RESULT_INPUT':
        return { color: 'bg-purple-100 text-purple-800', label: '결과 입력' };
      case 'COMPLETED':
        return { color: 'bg-green-100 text-green-800', label: '완료' };
      default:
        return { color: 'bg-gray-100 text-gray-800', label: '알 수 없음' };
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-96">
        <LoadingSpinner />
      </div>
    );
  }

  if (error) {
    return (
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="p-6"
      >
        <div className="text-center py-12">
          <AlertTriangle className="mx-auto h-12 w-12 text-red-400" />
          <h3 className="mt-2 text-sm font-medium text-gray-900">오류 발생</h3>
          <p className="mt-1 text-sm text-gray-500">{error}</p>
          <div className="mt-6">
            <button
              type="button"
              onClick={() => navigate('/products')}
              className="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              <ArrowLeft className="-ml-1 mr-2 h-4 w-4" />
              제품 목록으로 돌아가기
            </button>
          </div>
        </div>
      </motion.div>
    );
  }

  if (!product) {
    return (
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="p-6"
      >
        <div className="text-center py-12">
          <Package className="mx-auto h-12 w-12 text-gray-400" />
          <h3 className="mt-2 text-sm font-medium text-gray-900">제품을 찾을 수 없습니다</h3>
          <p className="mt-1 text-sm text-gray-500">요청하신 제품이 존재하지 않습니다.</p>
        </div>
      </motion.div>
    );
  }

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="p-6 max-w-7xl mx-auto"
    >
      {/* Header */}
      <div className="mb-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <button
              onClick={() => navigate('/products')}
              className="inline-flex items-center px-3 py-2 border border-gray-300 shadow-sm text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              <ArrowLeft className="-ml-1 mr-2 h-4 w-4" />
              뒤로
            </button>
            <div>
              <h1 className="text-2xl font-bold text-gray-900">제품 상세 정보</h1>
              <p className="text-sm text-gray-500">제품 ID: {product.id}</p>
            </div>
          </div>
          <div className="flex items-center space-x-3">
            <button
              onClick={() => navigate(`/tests/new?productId=${product.id}`)}
              className="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
            >
              <Plus className="-ml-1 mr-2 h-4 w-4" />
              시험 의뢰
            </button>
            <button
              onClick={() => navigate(`/products/${product.id}/edit`)}
              className="inline-flex items-center px-4 py-2 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              <Edit className="-ml-1 mr-2 h-4 w-4" />
              수정
            </button>
          </div>
        </div>
      </div>

      {/* 제품 기본 정보 */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
        <div className="lg:col-span-2">
          <div className="bg-white shadow rounded-lg">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-lg font-medium text-gray-900 flex items-center">
                <Package className="w-5 h-5 mr-2 text-blue-500" />
                기본 정보
              </h2>
            </div>
            <div className="px-6 py-4">
              <dl className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div className="sm:col-span-2">
                  <dt className="text-sm font-medium text-gray-500">제품명</dt>
                  <dd className="mt-1 text-lg font-semibold text-gray-900">{product.name}</dd>
                </div>
                <div className="sm:col-span-2">
                  <dt className="text-sm font-medium text-gray-500">설명</dt>
                  <dd className="mt-1 text-sm text-gray-900 whitespace-pre-wrap">
                    {product.description || '설명이 없습니다.'}
                  </dd>
                </div>
                <div>
                  <dt className="text-sm font-medium text-gray-500">생성일시</dt>
                  <dd className="mt-1 text-sm text-gray-900 flex items-center">
                    <Calendar className="w-4 h-4 mr-1 text-gray-400" />
                    {new Date(product.createdAt).toLocaleString('ko-KR')}
                  </dd>
                </div>
                <div>
                  <dt className="text-sm font-medium text-gray-500">최종 수정일시</dt>
                  <dd className="mt-1 text-sm text-gray-900 flex items-center">
                    <Calendar className="w-4 h-4 mr-1 text-gray-400" />
                    {product.updatedAt ? new Date(product.updatedAt).toLocaleString('ko-KR') : '-'}
                  </dd>
                </div>
              </dl>
            </div>
          </div>
        </div>

        {/* 시험 통계 */}
        <div>
          <div className="bg-white shadow rounded-lg">
            <div className="px-6 py-4 border-b border-gray-200">
              <h2 className="text-lg font-medium text-gray-900">시험 통계</h2>
            </div>
            <div className="px-6 py-4">
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-600">전체 시험</span>
                  <span className="text-lg font-semibold text-gray-900">{relatedTests.length}건</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-600">진행 중</span>
                  <span className="text-lg font-semibold text-blue-600">
                    {relatedTests.filter(test => test.stage !== 'COMPLETED').length}건
                  </span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-600">완료</span>
                  <span className="text-lg font-semibold text-green-600">
                    {relatedTests.filter(test => test.stage === 'COMPLETED').length}건
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* 관련 시험 목록 */}
      <div className="bg-white shadow rounded-lg">
        <div className="px-6 py-4 border-b border-gray-200">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-medium text-gray-900 flex items-center">
              <FileText className="w-5 h-5 mr-2 text-green-500" />
              관련 시험 목록
            </h2>
            <button
              onClick={() => navigate(`/tests/new?productId=${product.id}`)}
              className="inline-flex items-center px-3 py-2 border border-gray-300 shadow-sm text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
            >
              <Plus className="-ml-1 mr-2 h-4 w-4" />
              새 시험 의뢰
            </button>
          </div>
        </div>
        <div className="px-6 py-4">
          {testsLoading ? (
            <div className="flex justify-center py-8">
              <LoadingSpinner />
            </div>
          ) : relatedTests.length === 0 ? (
            <div className="text-center py-8">
              <FileText className="mx-auto h-12 w-12 text-gray-300" />
              <h3 className="mt-2 text-sm font-medium text-gray-900">관련 시험이 없습니다</h3>
              <p className="mt-1 text-sm text-gray-500">이 제품에 대한 시험 의뢰가 아직 없습니다.</p>
              <div className="mt-6">
                <button
                  onClick={() => navigate(`/tests/new?productId=${product.id}`)}
                  className="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                >
                  <Plus className="-ml-1 mr-2 h-4 w-4" />
                  첫 번째 시험 의뢰하기
                </button>
              </div>
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      시험 정보
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      의뢰자
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      진행 상태
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      생성일
                    </th>
                    <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                      액션
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {relatedTests.map((test) => {
                    const stageInfo = getTestStageInfo(test.stage);
                    return (
                      <tr key={test.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="flex items-center">
                            <FileText className="h-5 w-5 text-gray-400 mr-3" />
                            <div>
                              <div className="text-sm font-medium text-gray-900">
                                {test.requestInfo?.title || `시험 #${test.id}`}
                              </div>
                              <div className="text-sm text-gray-500">ID: {test.id}</div>
                            </div>
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="flex items-center text-sm text-gray-900">
                            <User className="h-4 w-4 text-gray-400 mr-1" />
                            {test.user?.username || '알 수 없음'}
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${stageInfo.color}`}>
                            {stageInfo.label}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {new Date(test.createdAt).toLocaleDateString('ko-KR')}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                          <button
                            onClick={() => navigate(`/tests/${test.id}`)}
                            className="text-blue-600 hover:text-blue-900"
                          >
                            상세보기
                          </button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </motion.div>
  );
};

export default ProductDetail;