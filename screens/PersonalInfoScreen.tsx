import React from 'react';
import type { Screen } from '../types';
import { ChevronDownIcon } from '../components/icons';

// Fix: Updated props to accept an optional `onNext` callback to handle navigation within the onboarding flow. `onNavigate` is also made optional.
interface PersonalInfoScreenProps {
  onNavigate?: (screen: Screen) => void;
  onNext?: () => void;
}

const PersonalInfoScreen: React.FC<PersonalInfoScreenProps> = ({ onNavigate, onNext }) => {
  return (
    <div className="relative h-full text-white">
      <div className="absolute inset-0 z-0">
        <img 
          src="https://images.unsplash.com/photo-1744706908605-ac30eb45f98c?q=80&w=2150&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
          alt="A runner on a road at dusk."
          className="w-full h-full object-cover"
        />
        <div className="absolute inset-0 bg-black/60"></div>
        <div className="absolute inset-0 bg-gradient-to-t from-[#0F172A] via-[#0F172A]/80 to-transparent"></div>
      </div>

      <div className="relative z-10 flex flex-col h-full px-8">
        <header className="pt-16 pb-6">
          <h2 className="text-3xl font-bold mb-2 text-center">Personal Information</h2>
          <p className="text-gray-400 text-center">Tell us a bit about yourself</p>
        </header>
        
        <main className="flex-grow overflow-y-auto min-h-0">
          <form className="space-y-6 pb-6">
            <div className="relative">
              <label className="text-sm font-medium text-gray-400" htmlFor="dob">Date Of Birth</label>
              <div className="relative">
                <input 
                  id="dob" 
                  type="text" 
                  className="w-full mt-2 p-4 bg-slate-800/80 rounded-lg border border-transparent focus:border-red-500 focus:ring-red-500 transition appearance-none" 
                  placeholder="Select date"
                  readOnly
                />
                <ChevronDownIcon className="absolute right-4 top-1/2 -translate-y-1/2 mt-1 w-6 h-6 text-gray-400 pointer-events-none" />
              </div>
            </div>
            <div className="relative">
              <label className="text-sm font-medium text-gray-400" htmlFor="gender">Gender</label>
               <div className="relative">
                  <input 
                    id="gender" 
                    type="text" 
                    className="w-full mt-2 p-4 bg-slate-800/80 rounded-lg border border-transparent focus:border-red-500 focus:ring-red-500 transition appearance-none" 
                    placeholder="Select gender"
                    readOnly
                  />
                  <ChevronDownIcon className="absolute right-4 top-1/2 -translate-y-1/2 mt-1 w-6 h-6 text-gray-400 pointer-events-none" />
               </div>
            </div>
            <div>
              <label className="text-sm font-medium text-gray-400" htmlFor="height">Height</label>
              <input 
                id="height" 
                type="number" 
                className="w-full mt-2 p-4 bg-slate-800/80 rounded-lg border border-transparent focus:border-red-500 focus:ring-red-500 transition" 
                placeholder="Enter height (cm)"
              />
            </div>
            <div>
              <label className="text-sm font-medium text-gray-400" htmlFor="weight">Weight</label>
              <input 
                id="weight" 
                type="number" 
                className="w-full mt-2 p-4 bg-slate-800/80 rounded-lg border border-transparent focus:border-red-500 focus:ring-red-500 transition" 
                placeholder="Enter weight (kg)"
              />
            </div>
          </form>
        </main>
        
        <footer className="pt-4 pb-8">
          <button 
            type="button"
            onClick={() => {
              if (onNext) {
                onNext();
              } else if (onNavigate) {
                onNavigate('goalsetting');
              }
            }}
            className="w-full py-4 bg-red-600 rounded-lg font-semibold text-lg hover:bg-red-700 transition-colors"
          >
            Continue
          </button>
        </footer>
      </div>
    </div>
  );
};

export default PersonalInfoScreen;