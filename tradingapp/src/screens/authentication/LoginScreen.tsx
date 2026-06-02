import { View, Text, StyleSheet, Image } from 'react-native';
import React from 'react'
import CustomSafeAreaView from '../../components/global/CustomSafeAreaView.tsx';
import { useAppDispatch } from '../../redux/reduxHook.tsx';
import Logo from '../../assets/images/logo.png';
import GoogleIcon from '../../assets/images/google.png';
import CustomText from '../../components/global/CustomText.tsx';
import { FONTS } from '../../constants/Fonts.tsx';
import { screenHeight, screenWidth } from '../../utils/Scaling.tsx';
import SocialLoginButton from '../../components/authentication/SocialLoginButton.tsx';
import EmailLoginButton from '../../components/authentication/EmailLoginButton';
import Icon from 'react-native-vector-icons/Ionicons';
import { signInWithApple, signInWithGoogle } from '../../redux/SocialLogin.tsx';
import { navigate } from '../../utils/NavigationUtil.tsx';


const LoginScreen = () => {
  const dispatch = useAppDispatch();
  return (
    <CustomSafeAreaView>
      <View style={styles.container}>
        <View style={styles.imgContainer}>
          <Image style={styles.img} source={Logo} />
        </View>

        <CustomText
          variant="h1"
          fontFamily={FONTS.Medium}
          style={{ color: 'white' }}
        >
          Trading Made Simple
        </CustomText>

        <CustomText variant="h7" style={styles.subText} fontFamily={FONTS.Bold}>
          Trade • Invest • Success
        </CustomText>

        <SocialLoginButton
          icon={<Image source={GoogleIcon} style={styles.gimg} />}
          text="Continue with Google"
          onPress={async () => signInWithGoogle(dispatch)}
        />

        <SocialLoginButton
          icon={<Icon name="logo-apple" size={25} color="black"/>}
          text="Continue with Apple"
          onPress={async () => signInWithApple(dispatch)}
        />

        <EmailLoginButton
          firstText="Sign in with Email"
          onPress={() => navigate("EmailScreen")}
          style={styles.touchText}
        />

        <CustomText variant="h7" fontFamily={FONTS.Medium} style={styles.text}>
          Made by Shovon Bhowmick
        </CustomText>
      </View>
    </CustomSafeAreaView>
  );
}


const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingBottom: 40,
  },

  imgContainer: {
    marginBottom: 24,
  },

  img: {
    width: 180,
    height: 180,
    resizeMode: 'contain',
  },

  subText: {
    marginTop: 8,
    marginBottom: 40,
  },

  gimg: {
    height: 20,
    width: 20,
  },

  text: {
    opacity: 0.6,
    position: 'absolute',
    bottom: 24,
    textAlign: 'center',
  },

  touchText: {
    marginVertical: 30,
    marginTop: 15,
  }

});

export default LoginScreen;