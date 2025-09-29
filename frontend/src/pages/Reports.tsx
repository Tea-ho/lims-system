import React from 'react';
import { motion } from 'framer-motion';
import { FileBarChart } from 'lucide-react';

const Reports: React.FC = () => {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="p-6"
    >
      <div className="text-center py-12">
        <FileBarChart className="w-16 h-16 mx-auto text-gray-300 mb-4" />
        <h1 className="text-2xl font-bold text-gray-900 mb-4">보고서 페이지</h1>
        <p className="text-gray-600">상세한 분석 보고서를 생성하고 관리합니다.</p>
        <p className="text-sm text-gray-400 mt-2">구현 예정</p>
      </div>
    </motion.div>
  );
};

export default Reports;