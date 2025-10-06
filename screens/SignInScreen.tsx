import React, { useState } from 'react';
import type { Screen } from '../types';
import { GoogleIcon, AppleIcon } from '../components/icons';
import { apiService } from '../src/services/api';

interface SignInScreenProps {
  onNavigate: (screen: Screen) => void;
  onUserLogin: (user: any) => void;
  onGuestLogin: () => void;
}

const SignInScreen: React.FC<SignInScreenProps> = ({ onNavigate, onUserLogin, onGuestLogin }) => {
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.id]: e.target.value
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError('');

    try {
      const user = await apiService.login(formData.email, formData.password);
      localStorage.removeItem('athlos_is_guest');
      onUserLogin(user);
      onNavigate('home');
    } catch (err) {
      setError('Invalid email or password. Please try again.');
      console.error('Login error:', err);
    } finally {
      setIsLoading(false);
    }
  };
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
          <h2 className="text-3xl font-bold mb-8 text-center">Sign In</h2>
          
          {error && (
            <div className="mb-4 p-3 bg-red-600/20 border border-red-600 rounded-lg text-red-400 text-center">
              {error}
            </div>
          )}
          
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label className="text-sm font-medium text-gray-400" htmlFor="email">Email</label>
              <input 
                id="email" 
                type="email" 
                value={formData.email}
                onChange={handleInputChange}
                required
                className="w-full mt-2 p-4 bg-slate-800/80 rounded-lg border border-transparent focus:border-red-500 focus:ring-red-500 transition" 
                placeholder="email@domain.com"
              />
            </div>
            <div>
              <label className="text-sm font-medium text-gray-400" htmlFor="password">Password</label>
              <input 
                id="password" 
                type="password" 
                value={formData.password}
                onChange={handleInputChange}
                required
                className="w-full mt-2 p-4 bg-slate-800/80 rounded-lg border border-transparent focus:border-red-500 focus:ring-red-500 transition" 
                placeholder="********"
              />
            </div>
            
            <button 
              type="submit"
              disabled={isLoading}
              className="w-full py-4 bg-red-600 rounded-lg font-semibold text-lg hover:bg-red-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {isLoading ? 'Signing In...' : 'Sign In'}
            </button>
          </form>

          <div className="flex items-center my-8">
            <hr className="flex-grow border-gray-700" />
            <span className="px-4 text-gray-400">or</span>
            <hr className="flex-grow border-gray-700" />
          </div>

          <div className="space-y-4">
            <button className="w-full py-4 bg-slate-800/80 rounded-lg font-semibold flex items-center justify-center hover:bg-slate-700/80 transition">
              <GoogleIcon className="w-6 h-6 mr-3" />
              Continue with Google
            </button>
            <button className="w-full py-4 bg-slate-800/80 rounded-lg font-semibold flex items-center justify-center hover:bg-slate-700/80 transition">
              <AppleIcon className="w-6 h-6 mr-3 text-white" />
              Continue with Apple
            </button>
            <button onClick={onGuestLogin} className="w-full py-4 bg-red-600 rounded-lg font-semibold flex items-center justify-center hover:bg-red-700 transition">
              Continue as Guest
            </button>
          </div>
        </main>

        <footer className="text-center py-4">
          <p className="text-gray-400">
            Don't have an account?{' '}
            <button onClick={() => onNavigate('signup')} className="font-semibold text-red-400 hover:underline">
              Sign up
            </button>
          </p>
        </footer>
      </div>
    </div>
  );
};

export default SignInScreen;