import { useState } from 'react'
import './styles/App.css'
import { Routes, Route } from "react-router-dom";
import { Login } from "./pages/Login"
import { Signup } from './pages/Signup';
import {NotFound} from "./pages/NotFound"
import { Home } from './pages/Home';
import { PopisTrgovina } from './pages/PopisTrgovina';
import { PonudeiPromocije } from './pages/PonudeiPromocije';
import { Dogadaji } from './pages/Dogadaji';
import Shop from './pages/Shop'

function App() {

  return (
    <>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/home" element={<Home />} />
        <Route path="/home/kvart" element={<Home />} ></Route>
        <Route path="/home/popisTrgovina" element={<PopisTrgovina />} ></Route>
        <Route path="/home/ponude" element={<PonudeiPromocije />} ></Route>
        <Route path="/home/popistrgovina/:id" element={<Shop />}></Route>
        <Route path="/home/dogadaji" element={<Dogadaji />} ></Route>

        <Route path="*" element={<NotFound />} /> 
      </Routes>
      
    </>
  )
}

export default App