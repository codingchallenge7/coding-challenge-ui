import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css'; // <-- Import the CSS file

function App() {
  const [ccyPair, setCcyPair] = useState('EURUSD');
  const [rate, setRate] = useState('');
  const [eventTime, setEventTime] = useState('');
  const [loading, setLoading] = useState(false);
  const [lastTenRates, setLastTenRates] = useState([]);

  // Get latest rate
  const fetchLatestRate = async () => {
    setLoading(true);
    setRate('');
    setEventTime('');
    try {
      const res = await axios.get(`/api/fxrate/latest?ccyPair=${ccyPair}`);
      setRate(res.data.rate);
      setEventTime(res.data.eventTime);
    } catch (e) {
      setRate('Error');
      setEventTime('');
    }
    setLoading(false);
  };

  // Get last ten rates (across all pairs)
  const fetchLastTenRates = async () => {
    setLoading(true);
    setLastTenRates([]);
    try {
      const res = await axios.get(`/api/fxrate`);
      setLastTenRates(res.data);
    } catch (e) {
      setLastTenRates([]);
    }
    setLoading(false);
  };

  // FX Exchange status
  const getExchangeStatus = async () => {
    setLoading(true);
    try {
      const res = await axios.get('/api/fxrate/exchange/status');
      console.log(res.data ? 'Running' : 'Stopped');
    } catch (e) {
      console.log('Unknown');
    }
    setLoading(false);
  };

  // Fetch latest rate on page load and when ccyPair changes
  useEffect(() => {
    fetchLatestRate();
    // eslint-disable-next-line
  }, [ccyPair]);

  return (
    <div className="fxrate-container">
      <h2>FX Rate Viewer</h2>
      <div>
        <label>
          Currency Pair:
          <input
            value={ccyPair}
            onChange={e => setCcyPair(e.target.value.toUpperCase())}
            maxLength={6}
            style={{ marginLeft: 8, marginRight: 16 }}
          />
        </label>
      </div>
      {loading && (
        <div className="loading-message">
          Loading...
        </div>
        /*TODO Show a loading spinner or message*/
      )}
      {rate && !loading && (
        <div className="result" style={{ marginTop: 24 }}>
          <strong>Latest Rate:</strong> {rate} {/*TODO Show the rate in a more readable format with decimal precisions*/}
          <br />
          <strong>Event Time:</strong> {eventTime}
        </div>
      )}
      {/* Place the Get Historic rates button below the latest rate section */}
      <div style={{ marginTop: 24 }}>
        <button onClick={fetchLastTenRates} disabled={loading}>
          'Get Historic rates'
        </button>
      </div>
      {/* Historic rates section */}
      {lastTenRates.length > 0 && !loading && (
          <div></div> /*TODO create a table to display the last ten rates*/
      )}
    </div>
      /*TODO display exchange status*/
  );
}

export default App;
