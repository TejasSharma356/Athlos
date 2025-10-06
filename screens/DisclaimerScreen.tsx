import React from 'react';
import type { Screen } from '../types';
import { ChevronLeftIcon, AlertTriangleIcon } from '../components/icons';

interface DisclaimerScreenProps {
  onNavigate: (screen: Screen) => void;
}

const DisclaimerScreen: React.FC<DisclaimerScreenProps> = ({ onNavigate }) => {
  return (
    <div className="relative flex flex-col h-full text-white bg-[#0F172A]">
       <div className="absolute inset-0 z-0">
        <img 
          src="https://images.unsplash.com/photo-1526676317768-d9b14f15615a?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
          alt="A runner on a road at dusk."
          className="w-full h-full object-cover"
        />
        <div className="absolute inset-0 bg-black/70"></div>
        <div className="absolute inset-0 bg-gradient-to-t from-[#0F172A] via-[#0F172A]/80 to-transparent"></div>
      </div>

      <div className="relative z-10 flex flex-col h-full">
        <header className="relative flex justify-center items-center p-6 pt-12">
          <button onClick={() => onNavigate('home')} className="absolute left-6 top-1/2 -translate-y-1/2 mt-6 p-2">
            <ChevronLeftIcon className="w-6 h-6" />
          </button>
          <h1 className="text-xl font-bold">Disclaimer</h1>
        </header>

        <main className="flex-grow flex flex-col justify-center items-center p-8 text-center">
          <div className="w-20 h-20 bg-yellow-500/20 rounded-full flex items-center justify-center mb-6">
            <AlertTriangleIcon className="w-12 h-12 text-yellow-400" />
          </div>
          <h2 className="text-3xl font-bold mb-4">Safety & Fair Play</h2>
          
          <div className="bg-slate-800/80 p-6 rounded-xl space-y-4 text-left">
            <p className="text-gray-300">
              <span className="font-bold text-white">Be Aware:</span> Always pay attention to your surroundings, including traffic and pedestrians. Your safety is the top priority.
            </p>
            <p className="text-gray-300">
               <span className="font-bold text-white">Play Fair:</span> To keep the game fun and competitive for everyone, do not use cars, bikes, or any other vehicles to claim territory.
            </p>
            <p className="text-red-400 text-center font-semibold pt-2">
              Ready? Connect your earphones, warm up, and let's go!
            </p>
          </div>
        </main>
        
        <footer className="p-8">
          <button 
            onClick={() => onNavigate('run')}
            className="w-full py-4 bg-red-600 rounded-lg font-semibold text-lg hover:bg-red-700 transition-colors"
          >
            I Understand, Start Run
          </button>
        </footer>
      </div>
    </div>
  );
};

export default DisclaimerScreen;