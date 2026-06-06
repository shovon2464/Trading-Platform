import SplashScreen from '../screens/onboarding/SplashScreen.tsx';
import LoginScreen from '../screens/authentication/LoginScreen.tsx';
import PhoneScreen from '../screens/authentication/PhoneScreen.tsx';
import PersonalDetailsScreen from '../screens/authentication/PersonalDetailsScreen.tsx';
import EmailScreen from '../screens/authentication/EmailScreen.tsx';
import AuthVerification from '../screens/authentication/AuthVerification.tsx';
import Stock from '../screens/stock/Stock.tsx';
import EmailPasswordScreen from '../screens/authentication/EmailPasswordScreen.tsx';
import EmailOtpScreen from '../screens/authentication/EmailOtpScreen.tsx';
import ForgotPassword from '../screens/authentication/ForgotPassword.tsx';
import PinScreen from '../screens/authentication/PinScreen.tsx';
import ConfirmPinScreen from '../screens/authentication/ConfirmPinScreen.tsx';
import AccountProtectedScreen from '../screens/authentication/AccountProtectedScreen.tsx';
import CreatePasswordScreen from '../screens/authentication/CreatePasswordScreen.tsx';

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
    name: 'AuthVerificationScreen',
    component: AuthVerification,
  },
  {
    name: 'EmailPasswordScreen',
    component: EmailPasswordScreen,
  },
  {
    name: 'EmailOtpScreen',
    component: EmailOtpScreen,
  },
  {
    name: 'ForgotPasswordScreen',
    component: ForgotPassword,
  },
  {
    name: 'PinScreen',
    component: PinScreen,
  },
  {
    name: 'ConfirmPinScreen',
    component: ConfirmPinScreen,
  },
  {
    name: 'AccountProtectedScreen',
    component: AccountProtectedScreen,
  },
  {
    name: 'CreatePasswordScreen',
    component: CreatePasswordScreen,
  },
];


export const dashboardStacks = [
  {
    name: "Stock",
    component: Stock,
  },
];

export const mergedStacks = [...authStacks, ...dashboardStacks]

