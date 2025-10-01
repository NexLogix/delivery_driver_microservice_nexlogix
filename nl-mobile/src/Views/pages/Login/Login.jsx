import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../../css/Login.css';
import ReporteUsuarioBloqueado from '../../modals/ReporteUsuarioBloqueado';
import AuthService from '../../../Services/AuthService';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isModalOpen, setModalOpen] = useState(false);
  const navigate = useNavigate();

  const openModal = () => setModalOpen(true);
  const closeModal = () => setModalOpen(false);

  /**
   * Maneja el envío del formulario de inicio de sesión.
   * Llama al servicio de autenticación y maneja la respuesta.
   */
  const handleLogin = async (e) => {
    e.preventDefault();
    setError(''); // Limpia errores previos

    try {
      await AuthService.login(email, password);
      navigate('/dashboard'); // Redirige al dashboard en caso de éxito
    } catch (error) {
      setError(error.message); // Muestra el mensaje de error
    }
  };

  return (
    <div className="login-container">
      <div className="login-background"></div>
      <div className="login-content">
        <h1 style={{ textAlign: 'center' }}>NexLogix</h1>
        <form onSubmit={handleLogin}>
          <div className="input-group">
            <label htmlFor="username"></label>
            <input
              type="text"
              id="username"
              placeholder="Correo electrónico o ID"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="input-group">
            <label htmlFor="password"></label>
            <input
              type="password"
              id="password"
              placeholder="Contraseña"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          {error && <p className="error-message">{error}</p>}
          <div className="remember-me">
            <input type="checkbox" id="remember" />
            <label htmlFor="remember">Recuérdame</label>
          </div>
          <button type="submit" className="signin-btn">Iniciar sesión</button>
        </form>
        <div className="blocked-user-link">
          <a href="#" onClick={(e) => { e.preventDefault(); openModal(); }}>
            ¿Usuario bloqueado?
          </a>
        </div>
      </div>
      <ReporteUsuarioBloqueado isOpen={isModalOpen} onClose={closeModal} />
    </div>
  );
};

export default Login;