import {useState , useEffect} from 'react';
import * as ttsApi from '../api/ttsApi';

const MAX_CHARS=2000;

function TtsGenerator(){
    const [text,setText]=useState('');
    const [languages,setLanguages]=useState({});
    const [voices, setVoices]=useState([]);
    const [language, setLanguage]=useState('en');
    const [voice, setVoice]=useState('');
    const [loading, setLoading]=useState(false);
    const [error , setError]=useState('');
    const [audioUrl, setAudioUrl]=useState(null);


    useEffect(()=>{
        ttsApi.getVoices().then((res)=>{
            setLanguages(res.data.languages);
            setVoices(res.data.voices);
            if(res.data.voices.length>0){
                setVoice(res.data.voices[0].voiceId);
                }
            });
        },[]
    );


const handleGenerate=async()=>{
    setError('');
    setAudioUrl(null);
    setLoading(true);

    try{
        const response=await ttsApi.generateSpeech(text,language,voice);
        const path=response.data.audioUrl;


        const audioResponse=await ttsApi.fetchAudioBlob(path);
        const blobUrl=URL.createObjectURL(audioResponse.data);
        setAudioUrl(blobUrl);

        }
    catch(err){
        setError(err.response?.data?.message || 'Something went wrong. Try again.');
        }

    finally{
        setLoading(false);
    }

};


  const charCount = text.length;
  const overLimit = charCount > MAX_CHARS;

  return (
    <div>
          <h2 className="font-display text-lg font-medium text-ink mb-4">Your text</h2>

      <textarea
        value={text}
        onChange={(e) => setText(e.target.value)}
        rows={6}
        placeholder="Type or paste the text you want to hear..."
        className="w-full px-4 py-3 bg-white border border-sage/30 rounded font-body text-ink resize-none focus:outline-none focus:ring-2 focus:ring-signal"
      />
      <div className={`text-right text-sm font-body mt-1 ${overLimit ? 'text-alert' : 'text-sage'}`}>
        {charCount} / {MAX_CHARS}
      </div>

      <div className="grid grid-cols-2 gap-4 mt-4">
        <div>
          <label className="block text-sm font-body text-sage mb-1">Language</label>
          <select
            value={language}
            onChange={(e) => setLanguage(e.target.value)}
            className="w-full px-3 py-2 bg-white border border-sage/30 rounded font-body text-ink focus:outline-none focus:ring-2 focus:ring-signal"
          >
            {Object.entries(languages).map(([code, name]) => (
              <option key={code} value={code}>{name}</option>
            ))}
          </select>
        </div>

        <div>
          <label className="block text-sm font-body text-sage mb-1">Voice</label>
          <select
            value={voice}
            onChange={(e) => setVoice(e.target.value)}
            className="w-full px-3 py-2 bg-white border border-sage/30 rounded font-body text-ink focus:outline-none focus:ring-2 focus:ring-signal"
          >
            {voices.map((v) => (
              <option key={v.voiceId} value={v.voiceId}>
                {v.name} ({v.gender})
              </option>
            ))}
          </select>
        </div>
      </div>

      {error && <p className="text-sm text-alert font-body mt-3">{error}</p>}

      <button
        onClick={handleGenerate}
        disabled={loading || !text.trim() || overLimit}
        className="mt-5 px-5 py-2.5 bg-signal text-ink font-body font-medium rounded hover:opacity-90 disabled:opacity-50 transition-opacity"
      >
        {loading ? 'Generating...' : 'Generate speech'}
      </button>

      {loading && (
        <div className="mt-6 flex items-end gap-1 h-8">
          {[0, 1, 2, 3, 4, 5, 6].map((i) => (
            <span
              key={i}
              className="w-1.5 bg-signal rounded-full animate-pulse"
              style={{
                height: `${20 + (i % 3) * 8}px`,
                animationDelay: `${i * 0.1}s`,
              }}
            />
          ))}
        </div>
      )}

      {audioUrl && !loading && (
        <div className="mt-6">
          <audio controls src={audioUrl} className="w-full" />
            <a
            href={audioUrl}
            download="speech.mp3"
            className="inline-block mt-2 text-sm font-body text-ink hover:text-signal transition-colors"
            >
            Download audio
          </a>
        </div>
      )}
    </div>
  );
}

export default TtsGenerator;