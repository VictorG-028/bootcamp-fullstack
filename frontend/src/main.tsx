import ReactDOM from 'react-dom/client';
import React from 'react';
import { AuthProvider } from './context/AuthContext';
import { Toaster } from 'react-hot-toast';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import Signup from './pages/Signup';
import Landing from './pages/Landing';
import MyDiscs from './pages/MyDiscs';
import MyWallet from './pages/MyWallet';
import { ErrorPage } from './pages/Navigation/Error';

import { PrivateRoutes } from './utils/PrivateRoutes';
import './global.css';


ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.Fragment>
    <Toaster position='top-right' toastOptions={{ duration: 2000 }} />
    <AuthProvider>
      {/* REACT ROUTER */}
      <BrowserRouter>
        <Routes>
          <Route path='*' element={<ErrorPage />} />
          <Route path='/' index element={<Landing />} />
          <Route path='/login' index element={<Login />} />
          <Route path='/signup' index element={<Signup />} />
          <Route path='/home' element={<PrivateRoutes />}>
            <Route path='/home' index element={<Home />} />
          </Route>
          <Route path='/myDiscs' element={<PrivateRoutes />}>
            <Route path='/myDiscs' index element={<MyDiscs />} />
          </Route>
          <Route path='/myWallet' element={<PrivateRoutes />}>
            <Route path='/myWallet' index element={<MyWallet />} />
          </Route>
        </Routes>
      </BrowserRouter>
      {/* REACT ROUTER */}
    </AuthProvider>
  </React.Fragment>,
)
