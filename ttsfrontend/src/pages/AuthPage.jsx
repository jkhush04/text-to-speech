import { useState } from 'react';
import { useAuth } from '../context/AuthContext';

function AuthPage() {
  const [mode, setMode] = useState('login'); // 'login' | 'register'
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login, register } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      if (mode === 'login') {
        await login(email, password);
      } else {
        await register(email, password);
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Something went wrong. Try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-paper flex items-center justify-center px-4">
      <div className="w-full max-w-sm">
        <div className="flex items-center gap-2 mb-8">
          <span className="w-2.5 h-2.5 rounded-full bg-signal"></span>
          <h1 className="font-display text-2xl font-semibold text-ink">TTS Studio</h1>
        </div>

        <div className="flex gap-6 mb-6 border-b border-sage/20">
          <button
            onClick={() => setMode('login')}
            className={`pb-3 font-body text-sm ${mode === 'login' ? 'text-ink border-b-2 border-signal font-medium' : 'text-sage'}`}
          >
            Log in
          </button>
          <button
            onClick={() => setMode('register')}
            className={`pb-3 font-body text-sm ${mode === 'register' ? 'text-ink border-b-2 border-signal font-medium' : 'text-sage'}`}
          >
            Create account
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-body text-sage mb-1">Email</label>
            <input
              type="email"
              autoComplete="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="w-full px-3 py-2 bg-white border border-sage/30 rounded font-body text-ink focus:outline-none focus:ring-2 focus:ring-signal"
            />
          </div>
          <div>
            <label className="block text-sm font-body text-sage mb-1">Password</label>
            <input
              type="password"
              autoComplete={mode==='login'?'current-password':'new-password'}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              minLength={8}
              className="w-full px-3 py-2 bg-white border border-sage/30 rounded font-body text-ink focus:outline-none focus:ring-2 focus:ring-signal"
            />
          </div>

          {error && (
            <p className="text-sm text-alert font-body">{error}</p>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full py-2.5 bg-signal text-ink font-body font-medium rounded hover:opacity-90 disabled:opacity-50 transition-opacity"
          >
            {loading ? 'Please wait...' : mode === 'login' ? 'Log in' : 'Create account'}
          </button>
        </form>
      </div>
    </div>
  );
}

export default AuthPage;