import { View, StyleSheet, TouchableOpacity, ScrollView } from 'react-native';
import React, { FC, useEffect } from 'react';
import { Colors } from '../../constants/Colors';
import { RFValue } from 'react-native-responsive-fontsize';
import CustomText from '../../components/global/CustomText';
import { FONTS } from '../../constants/Fonts';
import { useAppDispatch, useAppSelector } from '../../redux/reduxHook';
import { selectUser } from '../../redux/reducers/userSlice';
import { useTheme } from '@react-navigation/native';
import { Logout, refetchUser } from '../../redux/actions/userAction';
import CustomSafeAreaView from '../../components/global/CustomSafeAreaView';
import ProfileHeader from '../../components/authentication/ProfileHeader';
import UserAvatar from '../../components/dashboard/UserAvatar';
import Icon from 'react-native-vector-icons/MaterialIcons';
import Icon2 from 'react-native-vector-icons/MaterialCommunityIcons';

interface ProfileItemProps {
  icon: React.ReactNode;
  title: string;
  description: string;
  onPress?: () => void;
  danger?: boolean;
}

const ProfileItem: FC<ProfileItemProps> = ({
  icon,
  title,
  description,
  onPress,
  danger = false,
}) => {
  return (
    <TouchableOpacity onPress={onPress} style={[styles.itemCard]}>
      <View style={styles.flexRow}>
        {icon}
        <View style={{ flex: 1 }}>
          <CustomText
            fontFamily={FONTS.Medium}
            variant="h7"
            style={danger ? { color: 'red' } : {}}
          >
            {title}
          </CustomText>
          <CustomText variant="h9" style={{ opacity: 0.6, marginTop: 3 }}>
            {description}
          </CustomText>
        </View>
      </View>
    </TouchableOpacity>
  );
};

const ProfileScreen = () => {
  const user = useAppSelector(selectUser);
  const dispatch = useAppDispatch();
  const { colors } = useTheme();

  useEffect(() => {
    dispatch(refetchUser());
  }, []);

  return (
    <CustomSafeAreaView>
      <ProfileHeader />

      <ScrollView
        contentContainerStyle={{ paddingTop: 25 }}
        showsVerticalScrollIndicator={false}
      >
        <View style={[styles.profileCard, { backgroundColor: colors.card }]}>
          <UserAvatar style={styles.avatarLarge} />
          <CustomText
            fontFamily={FONTS.Medium}
            variant="h5"
            style={{ marginTop: 10 }}
          >
            {user?.fullName}
          </CustomText>
          <CustomText variant="h9" style={{ opacity: 0.6, marginTop: 4 }}>
            Account Details
          </CustomText>
        </View>

        <CustomText variant="h8" style={styles.sectionLabel}>
          Quick Actions
        </CustomText>
        <ProfileItem
          icon={
            <Icon2
              name="gift"
              size={RFValue(20)}
              style={styles.icon}
              color={colors.text}
            />
          }
          title="Refer"
          description="Invite friends to the app"
        />
        <ProfileItem
          icon={
            <Icon
              name="account-balance-wallet"
              size={RFValue(20)}
              style={styles.icon}
              color={colors.text}
            />
          }
          title={`$${user?.balance ?? 0}`}
          description="Stocks, F&O Balance"
        />

        <CustomText variant="h8" style={styles.sectionLabel}>
          Account
        </CustomText>
        <ProfileItem
          icon={
            <Icon
              name="receipt"
              size={RFValue(20)}
              style={styles.icon}
              color={colors.text}
            />
          }
          title="All Orders"
          description="Track orders, order details"
        />
        <ProfileItem
          icon={
            <Icon2
              name="bank"
              size={RFValue(20)}
              style={styles.icon}
              color={colors.text}
            />
          }
          title="Bank detail"
          description="Banks & AutoPay mandates"
        />

        <CustomText variant="h8" style={styles.sectionLabel}>
          Danger Zone
        </CustomText>
        <ProfileItem
          onPress={async () => {
            await dispatch(Logout());
          }}
          icon={
            <Icon
              name="exit-to-app"
              size={RFValue(20)}
              style={styles.icon}
              color={'red'}
            />
          }
          title="Logout"
          description="Logout from the app"
          danger
        />
      </ScrollView>
    </CustomSafeAreaView>
  );
};

const styles = StyleSheet.create({
  profileCard: {
    alignItems: 'center',
    padding: 20,
    borderRadius: 12,
    marginBottom: 20,
    shadowColor: '#000',
    shadowOpacity: 0.05,
    shadowRadius: 4,
    elevation: 2,
  },
  avatarLarge: {
    width: RFValue(60),
    height: RFValue(60),
    borderRadius: RFValue(30),
  },
  sectionLabel: {
    marginTop: 20,
    marginBottom: 8,
    opacity: 0.7,
  },
  itemCard: {
    backgroundColor: Colors.background_light,
    padding: 16,
    borderRadius: 10,
    marginBottom: 12,
    shadowColor: '#000',
    shadowOpacity: 0.05,
    shadowRadius: 3,
    elevation: 1,
  },
  flexRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
  },
  icon: {
    opacity: 0.6,
    marginRight: 12,
  },
});

export default ProfileScreen;
