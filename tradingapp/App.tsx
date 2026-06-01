import React from 'react';
import Navigation from './src/navigation/Navigation.tsx';
import { persistor, store } from './src/redux/store.tsx';
import { PersistGate } from 'redux-persist/integration/react';
import { Provider } from 'react-redux';
import { GestureHandlerRootView } from 'react-native-gesture-handler';
import Toast from 'react-native-toast-message';
import { toastConfig } from './ToastConfig.tsx';


const App = () => {
  return (
    <GestureHandlerRootView style={{flex:1}}>
    <Provider store={store}>
      <PersistGate loading={null} persistor={persistor}>
        <Navigation/>
      </PersistGate>
    </Provider>
      <Toast
        visibilityTime={2500}
        config={toastConfig}
        bottomOffset={0}
        swipeable={false}
        position="bottom"
      />
  </GestureHandlerRootView>
  );
};

export default App;