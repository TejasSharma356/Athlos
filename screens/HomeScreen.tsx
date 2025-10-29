import React, { useState, useRef, useEffect, useMemo } from 'react';
import type { Screen, User, LeaderboardUser } from '../types';
import { HomeIcon, ChartIcon, BellIcon, UserIcon, SettingsIcon, LogoutIcon, UserPlusIcon } from '../components/icons';
import Avatar from '../components/Avatar';
import { getLeaderboardUsers, getCurrentUserData, getFriendSuggestions } from './userService';

const LiquidProgress = ({ progress }: { progress: number }) => {
  // The SVG height is 200. Progress will map from 0 to 100.
  // When progress is 0, we want translateY to be 200 (fully hidden at bottom).
  // When progress is 100, we want translateY to be 0 (fully visible at top).
  const yPosition = (100 - progress) * 2; // (100 - progress) / 100 * 200

  return (
    <div className="absolute bottom-0 left-0 w-full h-full">
      <svg width="100%" height="100%" viewBox="0 0 400 200" preserveAspectRatio="none">
        <g style={{ transform: `translateY(${yPosition}px)` }} className="transition-transform duration-1000 ease-in-out">
          {/* Wave 1 - Slower, less opaque */}
          <path
            d="M -400 100 C -300 50, -200 50, -100 100 S 0 150, 100 100 S 200 50, 300 100 S 400 150, 500 100 S 600 50, 700 100 S 800 150, 900 100 V 300 H -400 Z"
            fill="rgba(255, 255, 255, 0.2)"
            className="animate-wave-slow"
          />
          {/* Wave 2 - Faster, more opaque */}
          <path
            d="M -400 110 C -300 60, -200 60, -100 110 S 0 160, 100 110 S 200 60, 300 110 S 400 160, 500 110 S 600 60, 700 110 S 800 160, 900 110 V 300 H -400 Z"
            fill="rgba(255, 255, 255, 0.3)"
            className="animate-wave-fast"
          />
        </g>
      </svg>
    </div>
  );
};


