export type Screen = 'welcome' | 'signup' | 'signin' | 'personalinfo' | 'goalsetting' | 'home' | 'profile' | 'settings' | 'editprofile' | 'leaderboard' | 'disclaimer' | 'run';

// Fix: Added the OnboardingStepProps interface, which was missing.
export interface OnboardingStepProps {
  onNext: () => void;
}

export type User = {
  name: string;
  email: string;
  avatar?: string;
};

export type LeaderboardUser = {
  id: number;
  name: string;
  avatar: string;
  steps: number;
  rank: number;
};

export type Activity = {
  id: string;
  distance: string;
  time: string;
  territory: string;
  date: string;
};
