import React, { useState } from 'react';
import type { Screen } from '../types';

interface SignUpScreenProps {
  onNavigate: (screen: Screen) => void;
  onSignUp: (name: string, email: string) => void;
}

const SignUpScreen: React.FC<SignUpScreenProps> = ({ onNavigate, onSignUp }) => {
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  return (
    <div className="relative flex flex-col h-full text-white">
       <div className="absolute inset-0 z-0">
        <img 
          src="https://images.unsplash.com/photo-1744706908605-ac30eb45f98c?q=80&w=2150&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
          alt="A runner on a road at dusk."
          className="w-full h-full object-cover"
        />
        <div className="absolute inset-0 bg-black/60"></div>
        <div className="absolute inset-0 bg-gradient-to-t from-[#0F172A] via-[#0F172A]/80 to-transparent"></div>
      </div>

      <div className="relative z-10 flex flex-col h-full p-8">
        <header className="text-center my-8">
          <h1 className="text-4xl font-bold tracking-wider">ATHLOS</h1>
        </header>

        <main className="flex-grow flex flex-col justify-center">
          <h2 className="text-3xl font-bold mb-8 text-center">Create Account</h2>
          
          <form className="space-y-6">
            <div>
              <label className="text-sm font-medium text-gray-400" htmlFor="full-name">Full Name</label>
              <input 
                id="full-name" 
                type="text" 
                value={fullName}
                onChange={(e) => setFullName(e.target.value)}
                className="w-full mt-2 p-4 bg-slate-800/80 rounded-lg border border-transparent focus:border-red-500 focus:ring-red-500 transition" 
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
                className="w-full mt-2 p-4 bg-slate-800/80 rounded-lg border border-transparent focus:border-red-500 focus:ring-red-500 transition" 
                placeholder="email@domain.com"
              />
            </div>
            <div>
              <label className="text-sm font-medium text-gray-400" htmlFor="password">Password</label>
              <input 
                id="password" 
                type="password" 
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full mt-2 p-4 bg-slate-800/80 rounded-lg border border-transparent focus:border-red-500 focus:ring-red-500 transition" 
                placeholder="********"
              />
            </div>
            
            <button 
              type="button"
              onClick={() => onSignUp(fullName, email)}
              className="w-full py-4 bg-red-600 rounded-lg font-semibold text-lg hover:bg-red-700 transition-colors"
            >
              Sign Up
            </button>
          </form>
        </main>

        <footer className="text-center py-4">
          <p className="text-gray-400">
            Already have an account?{' '}
            <button onClick={() => onNavigate('signin')} className="font-semibold text-red-400 hover:underline">
              Click here to Sign in
            </button>
          </p>
        </footer>
      </div>
    </div>
  );
};

export default SignUpScreen;