const HomeScreen: React.FC<{ onNavigate: (screen: Screen) => void; user: User; onLogout: () => void; }> = ({ onNavigate, user, onLogout }) => {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

  const userName = user.name;
  const userAvatarSrc = user.avatar;

  // Data is fetched from the user service
  const allUsers = useMemo(() => getLeaderboardUsers(), []);
  const topThreeUsers = useMemo(() => allUsers.slice(0, 3), [allUsers]);
  const currentUserData = useMemo(() => getCurrentUserData(userName), [userName]);
  const friendSuggestions = useMemo(() => getFriendSuggestions(userName, topThreeUsers), [userName, topThreeUsers]);
  
  const currentSteps = currentUserData ? currentUserData.steps : 0;
  const goalSteps = 10000;
  const progress = Math.min((currentSteps / goalSteps) * 100, 100);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsDropdownOpen(false);
      }
    };

    if (isDropdownOpen) {
      document.addEventListener('mousedown', handleClickOutside);
    }

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [isDropdownOpen]);

  const getRankClass = (index: number) => {
    switch (index) {
      case 0: return 'text-yellow-400';
      case 1: return 'text-gray-300';
      case 2: return 'text-orange-400';
      default: return 'text-gray-500';
    }
  };

  const getMedalSymbol = (index: number) => {
    switch (index) {
      case 0: return '🥇';
      case 1: return '🥈';
      case 2: return '🥉';
      default: return null;
    }
  };

  return (
    <div className="flex flex-col h-full text-white bg-[#0F172A]">
        <style>{`
          @keyframes wave-slow {
            0% { transform: translateX(0); }
            100% { transform: translateX(-400px); }
          }
          .animate-wave-slow {
            animation: wave-slow 10s linear infinite;
          }

          @keyframes wave-fast {
            0% { transform: translateX(0); }
            100% { transform: translateX(-400px); }
          }
          .animate-wave-fast {
            animation: wave-fast 6s linear infinite;
          }
        `}</style>
      <header className="flex justify-between items-center p-6 pt-12">
        <div>
          <h1 className="text-2xl font-bold">Home</h1>
        </div>
        <div className="flex items-center space-x-4">
          <span className="text-lg font-semibold">Athlos</span>
          <div className="relative">
            <div onClick={() => setIsDropdownOpen(!isDropdownOpen)} className="cursor-pointer">
              <Avatar
                name={userName}
                src={userAvatarSrc}
                className="w-10 h-10 rounded-full"
                textClassName="text-lg"
              />
            </div>
            {isDropdownOpen && (
              <div ref={dropdownRef} className="absolute right-0 mt-2 w-48 bg-slate-800 rounded-lg shadow-lg py-2 z-20 origin-top-right animate-in fade-in-20 slide-in-from-top-2">
                <button
                  onClick={() => {
                    setIsDropdownOpen(false);
                    onNavigate('profile');
                  }}
                  className="w-full flex items-center text-left px-4 py-2 text-sm text-gray-300 hover:bg-red-600 hover:text-white rounded-t-lg"
                >
                  <UserIcon className="w-5 h-5 mr-3" />
                  Profile
                </button>
                <button
                  onClick={() => {
                    setIsDropdownOpen(false);
                    onNavigate('settings');
                  }}
                  className="w-full flex items-center text-left px-4 py-2 text-sm text-gray-300 hover:bg-red-600 hover:text-white"
                >
                  <SettingsIcon className="w-5 h-5 mr-3" />
                  Settings
                </button>
                <div className="border-t border-gray-700 my-1"></div>
                <button
                  onClick={() => {
                    setIsDropdownOpen(false);
                    onLogout();
                  }}
                  className="w-full flex items-center text-left px-4 py-2 text-sm text-red-400 hover:bg-red-500 hover:text-white rounded-b-lg"
                >
                  <LogoutIcon className="w-5 h-5 mr-3" />
                  Logout
                </button>
              </div>
            )}
          </div>
        </div>
      </header>

      <main className="flex-grow p-6 overflow-y-auto">
        <section className="mb-8">
          <div className="bg-gradient-to-br from-red-500 to-orange-500 p-6 rounded-2xl shadow-lg relative overflow-hidden h-[150px] flex flex-col justify-center">
            <LiquidProgress progress={progress} />
            <div className="relative z-10">
                <h3 className="text-lg font-semibold text-red-200">Steps</h3>
                <p className="text-4xl font-bold my-1">{currentSteps.toLocaleString()} / <span className="text-2xl font-normal text-red-200">{goalSteps.toLocaleString()}</span></p>
                <p className="text-sm text-red-200">You have covered {Math.round(progress)}% of the goal</p>
            </div>
          </div>
        </section>

        <section className="mb-8">
          <h2 className="text-xl font-bold mb-4">Leaderboard</h2>
          <div className="bg-slate-800 p-4 rounded-2xl">
            {topThreeUsers.length > 0 ? (
              <ul className="space-y-4">
                {topThreeUsers.map((user, index) => (
                  <li key={index} className="flex items-center justify-between">
                    <div className="flex items-center">
                      <span className={`w-8 text-center text-xl font-bold ${getRankClass(index)}`}>{index + 1}</span>
                      <img src={user.avatar} alt={user.name} className="w-10 h-10 rounded-full mx-4" />
                      <span className="text-xl mr-2">{getMedalSymbol(index)}</span>
                      <span className="font-semibold">{user.name}</span>
                    </div>
                    <span className="text-gray-400">{user.steps.toLocaleString()} steps</span>
                  </li>
                ))}
              </ul>
            ) : (
              <p className="text-center text-gray-400 py-4">Leaderboard data is not available yet.</p>
            )}
          </div>
        </section>

        <section className="mb-8">
          <h2 className="text-xl font-bold mb-4">Connect with Friends</h2>
          {friendSuggestions.length > 0 ? (
            <div className="grid grid-cols-2 gap-4">
              {friendSuggestions.map((friend) => (
                <div key={friend.id} className="bg-slate-800 p-4 rounded-2xl flex flex-col items-center text-center">
                  <img src={friend.avatar} alt={friend.name} className="w-16 h-16 rounded-full mb-3" />
                  <span className="font-semibold text-sm mb-3 flex-grow">{friend.name}</span>
                  <button className="w-full flex items-center justify-center py-2 bg-slate-700 rounded-lg text-sm font-semibold hover:bg-slate-600 transition">
                    <UserPlusIcon className="w-4 h-4 mr-2" />
                    Connect
                  </button>
                </div>
              ))}
            </div>
           ) : (
            <div className="bg-slate-800 p-4 rounded-2xl text-center text-gray-400">
              <p>No friend suggestions at the moment.</p>
            </div>
          )}
        </section>

        <button 
          onClick={() => onNavigate('disclaimer')}
          className="w-full py-5 bg-red-600 rounded-2xl font-bold text-lg hover:bg-red-700 transition-colors shadow-lg"
        >
          Start RUN
        </button>

      </main>
      
      <footer className="w-full bg-slate-800 p-4 rounded-t-3xl">
        <nav className="flex justify-around items-center">
          <button className="p-2 text-red-400">
            <HomeIcon className="w-8 h-8" />
          </button>
          <button onClick={() => onNavigate('leaderboard')} className="p-2 text-gray-500 hover:text-red-400 transition-colors">
            <ChartIcon className="w-8 h-8" />
          </button>
          <button className="p-2 text-gray-500 hover:text-red-400 transition-colors">
            <BellIcon className="w-8 h-8" />
          </button>
        </nav>
      </footer>
    </div>
  );
};

export default HomeScreen;
