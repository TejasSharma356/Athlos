import React, { useState } from 'react';
import type { Screen, User } from '../types';
import { ChevronLeftIcon } from '../components/icons';
import Avatar from '../components/Avatar';

interface EditProfileScreenProps {
  onNavigate: (screen: Screen) => void;
  user: User;
  onSave: (name: string, email: string) => void;
}

const EditProfileScreen: React.FC<EditProfileScreenProps> = ({ onNavigate, user, onSave }) => {
  const [fullName, setFullName] = useState(user.name);
  const [email, setEmail] = useState(user.email);
  const userAvatarSrc = user.avatar;

  const handleSaveChanges = () => {
    onSave(fullName, email);
    onNavigate('profile');
  };

  return (
    <div className="relative flex flex-col h-full text-white bg-[#0F172A]">
      {/* Background */}
       <div className="absolute inset-0 z-0">
        <img 
          src="https://images.unsplash.com/photo-1744706908605-ac30eb45f98c?q=80&w=2150&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
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
            <div className="relative">
              <Avatar
                name={fullName}
                src={userAvatarSrc}
                className="w-24 h-24 rounded-full mb-2 border-4 border-slate-700"
                textClassName="text-4xl"
              />
              <button className="absolute bottom-2 -right-1 bg-red-600 p-2 rounded-full hover:bg-red-700 transition">
                <svg className="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.5L14.732 3.732z"></path></svg>
              </button>
            </div>
            <button className="mt-2 text-red-400 font-semibold hover:underline">
              Change Photo
            </button>
          </section>

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
            className="w-full py-4 bg-red-600 rounded-lg font-semibold text-lg hover:bg-red-700 transition-colors"
          >
            Save Changes
          </button>
        </footer>
      </div>
    </div>
  );
};

export default EditProfileScreen;
