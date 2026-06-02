import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import React, { FC } from 'react';
import { FONTS } from '../../constants/Fonts.tsx';
import CustomText from '../global/CustomText.tsx';
import { Colors } from '../../constants/Colors.tsx';

interface SocialLoginButtonProps {
  icon: React.ReactNode;
  text: string;
  onPress: () => void;
}


const SocialLoginButton:FC<SocialLoginButtonProps> = ({
  icon,
  text,
  onPress }) => {

  return (
    <TouchableOpacity
      style={[styles.socialButton]}
      activeOpacity={0.8}
      onPress={onPress}
    >
      {icon}
      <CustomText variant="h8" fontFamily={FONTS.Medium} style={styles.text}>
        {text}
      </CustomText>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  text: {
    marginLeft: 12,
    color: Colors.text,
    fontSize: 16,
  },
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 25,
    padding: 16,
    width: '100%',
  },
  socialButton: {
  flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 12,

    width: '78%',
    height: 54,

    marginTop: 36,
    borderRadius: 28,

    backgroundColor: 'rgba(255,255,255,0.06)',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.18)',
},
});

export default SocialLoginButton;
