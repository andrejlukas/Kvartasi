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
import { MojePonudeiPromocije } from './pages/user/MojePonudeiPromocije';
import { Kosarica } from './pages/user/Kosarica';
import { Recenzije } from './pages/user/Recenzije';
import { Proizvod } from './pages/user/Proizvod';

import { ShopHome } from './pages/shop/Home';
import { ShopNarudzbe } from './pages/shop/Narudzbe';
import { ShopPonudeiPromocije } from './pages/shop/PonudeiPromocije';
import { ShopDogadaji } from './pages/shop/Dogadaji';
import { ShopKorisnickiRacun } from './pages/shop/KorisnickiRacun';
import { ShopMojiPodaci } from './pages/shop/MojiPodaci';
import { ShopMojeRecenzije } from './pages/shop/MojeRecenzije';
import { ShopMojiAtributi } from './pages/shop/MojiAtributi';

import { ModeratorHome } from './pages/moderator/Home';
import { ModeratorPonude } from './pages/moderator/PonudeiPromocije';
import { ModeratorRecenzije } from './pages/moderator/Recenzije';
import { ModeratorUpravljanjeKupcima } from './pages/moderator/UpravljanjeKupcima';
import { ModeratorUpravljanjeTrgovinama } from './pages/moderator/UpravljanjeTrgovinama';
import { ModeratorKorisnickiRacun } from './pages/moderator/KorisnickiRacun';
import { ModeratorMojiPodaci } from './pages/moderator/MojiPodaci';

import { AdminHome } from './pages/admin/Home';
import { AdminPonude } from './pages/admin/PonudeiPromocije';
import { AdminRecenzije } from './pages/admin/Recenzije';
import { AdminUpravljanjeUlogama } from './pages/admin/UpravljanjeUlogama';
import { SusKorisnike } from './pages/admin/SusKorisnike';
import { AdminKorisnickiRacun } from './pages/admin/KorisnickiRacun';
import { AdminMojiPodaci } from './pages/admin/MojiPodaci';

function App() {
  const [isAuthorized, setIsAuthorized] = useState(null);
  const [role, setRole] = useState(null);

  const checkTokenExpirationAndRole = async () => {
    const token = localStorage.getItem("token");
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

    if (token === null) {
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
      if (!expirationResponse.ok) {
        throw new Error('Token expired');
      }
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
    if(!isAuthorized || role !== "ADMINISTRATOR") return <NotFound />;
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
        <Route path="/home/popistrgovina" element={
          <SecuredUserRoute>
            <PopisTrgovina />
          </SecuredUserRoute>
        } />
        <Route path="/home/popistrgovina/:email" element={
          <SecuredUserRoute>
            <Shop />
          </SecuredUserRoute>
        } />
        <Route path="/home/ponude" element={
          <SecuredUserRoute>
            <PonudeiPromocije />
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
        <Route path='/mojeponudeipromocije' element={
          <SecuredUserRoute>
            <MojePonudeiPromocije />
          </SecuredUserRoute>
        } />
        <Route path='/kosarica' element={
          <SecuredUserRoute>
            <Kosarica />
          </SecuredUserRoute>
        } />
        <Route path='/home/proizvod/:proizvodId' element={
          <SecuredUserRoute>
            <Proizvod />
          </SecuredUserRoute>
        } />
        <Route path="/home/popistrgovina/:email/recenzije" element={
          <SecuredUserRoute>
            <Recenzije />
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
        <Route path="/atributitrgovine" element={
          <SecuredShopRoute>
            <ShopMojiAtributi />
          </SecuredShopRoute>
        } />


        <Route path="/moderator/home" element={
          <SecuredModeratorRoute>
            <ModeratorHome />
          </SecuredModeratorRoute>
        } />
        <Route path="/moderator/home/proizvodi" element={
          <SecuredModeratorRoute>
            <ModeratorHome />
          </SecuredModeratorRoute>
        } />
          <Route path="/moderator/home/recenzije" element={
          <SecuredModeratorRoute>
            <ModeratorRecenzije />
          </SecuredModeratorRoute>
        } />
          <Route path="/moderator/home/ponude" element={
          <SecuredModeratorRoute>
            <ModeratorPonude />
          </SecuredModeratorRoute>
        } />
          <Route path="/moderator/home/kupci" element={
          <SecuredModeratorRoute>
            <ModeratorUpravljanjeKupcima />
          </SecuredModeratorRoute>
        } />
        <Route path="/moderator/home/trgovine" element={
          <SecuredModeratorRoute>
            <ModeratorUpravljanjeTrgovinama />
          </SecuredModeratorRoute>
        } />
          <Route path="/racunmoderator" element={
          <SecuredModeratorRoute>
            <ModeratorKorisnickiRacun />
          </SecuredModeratorRoute>
        } />
          <Route path="/podacimoderator" element={
          <SecuredModeratorRoute>
            <ModeratorMojiPodaci />
          </SecuredModeratorRoute>
        } />


        <Route path="/admin/home" element={
          <SecuredAdminRoute>
            <AdminHome />
          </SecuredAdminRoute>
        } />
        <Route path="/admin/home/proizvodi" element={
          <SecuredAdminRoute>
            <AdminHome />
          </SecuredAdminRoute>
        } />
        <Route path="/admin/home/recenzije" element={
          <SecuredAdminRoute>
            <AdminRecenzije />
          </SecuredAdminRoute>
        } />
        <Route path="/admin/home/ponude" element={
          <SecuredAdminRoute>
            <AdminPonude />
          </SecuredAdminRoute>
        } />
        <Route path="/admin/home/uloge" element={
          <SecuredAdminRoute>
            <AdminUpravljanjeUlogama />
          </SecuredAdminRoute>
        } />
        <Route path="/admin/home/suskorisnike" element={
          <SecuredAdminRoute>
            <SusKorisnike />
          </SecuredAdminRoute>
        } />
        <Route path="/racunadmin" element={
          <SecuredAdminRoute>
            <AdminKorisnickiRacun />
          </SecuredAdminRoute>
        } />
        <Route path="/podaciadmin" element={
          <SecuredAdminRoute>
            <AdminMojiPodaci />
          </SecuredAdminRoute>
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