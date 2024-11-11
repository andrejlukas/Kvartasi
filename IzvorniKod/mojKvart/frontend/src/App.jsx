import { useState } from 'react'
import './styles/App.css'
import { Routes, Route } from "react-router-dom";
import { Login } from "./pages/Login"
import { Signup } from './pages/Signup';
import {NotFound} from "./pages/NotFound"
import { Home } from './pages/Home';

function App() {

  return (
    <>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/home" element={<Home />} />
        <Route path="*" element={<NotFound />} /> 
      </Routes>
      
    </>
  )
}

export default App