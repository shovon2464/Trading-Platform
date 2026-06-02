import { appAxios } from '../../connectors/apiConfig.tsx';
import { setUser } from '../reducers/userSlice.tsx';
import { navigate, resetAndNavigate } from '../../utils/NavigationUtil.tsx';
import { CHECK_EMAIL } from '../../connectors/API.tsx';
import axios from 'axios';
import Toast from 'react-native-toast-message';

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
    let path = res.data.isExist ? 'EmailPasswordScreen' : 'EmailOtpScreen';
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
