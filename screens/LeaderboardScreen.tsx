import React, { useState, useEffect, useMemo } from 'react';
import type { Screen } from '../types';
import { ChevronLeftIcon, SearchIcon } from '../components/icons';
import { apiService, LeaderboardEntry } from '../src/services/api';
import { websocketService } from '../src/services/websocket';
import { Avatar } from '../components/Avatar';

interface LeaderboardScreenProps {
  onNavigate: (screen: Screen) => void;
  user: any;
}

const LeaderboardScreen: React.FC<LeaderboardScreenProps> = ({ onNavigate, user }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [leaderboardData, setLeaderboardData] = useState<LeaderboardEntry[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [activeTab, setActiveTab] = useState<'daily' | 'weekly' | 'all-time'>('daily');

  const filteredUsers = useMemo(() => {
    return leaderboardData.filter(entry =>
      entry.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [leaderboardData, searchTerm]);

  useEffect(() => {
    const loadLeaderboard = async () => {
      try {
        setIsLoading(true);
        let data: LeaderboardEntry[];
        
        switch (activeTab) {
          case 'daily':
            data = await apiService.getDailyLeaderboard();
            break;
          case 'weekly':
            data = await apiService.getWeeklyLeaderboard();
            break;
          case 'all-time':
            data = await apiService.getAllTimeLeaderboard();
            break;
          default:
            data = await apiService.getDailyLeaderboard();
        }
        
        setLeaderboardData(data);
      } catch (error) {
        console.error('Error loading leaderboard:', error);
      } finally {
        setIsLoading(false);
      }
    };

    loadLeaderboard();
  }, [activeTab]);

  useEffect(() => {
    // Connect to WebSocket for real-time updates
    websocketService.connect().then(() => {
      websocketService.subscribeToDailyLeaderboard((data) => {
        if (activeTab === 'daily') {
          setLeaderboardData(data);
        }
      });
      websocketService.subscribeToWeeklyLeaderboard((data) => {
        if (activeTab === 'weekly') {
          setLeaderboardData(data);
        }
      });
      websocketService.subscribeToAllTimeLeaderboard((data) => {
        if (activeTab === 'all-time') {
          setLeaderboardData(data);
        }
      });
    }).catch((error) => {
      console.error('Leaderboard WebSocket connection failed:', error);
    });

    return () => {
      websocketService.disconnect();
    };
  }, [activeTab]);

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

      {/* Tab Navigation */}
      <div className="flex border-b border-slate-800">
        <button
          onClick={() => setActiveTab('daily')}
          className={`flex-1 py-3 px-4 text-sm font-medium ${
            activeTab === 'daily' 
              ? 'text-red-400 border-b-2 border-red-400' 
              : 'text-gray-400 hover:text-white'
          }`}
        >
          Daily
        </button>
        <button
          onClick={() => setActiveTab('weekly')}
          className={`flex-1 py-3 px-4 text-sm font-medium ${
            activeTab === 'weekly' 
              ? 'text-red-400 border-b-2 border-red-400' 
              : 'text-gray-400 hover:text-white'
          }`}
        >
          Weekly
        </button>
        <button
          onClick={() => setActiveTab('all-time')}
          className={`flex-1 py-3 px-4 text-sm font-medium ${
            activeTab === 'all-time' 
              ? 'text-red-400 border-b-2 border-red-400' 
              : 'text-gray-400 hover:text-white'
          }`}
        >
          All Time
        </button>
      </div>

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
        {isLoading ? (
          <div className="text-center py-10">
            <div className="loading-spinner mb-4"></div>
            <p className="text-gray-400">Loading leaderboard...</p>
          </div>
        ) : filteredUsers.length > 0 ? (
          <ul className="space-y-2">
            {filteredUsers.map((entry) => (
              <li key={entry.userId} className="flex items-center p-3 bg-slate-800 rounded-lg hover:bg-slate-700/50 transition">
                <span className={`w-8 text-center text-lg font-bold ${getRankClass(entry.rank)}`}>
                  {entry.rank}
                </span>
                <div className="mx-4">
                  <Avatar name={entry.name} size={40} />
                </div>
                <div className="flex-grow">
                  <span className="font-semibold">{entry.name}</span>
                  <span className="text-lg ml-2">{getMedalSymbol(entry.rank)}</span>
                </div>
                <span className="text-gray-300 font-medium">{entry.totalSteps.toLocaleString()} steps</span>
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
