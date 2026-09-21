import { useState, useEffect } from 'react';
import * as ttsApi from '../api/ttsApi';

function HistoryPanel() {
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [playingId, setPlayingId] = useState(null);
  const [playingUrl, setPlayingUrl] = useState(null);

  useEffect(() => {
    loadHistory();
  }, []);

  const loadHistory = async () => {
    setLoading(true);
    try {
      const response = await ttsApi.getHistory();
      setHistory(response.data);
    } catch (err) {
      setError('Could not load history.');
    } finally {
      setLoading(false);
    }
  };

  const handlePlay = async (item) => {
    if (!item.audioUrl) return;
    setPlayingId(item.id);
    try {
      const audioResponse = await ttsApi.fetchAudioBlob(item.audioUrl);
      const blobUrl = URL.createObjectURL(audioResponse.data);
      setPlayingUrl(blobUrl);
    } catch (err) {
      setError('Could not load that recording.');
      setPlayingId(null);
    }
  };

  const formatTime = (isoString) => {
    const date = new Date(isoString);
    return date.toLocaleString(undefined, {
      month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit'
    });
  };

  const truncate = (text, max = 50) =>
    text.length > max ? text.slice(0, max) + '...' : text;

  if (loading) {
    return <p className="text-sage font-body text-sm">Loading history...</p>;
  }

  if (error) {
    return <p className="text-alert font-body text-sm">{error}</p>;
  }

  if (history.length === 0) {
    return <p className="text-sage font-body text-sm">No generations yet — your history will appear here.</p>;
  }

  return (
    <div className="space-y-1">
      {history.map((item) => (
        <div key={item.id}>
          <button
            onClick={() => handlePlay(item)}
            disabled={item.status !== 'SUCCESS'}
            className="w-full text-left py-2.5 border-b border-sage/10 hover:bg-white transition-colors disabled:opacity-40 disabled:cursor-not-allowed"
          >
            <div className="flex items-center justify-between gap-2">
              <span className="font-body text-sm text-ink truncate">
                {truncate(item.inputText)}
              </span>
              {item.status !== 'SUCCESS' && (
                <span className="text-xs text-alert font-body shrink-0">failed</span>
              )}
            </div>
            <span className="text-xs text-sage font-body">
              {formatTime(item.createdAt)} · {item.language}
            </span>
          </button>

          {playingId === item.id && playingUrl && (
            <audio
              controls
              autoPlay
              src={playingUrl}
              className="w-full mt-2 mb-2"
              onEnded={() => setPlayingId(null)}
            />
          )}
        </div>
      ))}
    </div>
  );
}

export default HistoryPanel;