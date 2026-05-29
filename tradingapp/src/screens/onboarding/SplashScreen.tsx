import React from 'react';
import { View, StyleSheet } from 'react-native';
import CustomSafeAreaView from '../../components/global/CustomSafeAreaView';
import DotLoading from '../../components/global/DotLoading';

const SplashScreen = () => {
  return (
    <CustomSafeAreaView>
      <View style={styles.container}>
        <DotLoading />
      </View>
    </CustomSafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default SplashScreen;
