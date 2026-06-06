import { appAxios } from '../../connectors/apiConfig.tsx';
import { setUser } from '../reducers/userSlice.tsx';
import { navigate, resetAndNavigate } from '../../utils/NavigationUtil.tsx';
import { CHECK_EMAIL, EMAIL_LOGIN, SEND_OTP, VERIFY_OTP } from '../../connectors/API.tsx';
import axios from 'axios';
import Toast from 'react-native-toast-message';
import { token_storage } from '../storage.tsx';
import { persistor } from '../store.tsx';
import { deleteBiometricPublicKey } from '../../utils/BiometricUtils.tsx';

export const checkProfile = () => async (dispatch : any) => {
  try {
    const res = await appAxios.get("api/v1/users/profile");
    const { id, email, phoneExist, fullName } = res.data;

    await dispatch(setUser({ id, email, phoneExist, fullName }));

    if (!fullName) {
      resetAndNavigate("PersonalDetailScreen");
    }
  } catch (error) {
    console.log("PROFILE ->", error);
  }
};

interface CheckEmail {
  email: string;
}

export const checkEmail = (data: CheckEmail) => async (dispatch: any) => {
  try {
    const res = await axios.post(CHECK_EMAIL, data);
    console.log("CHECK EMAIL-->", res.data);
    const emailExists = res.data === true;
    const path = emailExists ? 'EmailPasswordScreen' : 'CreatePasswordScreen';
    navigate(path, {email : data.email });
  } catch (error) {
    Toast.show({
      type: 'warningToast',
      props: {
        msg: 'Something went wrong, please try again later'
      },
    });
    console.log("CHECK EMAIL ERROR-->", error);
  }
}


interface EmailLogin {
  email: string;
  password: string;
}

export const emailLogin = (data: EmailLogin) => async (dispatch: any) => {
  try {
    const res = await axios.post(EMAIL_LOGIN, data);
    console.log("EMAIL LOGIN-->", res.data);
    token_storage.set("app_access_token", res.data.accessToken);
    token_storage.set("app_refresh_token", res.data.refreshToken);
    const { id, email, phoneExist, fullName } = res.data;
    await dispatch(setUser({ id, email, phoneExist, fullName }));
    if (!fullName) {
      resetAndNavigate('PersonalDetailsScreen');
    }
  } catch (error: any) {
    Toast.show({
      type: "normalToast",
      props: {
        msg: error?.response?.data?.msg || "Something went wrong, please try again later"
      }
    })
    console.log("EMAIL LOGIN ERROR-->", error);
  }
}


interface SendOTP {
  otp_type: string;
  email: string;
}

export const sendOtp = (data: SendOTP) => async (dispatch: any)=> {
  try {
    const res = await axios.post(SEND_OTP, data);
    console.log("SEND OTP-->", res.data);

    Toast.show({
      type: "normalToast",
      props: {
        msg: res?.data?.msg || "Something went wrong, please try again later"
      }
    });
  } catch (error: any) {
    Toast.show({
      type: "normalToast",
      props: {
        msg: error?.response?.data?.msg || "Something went wrong, please try again later"
      },
    })
    console.log("SEND OTP ERROR-->", error);
  }
}


interface VerifyOTP {
  otp_type: string;
  email: string;
  otp: string;
  data?: string | null;
}

export const verifyOtp = (data: VerifyOTP) => async (dispatch: any) => {
  try {
    const res = await axios.post(VERIFY_OTP, data);
    console.log("VERIFY OTP-->", res.data);

    if (data.otp_type === "phone") {
      resetAndNavigate("PersonalDetailScreen");
    }
  } catch (error: any) {
    Toast.show({
      type: "normalToast",
      props: {
        msg: error?.response?.data?.msg || "Something went wrong, please try again later"
      },
    });
    console.log("VERIFY OTP ERROR-->", error);
  }
}

interface Profile {
  name: string;
  gender: string;
}

export const updateProfile = (data: Profile) => async (dispatch: any) => {
  try {
    const res = await appAxios.put('api/v1/users/update-profile', data);
    console.log('PROFILE ->', res.data);
  } catch (error: any) {
    console.log('PROFILE ->', error);
  }
};

interface LoginPin {
  login_pin: string;
}

export const setLoginPin =
  (data: LoginPin, updateHook: () => void) => async (dispatch: any) => {
    try {
      const res = await appAxios.post('/auth/set-pin', data);
      console.log(res.data);
      token_storage.set(
        'socket_access_token',
        res.data.socket_tokens.socket_access_token,
      );
      token_storage.set(
        'socket_refresh_token',
        res.data.socket_tokens.socket_refresh_token,
      );
      updateHook();
      resetAndNavigate('AccountProtectedScreen');
    } catch (error: any) {
      console.log('SET LOGIN PIN ->', error);
    }
  };


export const Logout = () => async (dispatch: any) => {
  try {
    const res = await appAxios.post('/auth/logout');
    await token_storage.clearAll();
    await persistor.purge();
    resetAndNavigate('LoginScreen');
    await deleteBiometricPublicKey();
  } catch (error: any) {
    await token_storage.clearAll();
    await persistor.purge();
    resetAndNavigate('LoginScreen');
    console.log('LOG OUT ->', error);
  }
};

export const VerifyPin =
  (data: LoginPin, updateHook: () => void) => async (dispatch: any) => {
    try {
      const res = await appAxios.post('/auth/verify-pin', data);
      console.log(res.data);
      const access_token = res.data.socket_tokens.socket_access_token;
      token_storage.set('socket_access_token', access_token);
      token_storage.set(
        'socket_refresh_token',
        res.data.socket_tokens.socket_refresh_token,
      );
      updateHook();
      return { msg: 'Success', result: true };
    } catch (error: any) {
      console.log('VERIFY PIN ->', error);
      return { msg: error?.response?.data?.msg, result: false };
    }
  };

export const refetchUser = () => async (dispatch: any) => {
  try {
    const res = await appAxios.get('/auth/profile');
    await dispatch(setUser(res.data));
  } catch (error: any) {
    console.log('PROFILE ->', error);
  }
};
