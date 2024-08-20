import { UserModel } from "@/models/User";
import { WalletModel } from "@/models/Wallet";
import { albumApi, userApi } from "@/services/apiService";
import { createContext, useCallback, useEffect, useState } from "react";
import { Navigate } from "react-router-dom";

interface AuthContextModel extends UserModel, WalletModel {
  isAuthenticated: boolean;
  token: string;
  login: (email: string, password: string) => Promise<string | void>;
  logout: () => void;
  signup: (name: string, email: string, password: string) => Promise<string | void>;
  updateBalance: (newBalance: number) => Promise<void>;
  updateWallet: (newWalletModel: WalletModel) => Promise<void>;
}

export const AuthContext = createContext({} as AuthContextModel);

interface Props {
  children: React.ReactNode;
}

export const AuthProvider: React.FC<Props> = ({ children }) => {
  const [userData, setUserData] = useState<UserModel>();
  const [wallet, setWallet] = useState<WalletModel>();
  const [token, setToken] = useState<string>("");
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

  useEffect(() => {
    const data: UserModel = JSON.parse(localStorage.getItem('@Auth.Data') || "{}");
    if (data.id) {
      const token: string = JSON.parse(localStorage.getItem('@Auth.Token') || "{}");
      const wallet: WalletModel = JSON.parse(localStorage.getItem('@Wallet.Data') || "{}");
      setIsAuthenticated(true);
      setToken(token);
      setUserData(data);
      setWallet(wallet);
    }
  }, []);


  const Login = useCallback(async (email: string, password: string) => {
    const respAuth = await userApi.post('/api/users/auth', { email, password });

    if (respAuth instanceof Error) {
      return respAuth.message;
    }

    userApi.defaults.headers.common.Authorization = `Basic ${respAuth.data.token}`;
    albumApi.defaults.headers.common.Authorization = `Basic ${respAuth.data.token}`;
    const respUserInfo = await userApi.get(`/api/users/${respAuth.data.id}`);

    if (respUserInfo instanceof Error) {
      return respUserInfo.message;
    }

    const walletRes = await userApi.get("/api/wallet");

    if (walletRes instanceof Error) {
      return walletRes.message;
    }

    localStorage.setItem('@Auth.Token', JSON.stringify(respAuth.data.token));
    localStorage.setItem('@Auth.Data', JSON.stringify(respUserInfo.data));
    localStorage.setItem('@Wallet.Data', JSON.stringify(walletRes.data));

    setUserData(respUserInfo.data);
    setToken(respAuth.data.token);
    setWallet(walletRes.data);
    setIsAuthenticated(true);
  }, []);


  const Logout = useCallback(() => {
    localStorage.removeItem('@Auth.Token');
    localStorage.removeItem('@Auth.Data');
    localStorage.removeItem('@Wallet.Data');
    setUserData(undefined);
    setWallet(undefined);
    setIsAuthenticated(false);
    return <Navigate to='/' />;
  }, []);


  const Signup = useCallback(async (name: string, email: string, password: string) => {
    const createRes = await userApi.post('/api/users/create', { name, email, password });
    if (createRes instanceof Error) {
      return createRes.message;
    }

    const authRes = await userApi.post('/api/users/auth', { email, password });

    if (authRes instanceof Error) {
      return authRes.message;
    }

    userApi.defaults.headers.common.Authorization = `Basic ${authRes.data.token}`;
    albumApi.defaults.headers.common.Authorization = `Basic ${authRes.data.token}`;
    const walletRes = await userApi.get("/api/wallet");

    if (walletRes instanceof Error) {
      return walletRes.message;
    }

    const userData = {
      id: createRes.data.id,
      name: createRes.data.name,
      email: createRes.data.email,
      password: createRes.data.password,
    };

    localStorage.setItem('@Auth.Token', JSON.stringify(authRes.data.token));
    localStorage.setItem('@Auth.Data', JSON.stringify(userData));
    localStorage.setItem('@Wallet.Data', JSON.stringify(walletRes.data));

    setUserData(userData);
    setToken(authRes.data.token);
    setWallet(walletRes.data);
    setIsAuthenticated(true);
  }, []);


  const UpdateBalance = useCallback(async (newBalance: number) => {
    if (wallet) {
      wallet.balance = newBalance;
      localStorage.setItem('@Wallet.Data', JSON.stringify(wallet));
      setWallet(wallet);
    }
  }, []);

  const UpdateWallet = useCallback(async (newWalletModel: WalletModel) => {
    if (newWalletModel) {
      localStorage.setItem('@Wallet.Data', JSON.stringify(newWalletModel));
      setWallet(newWalletModel);
    }
  }, []);


  return (
    <AuthContext.Provider value={{
      isAuthenticated: isAuthenticated,
      token: token,
      ...wallet,
      ...userData,
      login: Login,
      logout: Logout,
      signup: Signup,
      updateBalance: UpdateBalance,
      updateWallet: UpdateWallet,
    }}>
      {children}
    </AuthContext.Provider>
  );
}
