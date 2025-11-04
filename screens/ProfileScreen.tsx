import React, { useState, useEffect } from 'react';
import type { Screen } from '../types';
import { ChevronLeftIcon, MapPinIcon } from '../components/icons';
import { apiService, User, Run } from '../src/services/api';
import { Avatar } from '../components/Avatar';

interface ProfileScreenProps {
  onNavigate: (screen: Screen) => void;
  user: User | null;
}

const ProfileScreen: React.FC<ProfileScreenProps> = ({ onNavigate, user }) => {
  const [userRuns, setUserRuns] = useState<Run[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [stats, setStats] = useState({
    totalRuns: 0,
    totalDistance: 0,
    totalTerritory: 0
  });

  useEffect(() => {
    const loadUserData = async () => {
      if (!user || !user.id) {
        setIsLoading(false);
        return;
      }

      try {
        const runs = await apiService.getUserRuns(user.id);
        setUserRuns(runs);

        // Calculate stats
        const totalRuns = runs.length;
        const totalDistance = runs.reduce((sum, run) => sum + (run.distanceMeters || 0), 0);
        const totalTerritory = runs.reduce((sum, run) => {
          // Simple territory calculation - in real app, this would be more sophisticated
          return sum + (run.claimedTerritory ? run.claimedTerritory.length * 0.1 : 0);
        }, 0);

        setStats({
          totalRuns,
          totalDistance: totalDistance / 1000, // Convert to km
          totalTerritory
        });
      } catch (error) {
        console.error('Error loading user data:', error);
      } finally {
        setIsLoading(false);
      }
    };

    loadUserData();
  }, [user]);

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    const now = new Date();
    const diffTime = Math.abs(now.getTime() - date.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays === 1) return 'Today';
    if (diffDays === 2) return 'Yesterday';
    if (diffDays <= 7) return `${diffDays - 1} days ago`;
    return date.toLocaleDateString();
  };

  const formatDuration = (seconds: number) => {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    if (hours > 0) {
      return `${hours}h ${minutes}m`;
    }
    return `${minutes}m`;
  };

  const recentActivities = userRuns.slice(0, 3).map(run => ({
    distance: `${((run.distanceMeters || 0) / 1000).toFixed(1)} km`,
    time: run.durationSeconds ? formatDuration(run.durationSeconds) : '0m',
    territory: run.claimedTerritory ? `${(run.claimedTerritory.length * 0.1).toFixed(1)} km²` : '0 km²',
    date: run.startTime ? formatDate(run.startTime) : 'Unknown'
  }));
  return (
    <div className="flex flex-col h-full text-white bg-[#0F172A]">
      <header className="relative flex justify-center items-center p-6 pt-12">
        <button onClick={() => onNavigate('home')} className="absolute left-6 top-1/2 -translate-y-1/2 mt-6 p-2">
          <ChevronLeftIcon className="w-6 h-6" />
        </button>
        <h1 className="text-xl font-bold">Profile</h1>
      </header>

      <main className="flex-grow p-6 overflow-y-auto">
        {isLoading ? (
          <div className="flex flex-col items-center justify-center h-full">
            <div className="loading-spinner mb-4"></div>
            <p className="text-gray-400">Loading profile...</p>
          </div>
        ) : (
          <>
            <section className="flex flex-col items-center text-center mb-8">
              <div className="mb-4">
                <Avatar name={user?.name} size={96} />
              </div>
              <h2 className="text-2xl font-bold">{user?.name || 'Guest User'}</h2>
              <p className="text-gray-400">
                {user?.createdAt ? `Joined ${new Date(user.createdAt).toLocaleDateString('en-US', { month: 'long', year: 'numeric' })}` : 'Guest Account'}
              </p>
              {user && user.id !== 0 && (
                <button 
                  onClick={() => onNavigate('editprofile')}
                  className="mt-4 px-6 py-2 bg-slate-800 rounded-full font-semibold text-sm hover:bg-slate-700 transition">
                  Edit Profile
                </button>
              )}
            </section>

            <section className="grid grid-cols-3 gap-4 text-center mb-8">
              <div>
                <p className="text-2xl font-bold">{stats.totalRuns}</p>
                <p className="text-sm text-gray-400">Total Runs</p>
              </div>
              <div>
                <p className="text-2xl font-bold">{stats.totalDistance.toFixed(1)} km</p>
                <p className="text-sm text-gray-400">Distance</p>
              </div>
              <div>
                <p className="text-2xl font-bold">{stats.totalTerritory.toFixed(1)} km²</p>
                <p className="text-sm text-gray-400">Territory</p>
              </div>
            </section>
          </>
        )}

        <section>
          <h3 className="text-xl font-bold mb-4">Recent Activities</h3>
          <div className="space-y-4">
            {recentActivities.length > 0 ? (
              recentActivities.map((activity, index) => (
                <div key={index} className="bg-slate-800 p-4 rounded-xl flex items-center">
                  <div className="w-12 h-12 bg-red-600 rounded-lg flex items-center justify-center mr-4">
                    <MapPinIcon className="w-6 h-6"/>
                  </div>
                  <div className="flex-grow">
                    <div className="flex justify-between items-center">
                      <p className="font-bold">{activity.distance} Run</p>
                      <p className="text-sm text-gray-400">{activity.date}</p>
                    </div>
                    <div className="flex justify-between text-sm text-gray-300 mt-1">
                      <span>{activity.time}</span>
                      <span>{activity.territory} Claimed</span>
                    </div>
                  </div>
                </div>
              ))
            ) : (
              <div className="bg-slate-800 p-8 rounded-xl text-center">
                <MapPinIcon className="w-12 h-12 mx-auto mb-4 text-gray-500"/>
                <p className="text-gray-400">No runs yet. Start your first run to see your activities here!</p>
              </div>
            )}
          </div>
        </section>
      </main>
    </div>
  );
};

export default ProfileScreen;
