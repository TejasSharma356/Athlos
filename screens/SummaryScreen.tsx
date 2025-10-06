import React from 'react';
import type { OnboardingStepProps } from '../types';
import { ChartIcon, HomeIcon, BellIcon } from '../components/icons';

const SummaryScreen: React.FC<OnboardingStepProps> = ({ onNext }) => {
  return (
    <div className="flex flex-col h-full p-8 text-white bg-[#0F172A] justify-between">
      <main className="flex-grow flex flex-col justify-center items-center text-center">
        <div className="w-16 h-16 bg-red-600 rounded-full flex items-center justify-center mb-6">
            <svg className="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path></svg>
        </div>
        <h2 className="text-3xl font-bold mb-4">You're All Set!</h2>
        <p className="text-gray-400 max-w-sm mb-8">
          Welcome to Athlos. Get ready to crush your goals and track your progress like never before.
        </p>

        <div className="space-y-4 w-full max-w-sm">
            <div className="bg-slate-800 p-4 rounded-lg flex items-center">
                <HomeIcon className="w-6 h-6 mr-4 text-red-400" />
                <p>Track your daily activity</p>
            </div>
             <div className="bg-slate-800 p-4 rounded-lg flex items-center">
                <ChartIcon className="w-6 h-6 mr-4 text-red-400" />
                <p>Compete on the leaderboard</p>
            </div>
             <div className="bg-slate-800 p-4 rounded-lg flex items-center">
                <BellIcon className="w-6 h-6 mr-4 text-red-400" />
                <p>Get personalized notifications</p>
            </div>
        </div>
      </main>

      <div className="pb-4">
        <button 
          type="button"
          onClick={onNext}
          className="w-full py-4 bg-red-600 rounded-lg font-semibold text-lg hover:bg-red-700 transition-colors"
        >
          Get Started
        </button>
      </div>
    </div>
  );
};

export default SummaryScreen;
