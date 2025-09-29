import React from 'react';
import { motion } from 'framer-motion';
import { Users as UsersIcon, UserPlus, Search } from 'lucide-react';

const Users: React.FC = () => {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="p-6"
    >
      <div className="mb-8">
        <div className="flex items-center mb-4">
          <UsersIcon className="w-8 h-8 text-indigo-600 mr-3" />
          <h1 className="text-3xl font-bold text-gray-900">사용자 관리</h1>
        </div>
        <p className="text-gray-600">시스템 사용자를 관리합니다.</p>
      </div>

      <div className="bg-white rounded-lg shadow p-6">
        <div className="flex justify-between items-center mb-6">
          <div className="flex items-center space-x-4">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
              <input
                type="text"
                placeholder="사용자 검색..."
                className="pl-10 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
            </div>
          </div>
          <button className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 flex items-center space-x-2">
            <UserPlus className="w-5 h-5" />
            <span>사용자 추가</span>
          </button>
        </div>

        <div className="text-center py-12 text-gray-500">
          <UsersIcon className="w-16 h-16 mx-auto mb-4 text-gray-300" />
          <p className="text-lg">사용자 관리 기능을 구현해주세요.</p>
          <p className="text-sm mt-2">이 페이지는 임시로 생성된 페이지입니다.</p>
        </div>
      </div>
    </motion.div>
  );
};

export default Users;
