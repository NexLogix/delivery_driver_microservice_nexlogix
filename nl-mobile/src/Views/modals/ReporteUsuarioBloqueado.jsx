import React, { useState } from 'react';
import '../css/ReporteUsuarioBloqueado.css';

/**
 * Componente de modal para que los usuarios bloqueados soliciten el desbloqueo.
 * @param {object} props - Propiedades del componente.
 * @param {boolean} props.isOpen - Controla si el modal está abierto o cerrado.
 * @param {function} props.onClose - Función para cerrar el modal.
 */
const ReporteUsuarioBloqueado = ({ isOpen, onClose }) => {
  // Estado para manejar los campos del formulario
  const [formData, setFormData] = useState({
    email: '',
    id: '',
    name: '',
    phone: ''
  });

  // Si el modal no está abierto, no renderizar nada.
  if (!isOpen) {
    return null;
  }

  // Maneja los cambios en los inputs del formulario
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prevData => ({
      ...prevData,
      [name]: value
    }));
  };

  /**
   * Maneja el envío del formulario.
   * Por ahora, solo muestra los datos en la consola y una alerta.
   */
  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Ticket de desbloqueo solicitado con los siguientes datos:', formData);
    alert('Se ha enviado una solicitud a TI para desbloquear tu cuenta. Nos pondremos en contacto contigo pronto.');
    onClose(); // Cierra el modal después de enviar
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <button className="modal-close-btn" onClick={onClose}>&times;</button>
        <h2>Solicitud de Desbloqueo de Cuenta</h2>
        <p>Por favor, completa el siguiente formulario para solicitar el desbloqueo de tu cuenta.</p>
        <form onSubmit={handleSubmit}>
          <div className="modal-input-group">
            <label htmlFor="email">Correo</label>
            <input type="email" id="email" name="email" value={formData.email} onChange={handleInputChange} required />
          </div>
          <div className="modal-input-group">
            <label htmlFor="id">ID</label>
            <input type="text" id="id" name="id" value={formData.id} onChange={handleInputChange} required />
          </div>
          <div className="modal-input-group">
            <label htmlFor="name">Nombre</label>
            <input type="text" id="name" name="name" value={formData.name} onChange={handleInputChange} required />
          </div>
          <div className="modal-input-group">
            <label htmlFor="phone">Teléfono</label>
            <input type="tel" id="phone" name="phone" value={formData.phone} onChange={handleInputChange} required />
          </div>
          <button type="submit" className="modal-submit-btn">Enviar Solicitud</button>
        </form>
      </div>
    </div>
  );
};

export default ReporteUsuarioBloqueado;
