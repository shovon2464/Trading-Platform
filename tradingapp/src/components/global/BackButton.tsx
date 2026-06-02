import { FC } from 'react';
import { StyleSheet, TouchableOpacity } from 'react-native';
import { RFValue } from 'react-native-responsive-fontsize';
import Icon from 'react-native-vector-icons/Ionicons';
import { useTheme } from '@react-navigation/core';
import { goBack, navigate } from '../../utils/NavigationUtil.tsx';


interface BackButtonProps {
  path?: string;
}

const BackButton: FC<BackButtonProps> = ({
  path
}) => {
  const { colors } = useTheme();

  return (
    <TouchableOpacity
      style={styles.container}
      onPress={() => {
        path ? navigate(path) : goBack();
      }}
    >
      <Icon name="arrow-back" color={colors.text} size={RFValue(20)} />
    </TouchableOpacity>
  );
};

const styles= StyleSheet.create({
  container: {
    width: "100%",
    justifyContent: "flex-start",
    alignItems: "flex-start",
    paddingVertical: 4,
    marginBottom: 8,
  },
});
export default BackButton;