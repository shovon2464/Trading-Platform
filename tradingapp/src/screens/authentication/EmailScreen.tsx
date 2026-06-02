import { View, Text, ScrollView } from 'react-native';
import React, { useState } from 'react';
import CustomSafeAreaView from '../../components/global/CustomSafeAreaView.tsx';
import BackButton from '../../components/global/BackButton.tsx';
import CenteredLogo from '../../components/global/CenteredLogo.tsx';
import CustomInput from '../../components/inputs/CustomInputs.tsx';
import { useAppDispatch } from '../../redux/reduxHook.tsx';
import { validateEmail } from '../../utils/ValidationUtils.tsx';

const EmailScreen = () => {
  const [loading, setLoading] = useState(false);
  const [email, setEmail] = useState("");
  const [emailError, setEmailError] = useState("");
  const dispatch = useAppDispatch();
  const validate = () => {
    if (!validateEmail(email)) {
      setEmailError("Please enter a valid email address");
      return false;
    }
    return true;
  }

  const handleOnSubmit = async () => {
    setLoading(true);
    if (validate()) {
      await dispatch(CheckEmail({ email: email.toLowerCase() }));
    }
    setLoading(false);
  }
  return (
    <CustomSafeAreaView>
      <BackButton path="LoginScreen" />

      <ScrollView>
        <CenteredLogo/>

        <CustomInput
          label="EMAIL ADDRESS"
          returnKeyType="done"
          value={email}
          inputMode="email"
          focusable={true}
          autoFocus={true}
          error={emailError}
          // onEndEditing-{() => validate()}
          onChangeText={(text) =>{
            setEmail(text);
            setEmailError("");
          }}
          placeholder="Eg: me@gmail.com"
          onSubmitEditing={handleOnSubmit}
        />
      </ScrollView>

    </CustomSafeAreaView>
  );
};

export default EmailScreen;