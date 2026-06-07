import {
  View,
  Text,
  ScrollView,
  StyleSheet,
  TouchableOpacity,
  Image,
  RefreshControl,
} from 'react-native';
import React, { FC, useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/reduxHook';
import { selectStocks } from '../../redux/reducers/stockSlice';
import { getAllStocks } from '../../redux/actions/stockAction';
import { SafeAreaView } from 'react-native-safe-area-context';
import MainHeader from '../../components/dashboard/MainHeader';
import { RFValue } from 'react-native-responsive-fontsize';
import { Colors } from '../../constants/Colors';
import CustomText from '../../components/global/CustomText';
import { useTheme } from '@react-navigation/native';
import { FONTS } from '../../constants/Fonts';
import DottedLine from '../../assets/images/dotted.png';
import StockCard from '../../components/stocks/StockCard';
import MiniStockList from '../../components/stocks/MiniStockList';
import Holdings from '../../components/stockholdings/Holdings.tsx';

interface SepratorProps {
  label: string;
  seeMore?: boolean;
}
const Seprator: FC<SepratorProps> = ({ label, seeMore }) => {
  const { colors } = useTheme();
  return (
    <View style={styles.sectionContainer}>
      <CustomText fontFamily={FONTS.Medium} fontSize={RFValue(10)}>
        {label}
      </CustomText>
      {seeMore && (
        <TouchableOpacity style={styles.seeMore}>
          <CustomText fontFamily={FONTS.Medium} variant="h8">
            See more
          </CustomText>
          <Image
            source={DottedLine}
            style={{
              height: 2,
              marginTop: 2,
              width: '100%',
              tintColor: colors.text,
            }}
          />
        </TouchableOpacity>
      )}
    </View>
  );
};

const Stock = () => {
  const dispatch = useAppDispatch();
  const stockData = useAppSelector(selectStocks) ?? [];
  const [refreshing, setRefreshing] = useState(false);
  const [activeTab, setActiveTab] = useState<'stocks' | 'holdings'>('stocks');

  const fetchStocks = async () => {
    await dispatch(getAllStocks());
    setRefreshing(false);
  };

  const refreshHandler = async () => {
    setRefreshing(true);
    await fetchStocks();
  };

  useEffect(() => {
    fetchStocks();
  }, []);

  return (
    <SafeAreaView>
      <MainHeader />

      <ScrollView
        style={styles.container}
        contentContainerStyle={{ paddingBottom: 100 }}
        nestedScrollEnabled
        refreshControl={
          <RefreshControl
            colors={[Colors.themeColor]}
            tintColor={Colors.themeColor}
            onRefresh={refreshHandler}
            refreshing={refreshing}
          />
        }
      >
        <Seprator label="Most Popular Stocks" />

        <StockCard data={stockData} />

        <Seprator label="Recently Stocks" />

        <View style={styles.tabContainer}>
          <TouchableOpacity
            style={[styles.tab, activeTab === 'stocks' && styles.activeTab]}
            onPress={() => setActiveTab('stocks')}
          >
            <CustomText
              fontFamily={FONTS.Medium}
              style={{ color: activeTab === 'stocks' ? '#fff' : '#888' }}
            >
              Stocks
            </CustomText>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.tab, activeTab === 'holdings' && styles.activeTab]}
            onPress={() => setActiveTab('holdings')}
          >
            <CustomText
              fontFamily={FONTS.Medium}
              style={{ color: activeTab === 'holdings' ? '#fff' : '#888' }}
            >
              Holdings
            </CustomText>
          </TouchableOpacity>
        </View>

        {activeTab === 'stocks' ? (
          <MiniStockList data={stockData.slice(4)} />
        ) : (
          <Holdings />
        )}
      </ScrollView>
    </SafeAreaView>
  );
};

export default Stock;

const styles = StyleSheet.create({
  sectionContainer: {
    marginBottom: 18,
    marginTop: 20,
    paddingRight: 6,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
  },
  seeMore: {
    overflow: 'hidden',
    top: 3,
  },
  container: {
    paddingHorizontal: RFValue(16),
    marginTop: 10,
  },
  tabContainer: {
    flexDirection: 'row',
    marginBottom: 16,
    borderRadius: 8,
    overflow: 'hidden',
  },
  tab: {
    flex: 1,
    paddingVertical: 10,
    alignItems: 'center',
    backgroundColor: Colors.background_light,
  },
  activeTab: {
    backgroundColor: Colors.active_tab,
  },
});
