import React, { useState } from 'react';
import '../css/MenuDesplegable.css';

/**
 * Componente de menú desplegable para el dashboard.
 * Muestra opciones como Reportes, Cerrar Sesión, Acerca de, Manual de Usuario.
 */
const MenuDesplegable = ({ onReportes, onCerrarSesion, onAcercaDe, onManualUsuario }) => {
  const [isOpen, setIsOpen] = useState(false);

  /**
   * Alterna la visibilidad del menú desplegable
   */
  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };

  /**
   * Cierra el menú cuando se selecciona una opción
   */
  const handleOptionClick = (callback) => {
    setIsOpen(false);
    callback();
  };

  return (
    <div className="menu-desplegable">
      <button className="menu-toggle-btn" onClick={toggleMenu}>
        <span className="menu-icon">☰</span>
        <span className="menu-text">Menú</span>
      </button>
      
      {isOpen && (
        <div className="menu-dropdown">
          <button 
            className="menu-option" 
            onClick={() => handleOptionClick(onReportes)}
          >
            📊 Reportes
          </button>
          <button 
            className="menu-option" 
            onClick={() => handleOptionClick(onAcercaDe)}
          >
            ℹ️ Acerca de
          </button>
          <button 
            className="menu-option" 
            onClick={() => handleOptionClick(onManualUsuario)}
          >
            📖 Manual de Usuario
          </button>
          <div className="menu-divider"></div>
          <button 
            className="menu-option logout-option" 
            onClick={() => handleOptionClick(onCerrarSesion)}
          >
            🚪 Cerrar Sesión
          </button>
        </div>
      )}
    </div>
  );
};

export default MenuDesplegable;
