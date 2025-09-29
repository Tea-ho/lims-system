import React from 'react';
import { motion } from 'framer-motion';
import { CheckCircle, XCircle, Clock, Users } from 'lucide-react';
import { ApprovalStatus } from '@/types';
import clsx from 'clsx';

interface BadgeProps {
  status: ApprovalStatus;
  size?: 'sm' | 'md' | 'lg';
  showIcon?: boolean;
  className?: string;
}

const Badge: React.FC<BadgeProps> = ({ 
  status, 
  size = 'md', 
  showIcon = true, 
  className 
}) => {
  const config = {
    PENDING: {
      label: '대기중',
      icon: Clock,
      className: 'bg-orange-50 text-orange-700 border-orange-200',
    },
    PARTIAL_APPROVED: {
      label: '부분승인',
      icon: Users,
      className: 'bg-blue-50 text-blue-700 border-blue-200',
    },
    APPROVED: {
      label: '승인완료',
      icon: CheckCircle,
      className: 'bg-green-50 text-green-700 border-green-200',
    },
    REJECTED: {
      label: '반려됨',
      icon: XCircle,
      className: 'bg-red-50 text-red-700 border-red-200',
    },
  };

  const sizeClasses = {
    sm: 'px-2 py-1 text-xs gap-1',
    md: 'px-3 py-1.5 text-sm gap-1.5',
    lg: 'px-4 py-2 text-base gap-2',
  };

  const iconSizes = {
    sm: 12,
    md: 14,
    lg: 16,
  };

  const statusConfig = config[status];
  const Icon = statusConfig.icon;

  const classes = clsx(
    'inline-flex items-center rounded-full font-medium border transition-all duration-200',
    statusConfig.className,
    sizeClasses[size],
    className
  );

  return (
    <motion.span 
      className={classes}
      initial={{ scale: 0 }}
      animate={{ scale: 1 }}
      transition={{ 
        type: "spring",
        stiffness: 500,
        damping: 30
      }}
    >
      {showIcon && <Icon size={iconSizes[size]} />}
      {statusConfig.label}
    </motion.span>
  );
};

export default Badge;
