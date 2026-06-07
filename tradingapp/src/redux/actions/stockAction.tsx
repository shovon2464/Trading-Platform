import { setHoldings, setStocks } from '../reducers/stockSlice.tsx';
import { appAxios } from '../../connectors/apiConfig.tsx';
import { navigate } from '../../utils/NavigationUtil.tsx';
import { formatCentsWithCommas } from '../../utils/NumberUtils.tsx';
import { refetchUser } from './userAction.tsx';

export const getAllStocks = () => async (dispatch: any) => {
  try {
    const res = await appAxios.get('/api/v1/stocks');
    await dispatch(setStocks(res.data));
  } catch (error: any) {
    console.log('GET STOCK ERROR ->', error?.response?.data || error.message);
  }
};

export const getAllHoldings = () => async (dispatch: any) => {
  try {
    const res = await appAxios.get(`api/v1/holdings`);
    await dispatch(setHoldings(res.data.data));
  } catch (error: any) {
    console.log('GET HOLDING ERROR ->', error);
  }
};


interface buyStockPayload {
  stock_id: string;
  quantity: number;
  amount: number;
  companyName: number;
}
interface sellStockPayload {
  holdingId: string;
  quantity: number;
  amount: number;
  companyName: number;
}

export const buyStock = (payload: buyStockPayload) => async (dispatch: any) => {
  try {
    const res = await appAxios.post(`/api/v1/holdings/buy`, payload);

    navigate('TransactionSuccess', {
      msg: `Your investment of  ${formatCentsWithCommas(
        payload.amount,
      )} completed ${payload.companyName} `,
    });

    await dispatch(refetchUser());
  } catch (error: any) {
    console.log('BUY STOCK ERROR ->', error);
  }
};

export const sellStock =
  (payload: sellStockPayload) => async (dispatch: any) => {
    try {
      const res = await appAxios.post(`/api/v1/holdings/sell`, payload);
      navigate('TransactionSuccess', {
        msg: `Your holding got sold  ${formatCentsWithCommas(
          payload.amount,
        )}  ${payload.companyName} `,
      });

      await dispatch(refetchUser());
    } catch (error: any) {
      console.log('SELL STOCK ERROR ->', error);
    }
  };

