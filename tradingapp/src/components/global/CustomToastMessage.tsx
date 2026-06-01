import { View, Text, Platform, StyleSheet, Animated } from 'react-native';
import React, { FC } from 'react';
import { Colors } from '../../constants/Colors.tsx';
import { RFPercentage, RFValue } from 'react-native-responsive-fontsize';
import Icon from 'react-native-vector-icons/MaterialIcons';
import CustomText from './CustomText.tsx';
import { FONTS } from '../../constants/Fonts.tsx';

interface CustomToastMessageProps {
  type: string;
  msg: string;
}

const CustomToastMessage:FC<CustomToastMessageProps> = ({ type, msg }) => {
  let bgColor = `${Colors.dark_background_light}`;
  let textColor = Colors.dark_text;

  switch (type) {
    case 'warningToast':
      bgColor = '#fcba03';
      textColor = Colors.light_text;
      break;
    default:
      break;
  }

  return (
    <Animated.View style={[styles.modal, { backgroundColor: bgColor }]}>
      <View style={styles.subContainer}>
        {type === 'successToast' && (
          <Icon
            name="checkmark-circle-sharp"
            size={RFValue(16)}
            color={Colors.themeColor}
          />
        )}

        {type === 'warningToast' && (
          <Icon
            name="warning-amber-sharp"
            size={RFValue(16)}
            color={Colors.themeColor}
          />
        )}

        {type === 'normalToast' && (
          <Icon
            name="info-outline"
            size={RFValue(16)}
            color={Colors.themeColor}
          />
        )}

        <CustomText
          style={{color: textColor}}
          variant="h7"
          fontFamily={FONTS.Medium}
          >
          {msg}
        </CustomText>
      </View>
    </Animated.View>
  );
}

const styles = StyleSheet.create({
  subContainer: {
    flexDirection: "row",
    alignItems: "center",
    gap: 12,
  },
  text: {
    color: Colors.dark_text,
  },
  modal: {
    paddingTop: 18,
    paddingBottom: Platform.OS === 'ios' ? RFPercentage(4.5) : 18,
    paddingHorizontal: RFPercentage(3.5),
    alignSelf: "center",
    position: "absolute",
    bottom: 0,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    width: "100%",
  }
})

export default CustomToastMessage;