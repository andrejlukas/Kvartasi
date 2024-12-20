import { useState, useEffect } from 'react';
import { Routes, Route } from "react-router-dom";
import { Login } from "./pages/Login";
import { Signup } from './pages/Signup';
import { NotFound } from "./pages/NotFound";
import { Home } from './pages/Home';
import { PopisTrgovina } from './pages/PopisTrgovina';
import { PonudeiPromocije } from './pages/PonudeiPromocije';
import { Dogadaji } from './pages/Dogadaji';
import { KorisnickiRacun } from './pages/KorisnickiRacun';
import Shop from './pages/Shop';
import { MojiPodaci } from './pages/MojiPodaci';
import { MojiRacuni } from './pages/MojiRacuni';
import { MojeRecenzije } from './pages/MojeRecenzije';

function App() {
  const [isAuthorized, setIsAuthorized] = useState(null);

  const checkTokenExpiration = async () => {
    const url = window.location.href;
    if (url.includes("?token=")) {
      setIsAuthorized(true);
      return;
    }

    const token = localStorage.getItem("token");
    if (!token) {
      setIsAuthorized(false);
      return;
    }

    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ token }),
    };

    try {
      const response = await fetch('/api/tokens/expiration', options);
      if (!response.ok) throw new Error('Token is expired!');
      await response.json();
      setIsAuthorized(true);
    } catch (error) {
      setIsAuthorized(false);
    }
  };

  useEffect(() => {
    checkTokenExpiration();
  }, []);

  const SecuredRoute = ({ children }) => {
    if (isAuthorized === null) return <div>Loading...</div>;
    if (!isAuthorized) return <NotFound />;

    return children;
  };

  return (
    <>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/signup" element={<Signup />} />

        <Route path="/home" element={
          <SecuredRoute>
            <Home />
          </SecuredRoute>
        } />
        <Route path="/home/kvart" element={
          <SecuredRoute>
            <Home />
          </SecuredRoute>
        } />
        <Route path="/home/popisTrgovina" element={
          <SecuredRoute>
            <PopisTrgovina />
          </SecuredRoute>
        } />
        <Route path="/home/ponude" element={
          <SecuredRoute>
            <PonudeiPromocije />
          </SecuredRoute>
        } />
        <Route path="/home/popistrgovina/:id" element={
          <SecuredRoute>
            <Shop />
          </SecuredRoute>
        } />
        <Route path="/home/dogadaji" element={
          <SecuredRoute>
            <Dogadaji />
          </SecuredRoute>
        } />
        <Route path="/korisnickiRacun" element={
          <SecuredRoute>
            <KorisnickiRacun />
          </SecuredRoute>
        } />
        <Route path='/mojipodaci' element={
          <SecuredRoute>
            <MojiPodaci />
          </SecuredRoute>
        } />
        <Route path='/mojiracuni' element={
          <SecuredRoute>
            <MojiRacuni />
          </SecuredRoute>
        } />
        <Route path='/mojerecenzije' element={
          <SecuredRoute>
            <MojeRecenzije />
          </SecuredRoute>
        } />

        <Route path="*" element={<NotFound />} />
      </Routes>
    </>
  );
}

export default App;