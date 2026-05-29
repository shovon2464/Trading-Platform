import SplashScreen from '../screens/onboarding/SplashScreen.tsx';
import LoginScreen from '../screens/authentication/LoginScreen.tsx';

export const authStacks = [
  {
    name: 'SplashScreen',
    component: SplashScreen,
  },
  {
    name: 'LoginScreen',
    component: LoginScreen,
  }
];

export const dashboardStacks = [];

export const mergedStacks = [...authStacks, ...dashboardStacks]

