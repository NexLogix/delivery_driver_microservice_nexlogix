import React from 'react';
import '../css/ModalGenerico.css';

/**
 * Componente de modal genérico reutilizable.
 * @param {object} props - Propiedades del componente.
 * @param {boolean} props.isOpen - Controla si el modal está abierto.
 * @param {function} props.onClose - Función para cerrar el modal.
 * @param {string} props.title - Título del modal.
 * @param {ReactNode} props.children - Contenido del modal.
 */
const ModalGenerico = ({ isOpen, onClose, title, children }) => {
  if (!isOpen) {
    return null;
  }

  return (
    <div className="modal-overlay">
      <div className="modal-content-generico">
        <button className="modal-close-btn" onClick={onClose}>&times;</button>
        <h2>{title}</h2>
        <div className="modal-body">
          {children}
        </div>
      </div>
    </div>
  );
};

export default ModalGenerico;
