import { View, StyleSheet } from 'react-native';
import React, { useState } from 'react';
import { validatePasswordEntry } from '../../utils/ValidationUtils';
import CustomSafeAreaView from '../../components/global/CustomSafeAreaView';
import BackButton from '../../components/global/BackButton';
import CustomInput from '../../components/inputs/CustomInputs';
import CustomButton from '../../components/global/CustomButton';
import { GlobalStyles } from '../../styles/GlobalStyles';
import GuidelineText from '../../components/global/GuidelineText';
import { navigate } from '../../utils/NavigationUtil';
import axios from 'axios';
import { CREATE_ACCOUNT } from '../../connectors/API.tsx';

interface PasswordInputs {
  password: string;
  confirmpassword: string;
}

const CreatePasswordScreen = ({ route }: any) => {
  const [inputs, setInputs] = useState<PasswordInputs>({
    password: '',
    confirmpassword: '',
  });

  const [errors, setErrors] = useState<{ [key: string]: string | undefined }>();
  const [loading, setLoading] = useState(false);

  const handleOnChange = (text: string, fieldName: string) => {
    setInputs(prevInputs => ({
      ...prevInputs,
      [fieldName]: text,
    }));
    // Clear the error when the user starts typing again
    setErrors(prevErrors => ({
      ...prevErrors,
      [fieldName]: undefined,
    }));
  };

  const validateForm = () => {
    const newErrors: { [key: string]: string | undefined } = {};
    if (!inputs.password.trim()) {
      newErrors.password = 'Enter a password';
    }
    if (!inputs.confirmpassword.trim()) {
      newErrors.confirmpassword = 'Confirm your password';
    }
    if (
      !validatePasswordEntry(inputs.password, 'test', route?.params?.email)
        .result
    ) {
      newErrors.password =
        'Set a stronger password, kindly refer to guidelines below.';
    }

    if (inputs?.confirmpassword !== inputs?.password) {
      newErrors.confirmpassword = 'Passwords do not match';
    }

    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;
  };

  const handleCreateAccount = async () => {
    if (validateForm()) {
      setLoading(true);
      try {
        const payload = {
          email: route.params?.email,
          password: inputs.password,
        };

        // Call your Spring Boot API
        await axios.post(CREATE_ACCOUNT, payload,);

        // On success, navigate directly to PersonalDetailsScreen
        navigate('PersonalDetailsScreen', { email: route.params?.email });
      } catch (error) {
        console.log('Account creation error:', error);
        // Show an error below the confirm password box if the API fails
        setErrors({ confirmpassword: 'Failed to create account. Try again.' });
      } finally {
        setLoading(false);
      }
    }
  };

  return (
    <CustomSafeAreaView>
      <BackButton />

      <CustomInput
        label="CREATE PASSWORD"
        placeholder="8-20 Characters"
        value={inputs?.password}
        error={errors?.password}
        onChangeText={text => handleOnChange(text, 'password')}
        password
      />

      <CustomInput
        label="CONFIRM PASSWORD"
        placeholder="8-20 Characters"
        error={errors?.confirmpassword}
        value={inputs.confirmpassword}
        onChangeText={text => handleOnChange(text, 'confirmpassword')}
        onSubmitEditing={handleCreateAccount}
        password
      />

      <View style={GlobalStyles.bottomBtn}>
        <GuidelineText
          text={[
            'Password must have at least one uppercase and lowercase letter.',
            'Must contain at least one number and one special character',
            "Must not contain user's first/last name & email id",
          ]}
        />

        <CustomButton
          disabled={loading}
          loading={loading}
          text="CREATE ACCOUNT"
          onPress={handleCreateAccount}
        />
      </View>
    </CustomSafeAreaView>
  );
};

export default CreatePasswordScreen;
