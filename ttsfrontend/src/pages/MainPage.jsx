import { useAuth } from '../context/AuthContext';
import TtsGenerator from '../components/TtsGenerator';
import HistoryPanel from '../components/HistoryPanel';

function MainPage() {
  const { email, logout } = useAuth();

  return (
    <div className="min-h-screen bg-paper">
      <header className="border-b border-sage/20 px-6 py-4 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <span className="w-2.5 h-2.5 rounded-full bg-signal"></span>
          <h1 className="font-display text-xl font-semibold text-ink">TTS Studio</h1>
        </div>
        <div className="flex items-center gap-4">
          <span className="font-body text-sm text-sage">{email}</span>
          <button
            onClick={logout}
            className="font-body text-sm text-ink hover:text-alert transition-colors"
          >
            Log out
          </button>
        </div>
      </header>

      <main className="max-w-6xl mx-auto px-6 py-8 grid grid-cols-1 lg:grid-cols-3 gap-8">
        <section className="lg:col-span-2">
        <TtsGenerator/>
        </section>

        <aside>
          <h2 className="font-display text-lg font-medium text-ink mb-4">History</h2>

            <HistoryPanel/>

        </aside>
      </main>
    </div>
  );
}

export default MainPage;