import { StyleSheet, Text, TextStyle, TouchableOpacity } from 'react-native';
import { FC } from 'react';
import { RFPercentage } from 'react-native-responsive-fontsize';
import { FONTS } from '../../constants/Fonts.tsx';
import { Colors } from '../../constants/Colors.tsx';


interface EmailLoginButtonProps {
  firstText: string;
  style?: TextStyle;
  onPress: () => void;
}

const EmailLoginButton:FC<EmailLoginButtonProps> = ({
  firstText,
  style,
  onPress
}) => {
  return (
    <TouchableOpacity onPress={onPress}>
      <Text style={[styles.bottomText, { color: Colors.active_tab }, style]}>
        {firstText}
      </Text>
    </TouchableOpacity>
  );
};


const styles = StyleSheet.create({
  bottomText: {
    fontFamily: FONTS.Medium,
    fontSize: RFPercentage(1.6),
    textDecorationLine: "underline",
  }
});

export default EmailLoginButton;