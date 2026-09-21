import {createContext, useContext, useState} from 'react';
import * as authApi from '../api/authApi';

const AuthContext =createContext(null);

export function AuthProvider({children}){
    const [token ,setToken]=useState(localStorage.getItem('tts_token'));
    const [email ,setEmail]=useState(localStorage.getItem('tts_email'));

    const handleAuthSuccess=(data)=>{
        localStorage.setItem('tts_token',data.token);
        localStorage.setItem('tts_email',data.email);
        setToken(data.token);
        setEmail(data.email);
    };

    const login = async (email, password) => {
        const response = await authApi.login(email, password);
        handleAuthSuccess(response.data);
      };

      const register = async (email, password) => {
        const response = await authApi.register(email, password);
        handleAuthSuccess(response.data);
      };

    const  logout=()=>{
        localStorage.removeItem('tts_token')
        localStorage.removeItem('tts_email')
        setToken(null);
        setEmail(null);
    };

    return(
        <AuthContext.Provider value={{token, email,login,register,logout,isAuthenticated:!!token}}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth(){
    return useContext(AuthContext);
}
