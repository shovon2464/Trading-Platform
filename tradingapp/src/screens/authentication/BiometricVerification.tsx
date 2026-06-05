import { View, Text, Image, StyleSheet } from 'react-native';
import React, { FC, useEffect, useState } from 'react';
import { selectUser } from '../../redux/reducers/userSlice';
import { useAppDispatch, useAppSelector } from '../../redux/reduxHook';
import { useWS } from '../../utils/WSProvider';
import CustomSafeAreaView from '../../components/global/CustomSafeAreaView';
import CustomText from '../../components/global/CustomText';
import TouchableText from '../../components/authentication/TouchableText';
import { FONTS } from '../../constants/Fonts';
import { RFValue } from 'react-native-responsive-fontsize';
import { Logout, VerifyPin } from '../../redux/actions/userAction';
import Logo from '../../assets/images/logo.png';
import { resetAndNavigate } from '../../utils/NavigationUtil';
import { loginWithBiometrics } from '../../utils/BiometricUtils';
import CustomNumberPad from '../../components/inputs/CustomNumberPad';
import DiamondOTPInput from '../../components/inputs/DiamondOTPInput';

const initialState = ['', '', '', ''];

interface BiometricProp {
  onForgotPin: () => void;
}
const BiometricVerification: FC<BiometricProp> = ({ onForgotPin }) => {
  const [otpValues, setOtpValues] = useState(['', '', '', '']);
  const user = useAppSelector(selectUser);
  const [focusedIndex, setFocusedIndex] = useState(0);
  const [loading, setLoading] = useState(false);
  const [otpError, setOtpError] = useState<string | null>(null);
  const dispatch = useAppDispatch();

  const handlePressNumber = (number: number | string) => {
    if (focusedIndex < otpValues.length) {
      const newOtpValues = [...otpValues];
      newOtpValues[focusedIndex] = number.toString();
      setOtpError(null);
      setOtpValues(newOtpValues);
      setFocusedIndex(focusedIndex + 1);
    }
  };

  const handlePressBackspace = () => {
    if (focusedIndex > 0) {
      const newOtpValues = [...otpValues];
      newOtpValues[focusedIndex - 1] = '';
      setOtpValues(newOtpValues);
      setFocusedIndex(focusedIndex - 1);
    }
  };

  const handleBiometricVerification = async () => {
    const { msg, result } = await dispatch(
      loginWithBiometrics(user.id || ''),
    );
    if (!result) {
      setOtpError(msg);
      return;
    }

    if (result) {
      setOtpValues(['B', 'I', 'O', 'P']);
      resetAndNavigate('Stock');
    }
  };

  const handlePressCheckmark = async () => {
    let valid = false;
    if (otpValues.join('') == 'BIOP') {
      return;
    }
    otpValues.forEach(i => {
      if (i === '') {
        valid = true;
        setOtpError('Enter PIN');
        setOtpValues(initialState);
        setFocusedIndex(0);
      }
    });
    if (!valid) {
      setLoading(true);
      setOtpValues(initialState);
      setFocusedIndex(0);
      setLoading(false);
    }
  };

  useEffect(() => {
    const allFilled = otpValues.every(value => value !== '');
    if (allFilled) {
      handlePressCheckmark();
    }
  }, [otpValues]);

  useEffect(() => {
    handleBiometricVerification();
  }, []);

  return (
    <CustomSafeAreaView>
      <View style={styles.container}>
        <Image source={Logo} style={styles.logo} />
        <CustomText variant="h1" fontFamily={FONTS.Medium}>
          Enter App PIN
        </CustomText>
        <View style={styles.emailContainer}>
          <CustomText style={styles.subText}>{user?.email}</CustomText>
          <TouchableText
            firstText="Logout"
            style={styles.logoutText}
            onPress={() => dispatch(Logout())}
          />
        </View>
      </View>

      <DiamondOTPInput
        onForgotPin={onForgotPin}
        loading={loading}
        otpValues={otpValues}
        error={otpError}
      />

      <CustomNumberPad
        customFont
        onPressBiometric={handleBiometricVerification}
        isBiometric={true}
        onPressNumber={handlePressNumber}
        onPressBackspace={handlePressBackspace}
        onPressCheckmark={handlePressCheckmark}
      />
    </CustomSafeAreaView>
  );
};

export default BiometricVerification;

const styles = StyleSheet.create({
  container: {
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: RFValue(30),
    marginBottom: RFValue(12),
  },
  logo: {
    height: RFValue(28),
    width: RFValue(28),
    alignSelf: 'center',
    marginBottom: 10,
  },
  emailContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    marginTop: 18,
  },
  subText: {
    fontSize: RFValue(11),
  },
  logoutText: {
    fontFamily: FONTS.Regular,
    fontSize: RFValue(11),
  },
});
