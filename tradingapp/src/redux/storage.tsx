import { Storage } from 'redux-persist';
import { createMMKV } from 'react-native-mmkv';

const storage = createMMKV();

export const token_storage = createMMKV({
  id: 'user-storage',
});

const reduxStorage: Storage = {
  setItem: (key, val) => {
    storage.set(key, val);
    return Promise.resolve(true);
  },
  getItem: key => {
    const value = storage.getString(key);
    return Promise.resolve(value);
  },
  removeItem: key => {
    storage.remove(key);
    return Promise.resolve();
  },
};

export default reduxStorage;
