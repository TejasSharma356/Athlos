import React, { useState, useEffect } from 'react';
import type { Screen, User, Activity } from '../types';
import { ChevronLeftIcon, MapPinIcon } from '../components/icons';
import Avatar from '../components/Avatar';

interface ProfileScreenProps {
  onNavigate: (screen: Screen) => void;
  user: User;
}

// Defines the structure for the profile data to be fetched
interface ProfileData {
  joinedDate: string;
  totalRuns: number;
  distance: number;
  territory: number;
  recentActivities: Activity[];
}

const ProfileScreen: React.FC<ProfileScreenProps> = ({ onNavigate, user }) => {
  const [profileData, setProfileData] = useState<ProfileData | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // In a real app, you would fetch the user's profile data here from your backend.
    const fetchProfileData = async () => {
      setIsLoading(true);
      // Example: const data = await yourApi.getUserProfile(user.id);
      // For now, we set a default empty state to prepare for backend integration.
      setProfileData({
        joinedDate: 'May 2024', // This can be replaced by dynamic data
        totalRuns: 0,
        distance: 0,
        territory: 0,
        recentActivities: [],
      });
      setIsLoading(false);
    };

    fetchProfileData();
  }, [user]); // Re-fetch if the user changes

  const userName = user.name;
  const userAvatarSrc = user.avatar;

  if (isLoading) {
    return (
      <div className="flex flex-col h-full text-white bg-[#0F172A] items-center justify-center">
        <h1 className="text-xl font-bold">Loading Profile...</h1>
      </div>
    );
  }

  return (
    <div className="flex flex-col h-full text-white bg-[#0F172A]">
      <header className="relative flex justify-center items-center p-6 pt-12">
        <button onClick={() => onNavigate('home')} className="absolute left-6 top-1/2 -translate-y-1/2 mt-6 p-2">
          <ChevronLeftIcon className="w-6 h-6" />
        </button>
        <h1 className="text-xl font-bold">Profile</h1>
      </header>

      <main className="flex-grow p-6 overflow-y-auto">
        <section className="flex flex-col items-center text-center mb-8">
          <Avatar
            name={userName}
            src={userAvatarSrc}
            className="w-24 h-24 rounded-full mb-4 border-4 border-slate-700"
            textClassName="text-4xl"
          />
          <h2 className="text-2xl font-bold">{userName}</h2>
          <p className="text-gray-400">{profileData?.joinedDate ? `Joined ${profileData.joinedDate}` : ''}</p>
          <button
            onClick={() => onNavigate('editprofile')}
            className="mt-4 px-6 py-2 bg-slate-800 rounded-full font-semibold text-sm hover:bg-slate-700 transition">
            Edit Profile
          </button>
        </section>

        <section className="grid grid-cols-3 gap-4 text-center mb-8">
          <div>
            <p className="text-2xl font-bold">{profileData?.totalRuns ?? 0}</p>
            <p className="text-sm text-gray-400">Total Runs</p>
          </div>
          <div>
            <p className="text-2xl font-bold">{profileData?.distance ?? 0} km</p>
            <p className="text-sm text-gray-400">Distance</p>
          </div>
          <div>
            <p className="text-2xl font-bold">{profileData?.territory?.toFixed(1) ?? 0} km²</p>
            <p className="text-sm text-gray-400">Territory</p>
          </div>
        </section>

        <section>
          <h3 className="text-xl font-bold mb-4">Recent Activities</h3>
          <div className="space-y-4">
            {profileData && profileData.recentActivities.length > 0 ? (
              profileData.recentActivities.map((activity) => (
                <div key={activity.id} className="bg-slate-800 p-4 rounded-xl flex items-center">
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
              <div className="bg-slate-800 p-6 rounded-xl text-center text-gray-400">
                <p>No recent activities found.</p>
              </div>
            )}
          </div>
        </section>
      </main>
    </div>
  );
};

export default ProfileScreen;
