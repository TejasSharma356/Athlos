import React, { useState } from 'react';
import type { Screen } from '../types';
import { ChevronDownIcon } from '../components/icons';
import { apiService, User } from '../src/services/api';

interface PersonalInfoScreenProps {
  onNavigate: (screen: Screen) => void;
  user: User | null;
  onUserUpdate?: (user: User) => void;
}

const PersonalInfoScreen: React.FC<PersonalInfoScreenProps> = ({ onNavigate, user, onUserUpdate }) => {
  const [age, setAge] = useState<number | undefined>(user?.age);
  const [gender, setGender] = useState<string | undefined>(user?.gender);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [showGenderDropdown, setShowGenderDropdown] = useState(false);
  
  const genders = ['Male', 'Female', 'Other', 'Prefer not to say'];
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
          {error && (
            <div className="mb-4 p-3 bg-red-600/20 border border-red-600 rounded-lg text-red-400 text-center">
              {error}
            </div>
          )}
          <form className="space-y-6 pb-6">
            <div>
              <label className="text-sm font-medium text-gray-400" htmlFor="age">Age</label>
              <input 
                id="age" 
                type="number" 
                value={age || ''}
                onChange={(e) => setAge(e.target.value ? parseInt(e.target.value) : undefined)}
                className="w-full mt-2 p-4 bg-slate-800/80 rounded-lg border border-transparent focus:border-red-500 focus:ring-red-500 transition" 
                placeholder="Enter your age"
                min="1"
                max="120"
              />
            </div>
            <div className="relative">
              <label className="text-sm font-medium text-gray-400" htmlFor="gender">Gender</label>
              <div className="relative">
                <button
                  type="button"
                  onClick={() => setShowGenderDropdown(!showGenderDropdown)}
                  className="w-full mt-2 p-4 bg-slate-800/80 rounded-lg border border-transparent focus:border-red-500 focus:ring-red-500 transition text-left flex justify-between items-center"
                >
                  <span className={gender ? 'text-white' : 'text-gray-400'}>
                    {gender || 'Select gender'}
                  </span>
                  <ChevronDownIcon className={`w-6 h-6 text-gray-400 transition-transform ${showGenderDropdown ? 'rotate-180' : ''}`} />
                </button>
                {showGenderDropdown && (
                  <div className="absolute z-10 w-full mt-1 bg-slate-800 rounded-lg border border-slate-700 shadow-lg">
                    {genders.map((g) => (
                      <button
                        key={g}
                        type="button"
                        onClick={() => {
                          setGender(g);
                          setShowGenderDropdown(false);
                        }}
                        className="w-full p-3 text-left hover:bg-slate-700 first:rounded-t-lg last:rounded-b-lg"
                      >
                        {g}
                      </button>
                    ))}
                  </div>
                )}
              </div>
            </div>
          </form>
        </main>
        
        <footer className="pt-4 pb-8">
          <button 
            type="button"
            onClick={async () => {
              if (!user || !user.id) {
                onNavigate('goalsetting');
                return;
              }
              
              if (!age || !gender) {
                alert('Please fill in all required fields');
                return;
              }
              
              setIsLoading(true);
              setError('');
              try {
                const updatedUser = await apiService.updateUser(user.id, {
                  age: age,
                  gender: gender
                });
                
                if (onUserUpdate) {
                  onUserUpdate(updatedUser);
                }
                
                localStorage.setItem('athlos_user', JSON.stringify(updatedUser));
                onNavigate('goalsetting');
              } catch (err: any) {
                console.error('Error saving personal info:', err);
                setError(err?.message || 'Failed to save. Please try again.');
              } finally {
                setIsLoading(false);
              }
            }}
            disabled={isLoading || !age || !gender}
            className="w-full py-4 bg-red-600 rounded-lg font-semibold text-lg hover:bg-red-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {isLoading ? 'Saving...' : 'Continue'}
          </button>
        </footer>
      </div>
    </div>
  );
};

export default PersonalInfoScreen;