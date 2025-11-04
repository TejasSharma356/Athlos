import React, { useState, useEffect } from 'react';
import type { Screen } from '../types';
import { ChevronLeftIcon } from '../components/icons';
import { apiService, User } from '../src/services/api';
import { Avatar } from '../components/Avatar';

interface EditProfileScreenProps {
  onNavigate: (screen: Screen) => void;
  user: User | null;
  onUserUpdate?: (user: User) => void;
}

const EditProfileScreen: React.FC<EditProfileScreenProps> = ({ onNavigate, user, onUserUpdate }) => {
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (user) {
      setFullName(user.name || '');
      setEmail(user.email || '');
    }
  }, [user]);

  const handleSaveChanges = async () => {
    if (!user || !user.id) {
      setError('No user found');
      return;
    }

    setIsLoading(true);
    setError('');

    try {
      const updatedUser = await apiService.updateUser(user.id, {
        name: fullName,
        email: email,
      });
      
      // Update user in parent component
      if (onUserUpdate) {
        onUserUpdate(updatedUser);
      }
      
      // Update localStorage
      localStorage.setItem('athlos_user', JSON.stringify(updatedUser));
      
      onNavigate('profile');
    } catch (err: any) {
      setError(err.message || 'Failed to save changes. Please try again.');
      console.error('Error saving profile:', err);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="relative flex flex-col h-full text-white bg-[#0F172A]">
      {/* Background */}
       <div className="absolute inset-0 z-0">
        <img 
          src="https://images.unsplash.com/photo-1744706908605-ac30eb45f98c?q=80&w=2150&auto=format=fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
          alt="A runner on a road at dusk."
          className="w-full h-full object-cover"
        />
        <div className="absolute inset-0 bg-black/70"></div>
        <div className="absolute inset-0 bg-gradient-to-t from-[#0F172A] via-[#0F172A]/80 to-transparent"></div>
      </div>

      {/* Content */}
      <div className="relative z-10 flex flex-col h-full">
        <header className="relative flex justify-center items-center p-6 pt-12">
          <button onClick={() => onNavigate('profile')} className="absolute left-6 top-1/2 -translate-y-1/2 mt-6 p-2">
            <ChevronLeftIcon className="w-6 h-6" />
          </button>
          <h1 className="text-xl font-bold">Edit Profile</h1>
        </header>

        <main className="flex-grow p-6 overflow-y-auto">
          <section className="flex flex-col items-center text-center mb-8">
            <div className="mb-4">
              <Avatar name={user?.name} size={96} />
            </div>
            <p className="text-sm text-gray-400">Avatar shows your initials</p>
          </section>

          {error && (
            <div className="mb-4 p-3 bg-red-600/20 border border-red-600 rounded-lg text-red-400 text-center">
              {error}
            </div>
          )}

          <form className="space-y-6">
            <div>
              <label className="text-sm font-medium text-gray-400" htmlFor="full-name">Full Name</label>
              <input 
                id="full-name" 
                type="text" 
                value={fullName}
                onChange={(e) => setFullName(e.target.value)}
                className="w-full mt-2 p-4 bg-slate-800/80 rounded-lg border border-transparent focus:border-red-500 focus:outline-none focus:ring-1 focus:ring-red-500 transition" 
                placeholder="Your full name"
              />
            </div>
            <div>
              <label className="text-sm font-medium text-gray-400" htmlFor="email">Email</label>
              <input 
                id="email" 
                type="email" 
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full mt-2 p-4 bg-slate-800/80 rounded-lg border border-transparent focus:border-red-500 focus:outline-none focus:ring-1 focus:ring-red-500 transition" 
                placeholder="email@domain.com"
              />
            </div>
          </form>
        </main>

        <footer className="p-6">
          <button 
            onClick={handleSaveChanges}
            disabled={isLoading}
            className="w-full py-4 bg-red-600 rounded-lg font-semibold text-lg hover:bg-red-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {isLoading ? 'Saving...' : 'Save Changes'}
          </button>
        </footer>
      </div>
    </div>
  );
};

export default EditProfileScreen;
