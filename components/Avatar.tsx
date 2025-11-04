import React from 'react';

/**
 * Generate initials from a user's name
 */
export function getInitials(name: string | undefined | null): string {
  if (!name) return 'U';
  
  const parts = name.trim().split(/\s+/);
  if (parts.length >= 2) {
    return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
  }
  return name.substring(0, 2).toUpperCase();
}

interface AvatarProps {
  name?: string | null;
  size?: number;
  className?: string;
}

/**
 * Avatar component that displays user initials
 */
export const Avatar: React.FC<AvatarProps> = ({ name, size = 40, className = '' }) => {
  const initials = getInitials(name);
  const sizeStyle = { width: size, height: size };
  
  return (
    <div
      className={`rounded-full flex items-center justify-center font-bold text-white ${className}`}
      style={{
        ...sizeStyle,
        backgroundColor: '#ef4444',
        fontSize: `${Math.floor(size * 0.4)}px`,
      }}
    >
      {initials}
    </div>
  );
};
