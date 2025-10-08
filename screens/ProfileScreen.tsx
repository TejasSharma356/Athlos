import React from 'react';
import type { Screen, User } from '../types';
import { ChevronLeftIcon, MapPinIcon } from '../components/icons';
import Avatar from '../components/Avatar';

interface ProfileScreenProps {
  onNavigate: (screen: Screen) => void;
  user: User;
}

const recentActivities = [
  { distance: '5.2 km', time: '28 min', territory: '1.5 km²', date: 'Today' },
  { distance: '3.1 km', time: '18 min', territory: '0.8 km²', date: 'Yesterday' },
  { distance: '10.5 km', time: '55 min', territory: '3.2 km²', date: '3 days ago' },
];

const ProfileScreen: React.FC<ProfileScreenProps> = ({ onNavigate, user }) => {
  const userName = user.name;
  const userAvatarSrc = user.avatar;

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
          <p className="text-gray-400">Joined May 2024</p>
          <button 
            onClick={() => onNavigate('editprofile')}
            className="mt-4 px-6 py-2 bg-slate-800 rounded-full font-semibold text-sm hover:bg-slate-700 transition">
            Edit Profile
          </button>
        </section>

        <section className="grid grid-cols-3 gap-4 text-center mb-8">
          <div>
            <p className="text-2xl font-bold">28</p>
            <p className="text-sm text-gray-400">Total Runs</p>
          </div>
          <div>
            <p className="text-2xl font-bold">124 km</p>
            <p className="text-sm text-gray-400">Distance</p>
          </div>
          <div>
            <p className="text-2xl font-bold">18.7 km²</p>
            <p className="text-sm text-gray-400">Territory</p>
          </div>
        </section>

        <section>
          <h3 className="text-xl font-bold mb-4">Recent Activities</h3>
          <div className="space-y-4">
            {recentActivities.map((activity, index) => (
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
            ))}
          </div>
        </section>
      </main>
    </div>
  );
};

export default ProfileScreen;
