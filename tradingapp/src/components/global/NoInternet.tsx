import { View, Text, StyleSheet } from 'react-native';
import React, { useEffect, useState } from 'react';
import { useTheme } from '@react-navigation/core';
import NetInfo from '@react-native-community/netinfo';
import { SafeAreaView } from 'react-native-safe-area-context';
import Icon from 'react-native-vector-icons/MaterialIcons';
import { RFValue } from 'react-native-responsive-fontsize';
import CustomText from './CustomText.tsx';
import { FONTS } from '../../constants/Fonts.tsx';


const NoInternet = () => {

  const { colors } = useTheme();
  const [isConnected, setIsConnected] = useState(false);

  useEffect(() => {
    const unsubscribe = NetInfo.addEventListener((state: any) => {
      setIsConnected(state.isConnected);
    });
    return () => {
      unsubscribe();
    };
  }, []);

  return (
    <>
      {! isConnected && (
       <SafeAreaView
         style={[
           styles.container,
           {
             backgroundColor: colors.background,
           },
         ]}
         >
         <View
           style={[
             styles.subContainer,
             {
               backgroundColor: colors.card,
             },
           ]}
           >
           <Icon name="wifi-off" size={RFValue(24)} color={colors.text} />
           <View>
             <CustomText
               variant="h7"
               fontFamily={FONTS.Medium}
             >
               No Internet Connection
             </CustomText>
             <CustomText
               variant="h8"
               fontFamily={FONTS.Medium}
               style={styles.bottomText}
             >
               Please check your internet connection and try again.
             </CustomText>
           </View>
         </View>
       </SafeAreaView>
      )}
    </>
  );
};


const styles = StyleSheet.create({
  container:{
    position: "absolute",
    bottom: 0,
    width: '100%',
  },
  subContainer:{
    justifyContent: 'flex-start',
    alignItems: 'center',
    gap:24,
    paddingHorizontal:24,
    paddingVertical:18,
    flexDirection: 'row',
    width: '100%',
  },
  bottomText:{
    opacity:0.87,
    marginTop:4,
  }
})

export default NoInternet;