import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import type { RootState } from '../store.tsx';

interface UserState {
  user: {
    fullName?: string;
    id?: string;
    email?: string;
    phoneExist?: boolean;
    balance?: number;
  };
}

const initialState:UserState = {
  user: {}
}


export const userSlice = createSlice({
  name: "user",
  initialState: initialState,
  reducers: {
    setUser: (state, action: PayloadAction<UserState["user"]>) => {
      state.user = action.payload;
    },
  },
});


export const { setUser } = userSlice.actions;

export const selectUser = (state: RootState) => state.user.user;

export default userSlice.reducer;