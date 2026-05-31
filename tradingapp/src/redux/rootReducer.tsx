import { combineReducers } from 'redux';
import userReducer from './reducers/userSlice.tsx';

const rootReducer = combineReducers({
  user: userReducer,
});

export default rootReducer;