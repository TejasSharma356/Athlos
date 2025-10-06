import React from 'react';
import type { Screen } from '../types';

interface WelcomeScreenProps {
  onNavigate: (screen: Screen) => void;
}

const WelcomeScreen: React.FC<WelcomeScreenProps> = ({ onNavigate }) => {
  return (
    <div className="relative flex flex-col h-full text-white">
      <div className="absolute inset-0 z-0">
        <img 
          src="https://images.unsplash.com/photo-1744706908605-ac30eb45f98c?q=80&w=2150&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
          alt="A runner on a road at dusk."
          className="w-full h-full object-cover"
        />
        <div className="absolute inset-0 bg-black/60"></div>
        <div className="absolute inset-0 bg-gradient-to-t from-[#0F172A] via-[#0F172A]/50 to-transparent"></div>
      </div>

      <div className="relative z-10 flex flex-col h-full p-8">
        {/* This container grows and pushes the button to the bottom, while positioning the text at its end. */}
        <div className="flex-grow flex flex-col justify-end pb-10">
          <h1 className="text-5xl md:text-6xl font-bold tracking-wider mb-4 text-left">
            ATHLOS
          </h1>
          <p className="text-lg text-gray-300 mb-4 text-left">
            Turn your city into a canvas and your feet into the paintbrush. Run, walk, and move to claim territory on a live, real-world map.
          </p>
          <p className="text-red-400 font-semibold text-lg text-left">
            Move to play. Play to conquer.
          </p>
        </div>

        {/* This container holds the button at the bottom */}
        <div className="flex-shrink-0">
          <button
            onClick={() => onNavigate('signup')}
            className="w-full py-4 bg-red-600 rounded-lg font-semibold text-lg hover:bg-red-700 transition-colors"
          >
            Get Started
          </button>
        </div>
      </div>
    </div>
  );
};

export default WelcomeScreen;
