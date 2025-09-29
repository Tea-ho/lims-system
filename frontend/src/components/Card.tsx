import React from 'react';
import { motion } from 'framer-motion';
import clsx from 'clsx';

interface CardProps {
  children: React.ReactNode;
  className?: string;
  hover?: boolean;
  padding?: 'sm' | 'md' | 'lg';
  onClick?: () => void;
}

const Card: React.FC<CardProps> = ({ 
  children, 
  className, 
  hover = false, 
  padding = 'md',
  onClick 
}) => {
  const paddingClasses = {
    sm: 'p-4',
    md: 'p-6',
    lg: 'p-8',
  };

  const classes = clsx(
    'bg-white rounded-2xl border border-gray-100 shadow-sm',
    paddingClasses[padding],
    {
      'hover:shadow-lg hover:-translate-y-1 hover:border-primary-100 transition-all duration-300 cursor-pointer': hover,
      'cursor-pointer': onClick,
    },
    className
  );

  const MotionComponent = hover || onClick ? motion.div : 'div';
  const motionProps = hover || onClick ? {
    whileHover: { y: -4, scale: 1.02 },
    whileTap: { scale: 0.98 },
    transition: { duration: 0.2 }
  } : {};

  return (
    <MotionComponent 
      className={classes} 
      onClick={onClick}
      {...motionProps}
    >
      {children}
    </MotionComponent>
  );
};

export default Card;
