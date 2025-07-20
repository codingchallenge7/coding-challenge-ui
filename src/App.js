import React, { useState } from 'react';
import axios from 'axios';

function App() {
  const [baseCurrency, setBaseCurrency] = useState('EUR');
  const [quoteCurrency, setQuoteCurrency] = useState('USD');
  const [rate, setRate] = useState(null);
  const [eventTime, setEventTime] = useState(null);
  const [loading, setLoading] = useState(false);

  const fetchLatestRate = async () => {
    setLoading(true);
    setRate(null);
    setEventTime(null);
    try {
      const res = await axios.get(
        `/api/fxrate/latest?baseCurrency=${baseCurrency}&quoteCurrency=${quoteCurrency}`
      );
      setRate(res.data.rate);
      setEventTime(res.data.eventTime);
    } catch (e) {
      setRate('Error');
      setEventTime(null);
    }
    setLoading(false);
  };

  return (
    <div style={{ padding: 20 }}>
      <h2>FX Rate Viewer</h2>
      <div>
        <label>
          Base Currency:
          <input
            value={baseCurrency}
            onChange={e => setBaseCurrency(e.target.value)}
            maxLength={3}
            style={{ marginLeft: 8, marginRight: 16 }}
          />
        </label>
        <label>
          Quote Currency:
          <input
            value={quoteCurrency}
            onChange={e => setQuoteCurrency(e.target.value)}
            maxLength={3}
            style={{ marginLeft: 8, marginRight: 16 }}
          />
        </label>
        <button onClick={fetchLatestRate} disabled={loading}>
          {loading ? 'Loading...' : 'Get Latest Rate'}
        </button>
      </div>
      {rate && (
        <div style={{ marginTop: 24 }}>
          <strong>Rate:</strong> {rate}
          <br />
          <strong>Event Time:</strong> {eventTime}
        </div>
      )}
    </div>
  );
}

export default App;

