import React from 'react';

const getInitials = (name: string = ''): string => {
  const parts = name.trim().split(' ').filter(Boolean);
  if (parts.length === 0) return '??';
  if (parts.length > 1) {
    return `${parts[0][0]}${parts[parts.length - 1][0]}`.toUpperCase();
  }
  if (parts[0].length > 1) {
    return parts[0].substring(0, 2).toUpperCase();
  }
  return parts[0].toUpperCase();
};

interface AvatarProps {
  name: string;
  src?: string;
  className?: string;
  textClassName?: string;
}

const Avatar: React.FC<AvatarProps> = ({ name, src, className, textClassName }) => {
  if (src) {
    return <img src={src} alt={name} className={className} />;
  }

  const initials = getInitials(name);

  return (
    <div className={`flex items-center justify-center bg-slate-700 text-white font-bold ${className}`}>
      <span className={textClassName}>{initials}</span>
    </div>
  );
};

export default Avatar;
