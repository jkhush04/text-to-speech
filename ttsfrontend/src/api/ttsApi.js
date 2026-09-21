import axiosInstance from './axiosInstance';

export const generateSpeech = (text, language, voice) =>
  axiosInstance.post('/api/tts', { text, language, voice });

export const getVoices = () =>
  axiosInstance.get('/api/voices');

export const getHistory = () =>
  axiosInstance.get('/api/history');

export const fetchAudioBlob = (audioPath) =>
    axiosInstance.get(audioPath, { responseType: 'blob' });