import React from 'react';
import { motion } from 'framer-motion';
import { Shield } from 'lucide-react';

const AuditLogs: React.FC = () => {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="p-6"
    >
      <div className="text-center py-12">
        <Shield className="w-16 h-16 mx-auto text-gray-300 mb-4" />
        <h1 className="text-2xl font-bold text-gray-900 mb-4">감사 로그</h1>
        <p className="text-gray-600">시스템의 모든 활동을 추적하고 감사합니다.</p>
        <p className="text-sm text-gray-400 mt-2">구현 예정</p>
      </div>
    </motion.div>
  );
};

export default AuditLogs;