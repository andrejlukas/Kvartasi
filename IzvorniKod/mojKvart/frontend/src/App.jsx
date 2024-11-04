import { useState } from 'react'
import './styles/App.css'
import { Login } from "./pages/Login"
import { Routes, Route } from "react-router-dom";
import { Signup } from './pages/Signup';
import {NotFound} from "./pages/NotFound"

function App() {

  return (
    <>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
      
    </>
  )
}

export default App
