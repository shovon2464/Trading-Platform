import { Platform } from 'react-native';
import { GoogleSignin } from '@react-native-google-signin/google-signin';
import { OAUTH } from '../connectors/API.tsx';
import axios from 'axios';
import Toast from 'react-native-toast-message';
import { token_storage } from './storage.tsx';
import { setUser } from './reducers/userSlice.tsx';
import appleAuth, { appleAuthAndroid } from '@invertase/react-native-apple-authentication';


const handleSignInError = (error: any) => {
  console.log("Error", error);
  Toast.show({
    type: 'normalToast',
    props: {
      msg: "We are facing issues, please try again after sometime",
    },
  });
};

const handleSignInSuccess = async (res: any, dispatch: any) => {
  token_storage.set("app_access_token", res.data.tokens.access_token);
  token_storage.set("app_refresh_token", res.data.tokens.refresh_token);
  await dispatch(setUser(res.data.user));
}

export const signInWithApple = async (dispatch: any) => {
  if (Platform.OS === "ios") {
    await signInWithAppleIos(dispatch);
  } else {
    await signInWithAppleAndroid(dispatch);
  }
};

export const signInWithAppleAndroid = async (dispatch: any) => {
  try {
    const rawNonce = 'uuid()';
    const state = 'uuid()';

    appleAuthAndroid.configure({
      clientId: 'client_id',
      redirectUri: 'https://auth.expo.dev',
      responseType: appleAuthAndroid.ResponseType.ALL,
      scope: appleAuthAndroid.Scope.ALL,
      nonce: rawNonce,
      state,
    });
    const response = await appleAuthAndroid.signIn();
    const res = await axios.post(OAUTH, {
      provider: 'apple',
      id_token: response.id_token,
    });
    await handleSignInSuccess(res, dispatch);
  } catch (error) {
    handleSignInError(error);
  }
}

export const signInWithAppleIos = async (dispatch: any) => {
  try {
    const appleAuthRequestResponse = await appleAuth.performRequest({
      requestedOperation: appleAuth.Operation.LOGIN,
      requestedScopes: [appleAuth.Scope.FULL_NAME, appleAuth.Scope.EMAIL],
    });
    const credentialState = await appleAuth.getCredentialStateForUser(
      appleAuthRequestResponse.user
    );

    if (credentialState === appleAuth.State.AUTHORIZED) {
      const appleResponse = appleAuthRequestResponse;
      const res = await axios.post(OAUTH, {
        provider: 'apple',
        id_token: appleResponse.identityToken,
      });
      await handleSignInSuccess(res, dispatch);
    } else {
      throw new Error('User not authorized');
    }

  } catch (error) {
    handleSignInError(error);
  }
};

export const signInWithGoogle = async (dispatch: any) => {
  try {
    await GoogleSignin.hasPlayServices();
    await GoogleSignin.signOut();
    const { idToken } = await GoogleSignin.signIn() as any;
    const res = await axios.post(OAUTH, {
      provider: "google",
      id_token: idToken,
    });
    await handleSignInSuccess(res, dispatch);
  } catch (error) {
    handleSignInError(error);
  }
};