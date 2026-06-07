import { combineReducers } from 'redux';
import userReducer from './reducers/userSlice.tsx';
import stockReducer from './reducers/stockSlice.tsx';

const rootReducer = combineReducers({
  user: userReducer,
  stock: stockReducer
});

export default rootReducer;