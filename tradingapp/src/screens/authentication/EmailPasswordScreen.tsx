import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
} from 'react-native';
import React, { useState } from 'react';
import CustomSafeAreaView from '../../components/global/CustomSafeAreaView.tsx';
import CenteredLogo from '../../components/global/CenteredLogo.tsx';
import CustomInput from '../../components/inputs/CustomInputs.tsx';
import { goBack, navigate } from '../../utils/NavigationUtil.tsx';
import { useAppDispatch } from '../../redux/reduxHook.tsx';
import { validatePasswordLength } from '../../utils/ValidationUtils.tsx';
import CustomText from '../../components/global/CustomText.tsx';
import { RFValue } from 'react-native-responsive-fontsize';
import { GlobalStyles } from '../../styles/GlobalStyles.tsx';
import CustomButton from '../../components/global/CustomButton.tsx';
import { emailLogin } from '../../redux/actions/userAction.tsx';


const EmailPasswordScreen = ({ route }: any) => {
  const [password, setPassword] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [loading, setLoading] = useState(false);
  const dispatch = useAppDispatch();
  const validate = () => {
    if (!validatePasswordLength(password)) {
      setPasswordError("Password must be between 8 and 20 characters");
      return false;
    }
    return true;
  }

  const handleOnSubmit = async () => {
    setLoading(true);
    if (validate()) {
      await dispatch(emailLogin({ email: route.params.email, password }));
    }
    setLoading(false);
  };
  return (
    <CustomSafeAreaView>
      <ScrollView>
        <CenteredLogo />
        <TouchableOpacity onPress={() => goBack()}>
          <View pointerEvents="none">
            <CustomInput label="EMAIL ADDRESS" value={route.params.email} />
          </View>
        </TouchableOpacity>

        <CustomInput
          label="ENTER PASSWORD"
          returnKeyType="done"
          placeholder="8-20 Characters"
          value={password}
          autoFocus={true}
          error={passwordError}
          onChangeText={text => {
            setPassword(text);
            setPasswordError('');
          }}
          onSubmitEditing={handleOnSubmit}
          password
        />

        <TouchableOpacity
          onPress={() =>
            navigate('ForgotPassword', {
              email: route?.params?.email || '',
            })
          }
        >
          <Text style={styles.forgotText}>Forgot Password?</Text>
        </TouchableOpacity>
      </ScrollView>
      <View style={GlobalStyles.bottomBtn}>
        <CustomButton
          text="ENTER"
          loading={loading}
          disabled={loading}
          onPress={() => {
            handleOnSubmit();
          }}
        />
      </View>
    </CustomSafeAreaView>
  );
};


const styles = StyleSheet.create({
  forgotText: {
    fontSize: RFValue(12),
    marginTop: 5,
    alignSelf: 'flex-end',
    color: '#d9534f',
  },
});

export default EmailPasswordScreen;
