import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from '../Views/pages/Login/Login';
import Dashboard from '../Views/pages/Dashboard/Dashboard';

/**
 * Componente principal de enrutamiento.
 * Define las rutas de la aplicación y qué componente renderizar para cada una.
 */
const AppRouter = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        {/* Agrega más rutas aquí en el futuro */}
      </Routes>
    </Router>
  );
};

export default AppRouter;
