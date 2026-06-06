 import { Platform } from "react-native";


export const BASE_URL =
  Platform.OS === "android"
    ? "http://10.0.2.2:8080"
    : "http://localhost:8080";

export const SOCKET_URL =
  Platform.OS === "android"
    ? "http://10.0.2.2:4000"
    : "http://localhost:4000";

export const TRADINGVIEW_WEB_URI =
  Platform.OS === "android"
    ? "http://10.0.2.2:3001"
    : "http://localhost:3001";


 export const CREATE_ACCOUNT = `${BASE_URL}/api/v1/users/create-account`;
 export const CHECK_EMAIL = `${BASE_URL}/api/v1/auth/check-email`;
 export const EMAIL_LOGIN = `${BASE_URL}/api/v1/auth/login`;
 export const REFRESH_TOKEN = `${BASE_URL}/api/v1/auth/refresh-token`;
 export const VERIFY_OTP = `${BASE_URL}/api/v1/auth/verify-otp`;
 export const SEND_OTP = `${BASE_URL}/api/v1/auth/send-otp`;
 export const REGISTER = `${BASE_URL}/api/v1/auth/register`;
 export const OAUTH = `${BASE_URL}/api/v1/auth/oauth`;

