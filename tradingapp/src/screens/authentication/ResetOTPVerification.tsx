import {
  View,
  Text,
  KeyboardAvoidingView,
  StyleSheet,
  ScrollView,
  TextInput,
} from 'react-native';
import React, { FC, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/reduxHook';
import { selectUser } from '../../redux/reducers/userSlice';
import { useTheme } from '@react-navigation/native';
import { sendOtp, verifyOtp } from '../../redux/actions/userAction';
import { RFValue } from 'react-native-responsive-fontsize';
import { Colors } from '../../constants/Colors';
import { FONTS } from '../../constants/Fonts';
import CustomSafeAreaView from '../../components/global/CustomSafeAreaView';
import Icon from 'react-native-vector-icons/MaterialIcons';
import CustomText from '../../components/global/CustomText';
import OtpTimer from '../../components/authentication/OtpTimer';
import CustomButton from '../../components/global/CustomButton';

interface pin {
  pin: string;
}

const ResetOTPVerification: FC<pin> = ({ pin }) => {
  const dispatch = useAppDispatch();
  const user = useAppSelector(selectUser);

  const [loading, setLoading] = useState(false);
  const [otpError, setOtpError] = useState<string | null>(null);
  const [otp, setOtp] = useState<string>('');
  const { colors } = useTheme();

  const handleVerification = async () => {
    setLoading(true);
    if (!otp) {
      setLoading(false);
      setOtpError('Enter OTP');
      return;
    }

    await dispatch(
      verifyOtp({
        email: user.email || '',
        data: pin,
        otp: otp,
        otp_type: 'reset_pin',
      }),
    );

    setLoading(false);
  };

  const handleChange = (text: string) => {
    setOtp(text);
    setOtpError(null);
  };

  const resendOtp = async () => {
    await dispatch(sendOtp({ email: user.email || '', otp_type: 'reset_pin' }));
  };

  return (
    <KeyboardAvoidingView
      keyboardVerticalOffset={10}
      behavior="padding"
      style={styles.keyboardContainer}
    >
      <CustomSafeAreaView>
        <ScrollView contentContainerStyle={styles.container}>
          <Icon color={Colors.profit} name="lock" size={RFValue(22)} />
          <CustomText variant="h6" fontFamily={FONTS.Bold} style={styles.title}>
            Verify Identity
          </CustomText>

          <CustomText style={styles.subText}>
            Enter OTP sent to your registered mail
          </CustomText>

          <TextInput
            value={otp}
            maxLength={6}
            onChangeText={handleChange}
            autoFocus
            keyboardType="number-pad"
            style={[styles.input, { color: colors.text }]}
            caretHidden
          />

          {otpError && <Text style={styles.errorText}>{otpError}</Text>}

          <OtpTimer
            onPress={() => resendOtp()}
            type="otp"
            style={styles.timer}
          />
        </ScrollView>

        <View style={styles.btnContainer}>
          <CustomButton
            text={'VERIFY'}
            onPress={handleVerification}
            loading={loading}
            disabled={false}
          />
        </View>
      </CustomSafeAreaView>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: {
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 20,
    marginBottom: RFValue(10),
  },
  errorText: {
    color: Colors.errorColor,
    fontSize: RFValue(11),
    fontFamily: FONTS.Regular,
    marginTop: 20,
  },
  btnContainer: {
    justifyContent: 'flex-end',
    flex: 1,
  },
  emailContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
    marginTop: 15,
  },
  subText: {
    fontSize: RFValue(10),
    marginTop: 15,
    opacity: 0.8,
  },
  logoutText: {
    fontFamily: FONTS.Regular,
    fontSize: RFValue(10),
  },
  keyboardContainer: {
    flex: 1,
  },
  title: {
    marginTop: 20,
  },
  input: {
    marginTop: 80,
    fontSize: RFValue(18),
    borderBottomWidth: 2,
    borderBottomColor: Colors.border,
    width: '30%',
    textAlign: 'center',
  },
  timer: {
    fontSize: RFValue(10),
    marginTop: 60,
  },
});

export default ResetOTPVerification;
