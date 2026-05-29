import { View, ViewStyle, StyleSheet } from 'react-native';
import React, { FC } from 'react';
import { SafeAreaView } from 'react-native-safe-area-context';

interface CustomSafeAreaViewProps {
  children: React.ReactNode;
  style?: ViewStyle;
}

const CustomSafeAreaView:FC<CustomSafeAreaViewProps> = ({
  children, style
}) => {
  return (
   <>
     <SafeAreaView
       style={[styles.container, style]}
       edges={['top', 'left', 'right']}
     >
       <View style={[styles.container, style]}>{children}</View>
     </SafeAreaView>
   </>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 15,
    paddingHorizontal: 24
  },
});


export default CustomSafeAreaView;