import React, { useEffect } from 'react';
import { View, StyleSheet } from 'react-native';
import CustomSafeAreaView from '../../components/global/CustomSafeAreaView';
import DotLoading from '../../components/global/DotLoading';
import { useAppDispatch } from '../../redux/reduxHook.tsx';
import { token_storage } from '../../redux/storage.tsx';
import { resetAndNavigate } from '../../utils/NavigationUtil.tsx';
import { jwtDecode } from "jwt-decode";
import Toast from 'react-native-toast-message';
import { refresh_tokens } from '../../connectors/apiConfig.tsx';
import { checkProfile } from '../../redux/actions/userAction.tsx';


interface DecodedToken {
  exp: number;
}
const SplashScreen = () => {

  const dispatch = useAppDispatch();

  const tokenCheck = async () => {
    const app_access_token = token_storage.getString(
      'app_access_token',
    ) as string;
    const app_refresh_token = token_storage.getString(
      'app_refresh_token',
    ) as string;

    if (app_access_token) {
      const decodedAccessToken = jwtDecode<DecodedToken>(app_access_token);
      const decodedRefreshToken = jwtDecode<DecodedToken>(app_refresh_token);

      const currentTime = Date.now() / 1000;

      if (decodedRefreshToken?.exp < currentTime) {
        resetAndNavigate('LoginScreen');
        Toast.show({
          type: 'warningToast',
          props: {
            msg: 'Session Expired, Login Again'
          },
        });
        return;
      }

      if (decodedAccessToken?.exp < currentTime) {
        try {
          refresh_tokens('app', true);
          await dispatch(checkProfile());
        } catch (error) {
          console.log('Error Refreshing token');
          Toast.show({
            type: 'warningToast',
            props: {
              msg: 'Session Expired, Login Again',
            },
          });
          return;
        }
      } else {
        await dispatch(checkProfile());
      }

      return;
    }
    resetAndNavigate('LoginScreen');
  }

  useEffect(() => {

    const deeplinks = async () => {
      await tokenCheck();
    }

    const timerId = setTimeout(deeplinks, 1000);

    return () => clearTimeout(timerId);
  }, []);

  return (
    <CustomSafeAreaView>
      <View style={styles.container}>
        <DotLoading />
      </View>
    </CustomSafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default SplashScreen;
