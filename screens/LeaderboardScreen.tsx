import React, { useState, useMemo } from 'react';
import type { Screen, LeaderboardUser } from '../types';
import { ChevronLeftIcon, SearchIcon } from '../components/icons';
import { getLeaderboardUsers } from './userService';

// Fix: Added LeaderboardScreenProps interface to define component props.
interface LeaderboardScreenProps {
  onNavigate: (screen: Screen) => void;
}

const LeaderboardScreen: React.FC<LeaderboardScreenProps> = ({ onNavigate }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const allUsers = useMemo(() => getLeaderboardUsers(), []);

  const filteredUsers = useMemo(() => {
    if (!allUsers) return [];
    return allUsers.filter(user =>
      user.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [allUsers, searchTerm]);

  const getMedalSymbol = (rank: number) => {
    if (rank === 1) return '🥇';
    if (rank === 2) return '🥈';
    if (rank === 3) return '🥉';
    return null;
  };
  
  const getRankClass = (rank: number) => {
    if (rank === 1) return 'text-yellow-400';
    if (rank === 2) return 'text-gray-300';
    if (rank === 3) return 'text-orange-400';
    return 'text-gray-500';
  };

  return (
    <div className="flex flex-col h-full text-white bg-[#0F172A]">
      <header className="relative flex justify-center items-center p-6 pt-12 border-b border-slate-800">
        <button onClick={() => onNavigate('home')} className="absolute left-6 top-1/2 -translate-y-1/2 mt-6 p-2">
          <ChevronLeftIcon className="w-6 h-6" />
        </button>
        <h1 className="text-xl font-bold">Leaderboard</h1>
      </header>

      <div className="p-6">
        <div className="relative">
          <input
            type="text"
            placeholder="Search for a player..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full p-4 pl-12 bg-slate-800 rounded-lg border border-transparent focus:border-red-500 focus:ring-red-500 transition"
          />
          <SearchIcon className="absolute left-4 top-1/2 -translate-y-1/2 w-6 h-6 text-gray-400" />
        </div>
      </div>

      <main className="flex-grow overflow-y-auto px-6 pb-6">
        {filteredUsers.length > 0 ? (
          <ul className="space-y-2">
            {filteredUsers.map((user) => (
              <li key={user.rank} className={`flex items-center p-3 bg-slate-800 rounded-lg hover:bg-slate-700/50 transition ${user.name === 'Alex Johnson' ? 'border-2 border-red-500' : ''}`}>
                <span className={`w-8 text-center text-lg font-bold ${getRankClass(user.rank)}`}>
                  {user.rank}
                </span>
                <img src={user.avatar} alt={user.name} className="w-10 h-10 rounded-full mx-4" />
                <div className="flex-grow">
                  <span className="font-semibold">{user.name}</span>
                  <span className="text-lg ml-2">{getMedalSymbol(user.rank)}</span>
                </div>
                <span className="text-gray-300 font-medium">{user.steps.toLocaleString()} steps</span>
              </li>
            ))}
          </ul>
        ) : (
          <div className="text-center py-10">
            <p className="text-gray-400">No players found.</p>
          </div>
        )}
      </main>
    </div>
  );
};

export default LeaderboardScreen;
