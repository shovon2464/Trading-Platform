import React, { useState } from 'react';
import { ScrollView, StyleSheet, Text, View } from 'react-native';
import { useAppDispatch } from '../../redux/reduxHook.tsx';
import { updateProfile } from '../../redux/actions/userAction.tsx';
import CustomSafeAreaView from '../../components/global/CustomSafeAreaView.tsx';
import BackButton from '../../components/global/BackButton.tsx';
import CustomText from '../../components/global/CustomText.tsx';
import { FONTS } from '../../constants/Fonts.tsx';
import CustomInput from '../../components/inputs/CustomInputs.tsx';
import CustomButton from '../../components/global/CustomButton.tsx';
import { GlobalStyles } from '../../styles/GlobalStyles.tsx';
import CustomRadioInput from '../../components/inputs/CustomRadioInput.tsx';


interface Inputs {
  name: string;
  date_of_birth: string;
  gender: string;
}

const PersonalDetailsScreen = () => {
  const [inputs, setInputs] = useState<Inputs>({
    name: '',
    date_of_birth: '',
    gender: '',
  });
  const dispatch = useAppDispatch();
  const [errors, setErrors] = useState<{ [key: string]: string | undefined }>(
    {},
  );
  const [loading, setLoading] = useState(false);
  const [isFormValid, setIsFormValid] = useState(false);

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

    if (!inputs.name.trim()) {
      newErrors.name = 'Name is required';
    }
    if (!inputs.date_of_birth.trim()) {
      newErrors.date_of_birth = 'Date is required';
    }
    if (!inputs.gender.trim()) {
      newErrors.gender = 'Gender is required';
    }

    setErrors(newErrors);

    setIsFormValid(Object.keys(newErrors).length === 0);

    return Object.keys(newErrors).length === 0;
  };

  const handleOnSubmit = async () => {
    if (validateForm()) {
      setLoading(true);
      await dispatch(updateProfile(inputs));
      setLoading(false);
    }
  };

  return(
    <CustomSafeAreaView>
      <BackButton />
      <CustomText variant="h4" fontFamily={FONTS.Bold} style={styles.headText}>
        Personal Details
      </CustomText>

      <ScrollView
        contentContainerStyle={{
          marginTop: 20,
          flex: 1,
          flexDirection: 'column',
          gap: 20,
        }}
      >
        <CustomInput
          label="NAME (AS PER YOUR DRIVER'S LISENCE)"
          returnKeyType="done"
          value={inputs.name}
          error={errors?.name}
          onChangeText={text => {
            handleOnChange(text, 'name');
          }}
        />


        <CustomRadioInput
          label="GENDER"
          error={errors?.gender}
          options={['MALE', 'FEMALE', 'OTHER']}
          onSelect={(text: string) => {
            return handleOnChange(text, 'gender');
          }}
          selected={inputs?.gender}
        />
      </ScrollView>
      <View style={GlobalStyles.bottomBtn}>
        <CustomButton
          text="NEXT"
          loading={loading}
          disabled={false}
          onPress={handleOnSubmit}
        />
      </View>
    </CustomSafeAreaView>
  );
};

const styles = StyleSheet.create({
  headText: {
    marginVertical: 10,
  },
});

export default PersonalDetailsScreen;