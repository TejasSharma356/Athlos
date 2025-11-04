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

/**
 * Generate a data URL for an avatar with initials
 */
export function generateInitialsAvatar(name: string | undefined | null, size: number = 40): string {
  const initials = getInitials(name);
  const canvas = document.createElement('canvas');
  canvas.width = size;
  canvas.height = size;
  const ctx = canvas.getContext('2d');
  
  if (!ctx) return '';
  
  // Red background (app color #ef4444)
  ctx.fillStyle = '#ef4444';
  ctx.fillRect(0, 0, size, size);
  
  // White text
  ctx.fillStyle = '#ffffff';
  ctx.font = `bold ${Math.floor(size * 0.4)}px Arial`;
  ctx.textAlign = 'center';
  ctx.textBaseline = 'middle';
  ctx.fillText(initials, size / 2, size / 2);
  
  return canvas.toDataURL();
}

/**
 * Avatar component that shows initials
 */
export function AvatarComponent({ name, size = 40, className = '' }: { name?: string | null; size?: number; className?: string }) {
  const avatarUrl = generateInitialsAvatar(name, size);
  return (
    <img 
      src={avatarUrl}
      alt={name || 'User'}
      className={`rounded-full ${className}`}
      style={{ width: size, height: size }}
    />
  );
}
