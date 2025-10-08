import React, { useState } from 'react';
import type { Screen, User } from './types';
import WelcomeScreen from './screens/WelcomeScreen';
import SignUpScreen from './screens/SignUpScreen';
import SignInScreen from './screens/SignInScreen';
import PersonalInfoScreen from './screens/PersonalInfoScreen';
import GoalSettingScreen from './screens/GoalSettingScreen';
import HomeScreen from './screens/HomeScreen';
import ProfileScreen from './screens/ProfileScreen';
import SettingsScreen from './screens/SettingsScreen';
import EditProfileScreen from './screens/EditProfileScreen';
import LeaderboardScreen from './screens/LeaderboardScreen';
import DisclaimerScreen from './screens/DisclaimerScreen';
import RunScreen from './screens/RunScreen';

const App: React.FC = () => {
  const [screen, setScreen] = useState<Screen>('welcome');
  const [currentUser, setCurrentUser] = useState<User>({
    name: 'Alex Johnson',
    email: 'alex.j@example.com',
  });

  const navigate = (newScreen: Screen): void => {
    setScreen(newScreen);
  };

  const handleSignUp = (name: string, email: string) => {
    setCurrentUser({ name, email });
    navigate('personalinfo');
  };

  const handleSignIn = () => {
    // In a real app, you'd fetch user data. Here we just navigate.
    navigate('home');
  };

  const handleUpdateProfile = (name: string, email: string) => {
    setCurrentUser(prevUser => ({ ...prevUser, name, email }));
  };
  
  const handleLogout = () => {
      // Reset to a default or null state and go to sign in
      setCurrentUser({ name: 'Alex Johnson', email: 'alex.j@example.com' });
      navigate('signin');
  }

  const renderScreen = () => {
    switch (screen) {
      case 'welcome':
        return <WelcomeScreen onNavigate={navigate} />;
      case 'signup':
        return <SignUpScreen onNavigate={navigate} onSignUp={handleSignUp} />;
      case 'signin':
        return <SignInScreen onNavigate={navigate} onSignIn={handleSignIn} />;
      case 'personalinfo':
        return <PersonalInfoScreen onNavigate={navigate} />;
      case 'goalsetting':
        return <GoalSettingScreen onNavigate={navigate} />;
      case 'home':
        return <HomeScreen onNavigate={navigate} user={currentUser} onLogout={handleLogout} />;
      case 'profile':
        return <ProfileScreen onNavigate={navigate} user={currentUser} />;
      case 'settings':
        return <SettingsScreen onNavigate={navigate} />;
      case 'editprofile':
        return <EditProfileScreen onNavigate={navigate} user={currentUser} onSave={handleUpdateProfile} />;
      case 'leaderboard':
        return <LeaderboardScreen onNavigate={navigate} />;
      case 'disclaimer':
        return <DisclaimerScreen onNavigate={navigate} />;
      case 'run':
        return <RunScreen onNavigate={navigate} />;
      default:
        return <WelcomeScreen onNavigate={navigate} />;
    }
  };

  return (
    <div className="min-h-screen bg-black flex items-center justify-center font-sans antialiased text-white">
      <div className="relative w-full max-w-[400px] h-[844px] bg-[#0F172A] overflow-hidden shadow-2xl rounded-3xl">
        <div className="absolute top-0 left-0 w-full px-8 py-3 flex justify-between items-center z-50">
            <div className="flex items-center space-x-1">
              <span className="font-semibold text-sm">9:41</span>
            </div>
            <div className="w-24 h-8 bg-black/80 backdrop-blur-sm rounded-full"></div>
            <div className="flex items-center space-x-2">
                <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M18.364 5.636a9 9 0 010 12.728M12 18.001v.01"></path><path d="M8.5 12.5a3.5 3.5 0 013.5-3.5h0a3.5 3.5 0 013.5 3.5v0a3.5 3.5 0 01-3.5 3.5h0a3.5 3.5 0 01-3.5-3.5z"></path></svg>
                <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20"><path d="M18 9.5a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0zM11.5 9.5a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0zM5 9.5a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0z"></path></svg>
                <div className="w-6 h-3 border-2 border-white rounded-sm flex items-center p-0.5"><div className="w-3/4 h-full bg-white rounded-xs"></div></div>
            </div>
        </div>
        <div className="h-full w-full">
            {renderScreen()}
        </div>
      </div>
    </div>
  );
};

export default App;
