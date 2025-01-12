import { useState, useEffect } from 'react';
import { Routes, Route } from 'react-router-dom';

import { Login } from './pages/Login';
import { Signup } from './pages/Signup';
import { NotVerified } from './pages/NotVerified'
import { Suspended  } from './pages/Suspended';
import { NotFound } from './pages/NotFound';

import { Home } from './pages/user/Home';
import { PopisTrgovina } from './pages/user/PopisTrgovina';
import { PonudeiPromocije } from './pages/user/PonudeiPromocije';
import { Dogadaji } from './pages/user/Dogadaji';
import { KorisnickiRacun } from './pages/user/KorisnickiRacun';
import { Shop } from './pages/user/Shop';
import { MojiPodaci } from './pages/user/MojiPodaci';
import { MojiRacuni } from './pages/user/MojiRacuni';
import { MojeRecenzije } from './pages/user/MojeRecenzije';

import { ShopHome } from './pages/shop/Home';
import { ShopNarudzbe } from './pages/shop/Narudzbe';
import { ShopPonudeiPromocije } from './pages/shop/PonudeiPromocije';
import { ShopDogadaji } from './pages/shop/Dogadaji';
import { ShopKorisnickiRacun } from './pages/shop/KorisnickiRacun';
import { ShopMojiPodaci } from './pages/shop/MojiPodaci';
import { ShopMojeRecenzije } from './pages/shop/MojeRecenzije';

function App() {
  const [isAuthorized, setIsAuthorized] = useState(null);
  const [role, setRole] = useState(null);

  const checkTokenExpirationAndRole = async () => {
    const url = window.location.href;
    if (url.includes("?token=")) {
      localStorage.setItem('token', url.split("?token=")[1]);
      setIsAuthorized(true);

      const options = {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ "oneLiner": url.split("?token=")[1] }),
      };
      try {
        const claimsResponse = await fetch('/api/tokens/claims', options);
        if (!claimsResponse.ok) throw new Error('Failed to fetch claims');
        const claims = await claimsResponse.json();
        setRole(claims.role);
      } catch(e) {
        setIsAuthorized(false);
      }
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
      body: JSON.stringify({ "oneLiner": token }),
    };

    try {
      const expirationResponse = await fetch('/api/tokens/expiration', options);
      if (!expirationResponse.ok) throw new Error('Token expired');

      const claimsResponse = await fetch('/api/tokens/claims', options);
      if (!claimsResponse.ok) throw new Error('Failed to fetch claims');
      const claims = await claimsResponse.json();
      setRole(claims.role);
      setIsAuthorized(true);
    } catch (error) {
      setIsAuthorized(false);
    }
  };

  useEffect(() => {
    checkTokenExpirationAndRole();
  }, []);

  const SecuredUserRoute = ({ children }) => {
    if(isAuthorized === null && role === null) return <div>Loading...</div>;
    if(!isAuthorized || role !== "KUPAC") return <NotFound />;
    return children;
  };

  const SecuredShopRoute = ({ children }) => {
    if(isAuthorized === null && role === null) return <div>Loading...</div>;
    if(!isAuthorized || role !== "TRGOVINA") return <NotFound />;
    return children;
  };

  const SecuredModeratorRoute = ({ children }) => {
    if(isAuthorized === null && role === null) return <div>Loading...</div>;
    if(!isAuthorized || role !== "MODERATOR") return <NotFound />;
    return children;
  };

  const SecuredAdminRoute = ({ children }) => {
    if(isAuthorized === null && role === null) return <div>Loading...</div>;
    if(!isAuthorized || role !== "ADMIN") return <NotFound />;
    return children;
  };

  return (
    <>
      <Routes>
        <Route path="/home" element={
          <SecuredUserRoute>
            <Home />
          </SecuredUserRoute>
        } />
        <Route path="/home/kvart" element={
          <SecuredUserRoute>
            <Home />
          </SecuredUserRoute>
        } />
        <Route path="/home/popisTrgovina" element={
          <SecuredUserRoute>
            <PopisTrgovina />
          </SecuredUserRoute>
        } />
        <Route path="/home/ponude" element={
          <SecuredUserRoute>
            <PonudeiPromocije />
          </SecuredUserRoute>
        } />
        <Route path="/home/popisTrgovina/:email" element={
          <SecuredUserRoute>
            <Shop />
          </SecuredUserRoute>
        } />
        <Route path="/home/dogadaji" element={
          <SecuredUserRoute>
            <Dogadaji />
          </SecuredUserRoute>
        } />
        <Route path="/korisnickiracun" element={
          <SecuredUserRoute>
            <KorisnickiRacun />
          </SecuredUserRoute>
        } />
        <Route path='/mojipodaci' element={
          <SecuredUserRoute>
            <MojiPodaci />
          </SecuredUserRoute>
        } />
        <Route path='/mojiracuni' element={
          <SecuredUserRoute>
            <MojiRacuni />
          </SecuredUserRoute>
        } />
        <Route path='/mojerecenzije' element={
          <SecuredUserRoute>
            <MojeRecenzije />
          </SecuredUserRoute>
        } />


        <Route path="/trgovina/home" element={
          <SecuredShopRoute>
            <ShopHome />
          </SecuredShopRoute>
        } />
        <Route path="/trgovina/home/proizvodi" element={
          <SecuredShopRoute>
            <ShopHome />
          </SecuredShopRoute>
        } />
        <Route path="/trgovina/home/narudzbe" element={
          <SecuredShopRoute>
            <ShopNarudzbe />
          </SecuredShopRoute>
        } />
        <Route path="/trgovina/home/ponude" element={
          <SecuredShopRoute>
            <ShopPonudeiPromocije />
          </SecuredShopRoute>
        } />
        <Route path="/trgovina/home/dogadaji" element={
          <SecuredShopRoute>
            <ShopDogadaji />
          </SecuredShopRoute>
        } />
        <Route path="/racuntrgovine" element={
          <SecuredShopRoute>
            <ShopKorisnickiRacun />
          </SecuredShopRoute>
        } />
        <Route path="/podacitrgovine" element={
          <SecuredShopRoute>
            <ShopMojiPodaci />
          </SecuredShopRoute>
        } />
        <Route path="/recenzijetrgovine" element={
          <SecuredShopRoute>
            <ShopMojeRecenzije />
          </SecuredShopRoute>
        } />


        <Route path="/" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/notverified" element={<NotVerified />} />
        <Route path="/suspended" element={<Suspended />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </>
  );
}

export default App;