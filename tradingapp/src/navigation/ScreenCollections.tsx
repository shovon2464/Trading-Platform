import SplashScreen from '../screens/onboarding/SplashScreen.tsx';
import LoginScreen from '../screens/authentication/LoginScreen.tsx';
import PhoneScreen from '../screens/authentication/PhoneScreen.tsx';
import PersonalDetailsScreen from '../screens/authentication/PersonalDetailsScreen.tsx';
import EmailScreen from '../screens/authentication/EmailScreen.tsx';
import AuthVerification from '../screens/authentication/AuthVerification.tsx';
import Stock from '../screens/stock/Stock.tsx';

export const authStacks = [
  {
    name: 'SplashScreen',
    component: SplashScreen,
  },
  {
    name: 'LoginScreen',
    component: LoginScreen,
  },
  {
    name: 'EmailScreen',
    component: EmailScreen,
  },
  {
    name: 'PhoneScreen',
    component: PhoneScreen,
  },
  {
    name: 'PersonalDetailsScreen',
    component: PersonalDetailsScreen,
  },
  {
    name: 'AuthVerification',
    component: AuthVerification,
  }
];


export const dashboardStacks = [
  {
    name: "Stock",
    component: Stock,
  },
];

export const mergedStacks = [...authStacks, ...dashboardStacks]

