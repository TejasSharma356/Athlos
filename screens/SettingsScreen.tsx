import React, { useState } from 'react';
import type { Screen } from '../types';
import { ChevronLeftIcon, ChevronRightIcon, UserIcon, BellIcon, ShieldCheckIcon, HelpCircleIcon, DocumentTextIcon } from '../components/icons';

interface SettingsScreenProps {
  onNavigate: (screen: Screen) => void;
}

const SettingsScreen: React.FC<SettingsScreenProps> = ({ onNavigate }) => {
  const [notificationsEnabled, setNotificationsEnabled] = useState(true);

  const Toggle: React.FC<{ enabled: boolean; onToggle: () => void }> = ({ enabled, onToggle }) => (
    <button
      onClick={onToggle}
      className={`relative inline-flex items-center h-6 rounded-full w-11 transition-colors ${
        enabled ? 'bg-red-600' : 'bg-slate-600'
      }`}
    >
      <span
        className={`inline-block w-4 h-4 transform bg-white rounded-full transition-transform ${
          enabled ? 'translate-x-6' : 'translate-x-1'
        }`}
      />
    </button>
  );

  const SettingsSection: React.FC<{ title: string; children: React.ReactNode }> = ({ title, children }) => (
    <div className="mb-6">
      <h3 className="text-gray-400 text-sm font-semibold mb-2 px-4">{title}</h3>
      <div className="bg-slate-800 rounded-xl">
        {children}
      </div>
    </div>
  );
  
  // Fix: Corrected the type for the `icon` prop to ensure it accepts a `className`, resolving a TypeScript error with React.cloneElement.
  const SettingsItem: React.FC<{ icon: React.ReactElement<{ className?: string }>; label: string; action?: React.ReactNode; onClick?: () => void; isFirst?: boolean; isLast?: boolean }> = ({ icon, label, action, onClick, isFirst, isLast }) => (
    <div
      onClick={onClick}
      className={`flex items-center p-4 ${onClick ? 'cursor-pointer' : ''} ${!isLast ? 'border-b border-slate-700' : ''}`}
    >
      <div className="text-red-400 mr-4">{React.cloneElement(icon, { className: 'w-6 h-6' })}</div>
      <span className="flex-grow text-white">{label}</span>
      {action || (onClick && <ChevronRightIcon className="w-5 h-5 text-gray-500" />)}
    </div>
  );


  return (
    <div className="flex flex-col h-full text-white bg-[#0F172A]">
      <header className="relative flex justify-center items-center p-6 pt-12">
        <button onClick={() => onNavigate('home')} className="absolute left-6 top-1/2 -translate-y-1/2 mt-6 p-2">
          <ChevronLeftIcon className="w-6 h-6" />
        </button>
        <h1 className="text-xl font-bold">Settings</h1>
      </header>

      <main className="flex-grow p-6 overflow-y-auto">
        <SettingsSection title="ACCOUNT">
          <SettingsItem icon={<UserIcon />} label="Edit Profile" onClick={() => onNavigate('profile')} isFirst />
          <SettingsItem icon={<ShieldCheckIcon />} label="Privacy & Security" onClick={() => {}} isLast />
        </SettingsSection>

        <SettingsSection title="NOTIFICATIONS">
          <SettingsItem 
            icon={<BellIcon />} 
            label="Push Notifications" 
            action={<Toggle enabled={notificationsEnabled} onToggle={() => setNotificationsEnabled(!notificationsEnabled)} />}
            isFirst
            isLast
          />
        </SettingsSection>
        
        <SettingsSection title="SUPPORT">
          <SettingsItem icon={<HelpCircleIcon />} label="Help Center" onClick={() => {}} isFirst />
          <SettingsItem icon={<DocumentTextIcon />} label="Terms of Service" onClick={() => {}} isLast />
        </SettingsSection>

      </main>
    </div>
  );
};

export default SettingsScreen;