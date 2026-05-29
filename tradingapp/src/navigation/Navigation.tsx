import { DefaultTheme, NavigationContainer } from '@react-navigation/native';
import MainNavigator from './MainNavigator.tsx';
import { Colors } from '../constants/Colors.tsx';
import { navigationRef } from '../utils/NavigationUtil.tsx';


const Navigation = () => {
  const MyTheme = {
    ...DefaultTheme,
    colors: {
      ...DefaultTheme.colors,
      background: Colors.background,
      text: Colors.text,
      card: Colors.card,
      border: Colors.border,
      notification: Colors.notification_card,
      primary: Colors.themeColor,
    },
  };

  return (
    <NavigationContainer ref={navigationRef} theme={MyTheme} >
      <MainNavigator />
    </NavigationContainer>
  )
}

export default Navigation;