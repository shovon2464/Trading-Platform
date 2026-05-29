import { View, Text, TextStyle, TextProps, StyleSheet } from 'react-native';
import React, { FC } from 'react';
import { FONTS } from '../../constants/Fonts.tsx';
import { useTheme } from '@react-navigation/core';
import { RFValue } from 'react-native-responsive-fontsize';
import {
  computeWindowedRenderLimits
} from '@react-native/virtualized-lists/types_generated/Lists/VirtualizeUtils';
import styles from 'react-native-webview/lib/WebView.styles';

interface Props {
  variant?:
    | "h1"
    | "h2"
    | "h3"
    | "h4"
    | "h5"
    | "h6"
    | "h7"
    | "h8"
    | "h9"
    | "body";
  fontFamily?: FONTS;
  fontSize?: number;
  style?: TextStyle;
  children?: React.ReactNode;
  numberOfLines?: number;
  onLayout?: (event: object) => void;
}

const CustomText: FC<Props> = ({
  variant = 'body',
  fontFamily = FONTS.Regular,
  fontSize,
  style,
  children,
  numberOfLines,
  onLayout,
}) => {
  const { colors} = useTheme();

  let computedFontSize: number;

  switch (variant) {
    case 'h1':
      computedFontSize = RFValue(fontSize || 22);
      break;
    case 'h2':
      computedFontSize = RFValue(fontSize || 20);
      break;
    case 'h3':
      computedFontSize = RFValue(fontSize || 18);
      break;
    case 'h4':
      computedFontSize = RFValue(fontSize || 16);
      break;
    case 'h5':
      computedFontSize = RFValue(fontSize || 14);
      break;
    case 'h6':
      computedFontSize = RFValue(fontSize || 12);
      break;
    case 'h7':
      computedFontSize = RFValue(fontSize || 10);
      break;
    case 'h8':
      computedFontSize = RFValue(fontSize || 6);
      break;
    case 'h9':
      computedFontSize = RFValue(fontSize || 4);
      break;
    case 'body':
      computedFontSize = RFValue(fontSize || 14);
      break;
    default:
      computedFontSize = RFValue(fontSize || 14);
      break;
  }

  const fontFamilyStyle = {
    fontFamily:
      fontFamily === FONTS.Black
        ? 'Roboto-Black'
        : fontFamily === FONTS.Bold
        ? 'Roboto-Bold'
        : fontFamily === FONTS.Light
        ? 'Roboto-Light'
        : fontFamily === FONTS.Medium
        ? 'Roboto-Medium'
        : fontFamily === FONTS.Number
        ? 'Manrope-Regular'
        : fontFamily === FONTS.NumberSemiBold
        ? 'Manrope-SemiBold'
        : fontFamily === FONTS.Lato
        ? 'Lato-Regular'
        : fontFamily === FONTS.Thin
        ? 'Roboto-Thin'
        : 'Roboto-Regular',
  };

  return (
    <Text
      style={[
        styles2.text,
        { color: colors.text, fontSize: computedFontSize },
        fontFamilyStyle,
        style,
      ]}
      numberOfLines={numberOfLines}
      onLayout={onLayout}
    >
      {children}
    </Text>
  );
};


const styles2 = StyleSheet.create({
  text: {
    textAlign: "left",
  },
});

export default CustomText;