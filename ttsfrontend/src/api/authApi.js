import axiosInstance from './axiosInstance';

export const register = (email, password) =>
  axiosInstance.post('/api/auth/register', { email, password });

export const login = (email, password) =>
  axiosInstance.post('/api/auth/login', { email, password });