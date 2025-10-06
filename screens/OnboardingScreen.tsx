import React, { useState } from 'react';
import PersonalInfoScreen from './PersonalInfoScreen';
import GoalSettingScreen from './GoalSettingScreen';

interface OnboardingScreenProps {
  onComplete: () => void;
}

const OnboardingScreen: React.FC<OnboardingScreenProps> = ({ onComplete }) => {
  const [step, setStep] = useState(0);

  const nextStep = () => {
    setStep(currentStep => currentStep + 1);
  };

  const steps = [
    <PersonalInfoScreen onNext={nextStep} />,
    <GoalSettingScreen onNext={onComplete} />,
  ];

  return (
    <div className="relative w-full h-full overflow-hidden">
      <div className="flex h-full transition-transform duration-500 ease-in-out" style={{ transform: `translateX(-${step * 100}%)`, width: `${steps.length * 100}%` }}>
        {steps.map((screen, index) => (
          <div key={index} className="w-full h-full flex-shrink-0">
            {screen}
          </div>
        ))}
      </div>
    </div>
  );
};

export default OnboardingScreen;