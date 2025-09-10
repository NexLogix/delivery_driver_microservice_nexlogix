import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../../css/Dashboard.css';
import MenuDesplegable from '../../components/MenuDesplegable';
import ModalGenerico from '../../modals/ModalGenerico';

const Dashboard = () => {
  const [modalReportes, setModalReportes] = useState(false);
  const [modalAcercaDe, setModalAcercaDe] = useState(false);
  const [modalManualUsuario, setModalManualUsuario] = useState(false);
  const navigate = useNavigate();

  /**
   * Abre el modal de reportes
   */
  const handleReportes = () => {
    setModalReportes(true);
  };

  /**
   * Maneja el cierre de sesión y redirige al login
   */
  const handleCerrarSesion = () => {
    navigate('/');
  };

  /**
   * Abre el modal de acerca de
   */
  const handleAcercaDe = () => {
    setModalAcercaDe(true);
  };

  /**
   * Abre el modal del manual de usuario
   */
  const handleManualUsuario = () => {
    setModalManualUsuario(true);
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <h1>Dashboard Conductor</h1>
        <MenuDesplegable 
          onReportes={handleReportes}
          onCerrarSesion={handleCerrarSesion}
          onAcercaDe={handleAcercaDe}
          onManualUsuario={handleManualUsuario}
        />
      </header>
      <main className="dashboard-main">
        <div className="dashboard-card">
          <h2>Rutas Asignadas</h2>
          <p>Aquí se mostrará la lista de rutas asignadas para hoy.</p>
        </div>
        <div className="dashboard-card">
          <h2>Vehículos Asignados</h2>
          <p>Aquí se mostrará información de los vehículos asignados al conductor.</p>
        </div>
        <div className="dashboard-card">
          <h2>Mapa de Ruta</h2>
          <p>Aquí se mostrará un mapa con la ruta optimizada.</p>
        </div>
        <div className="dashboard-card">
          <h2>Estadísticas</h2>
          <p>Aquí se mostrarán las estadísticas de rendimiento.</p>
        </div>
      </main>

      {/* Modales */}
      <ModalGenerico 
        isOpen={modalReportes} 
        onClose={() => setModalReportes(false)}
        title="Reportes"
      >
        <div>
          <h3>Reportes de Entregas</h3>
          <p>Aquí podrás generar y visualizar reportes detallados de tus entregas:</p>
          <ul>
            <li><strong>Reporte Diario:</strong> Entregas completadas hoy</li>
            <li><strong>Reporte Semanal:</strong> Resumen de la semana</li>
            <li><strong>Reporte Mensual:</strong> Estadísticas del mes</li>
            <li><strong>Reporte de Incidencias:</strong> Problemas reportados</li>
          </ul>
          <p><em>Funcionalidad en desarrollo...</em></p>
        </div>
      </ModalGenerico>

      <ModalGenerico 
        isOpen={modalAcercaDe} 
        onClose={() => setModalAcercaDe(false)}
        title="Acerca de NexLogix"
      >
        <div>
          <h3>NexLogix - Sistema de Gestión de Entregas</h3>
          <p><strong>Versión:</strong> 1.0.0</p>
          <p><strong>Desarrollado por:</strong> Equipo NexLogix</p>
          <p><strong>Fecha de lanzamiento:</strong> Septiembre 2025</p>
          
          <h4>Descripción:</h4>
          <p>NexLogix es una plataforma integral diseñada para optimizar la gestión de entregas y mejorar la eficiencia de los conductores. Nuestro sistema proporciona herramientas avanzadas para el seguimiento de rutas, gestión de paquetes y análisis de rendimiento.</p>
          
          <h4>Contacto:</h4>
          <p>📧 soporte@nexlogix.com</p>
          <p>📞 +1 (555) 123-4567</p>
        </div>
      </ModalGenerico>

      <ModalGenerico 
        isOpen={modalManualUsuario} 
        onClose={() => setModalManualUsuario(false)}
        title="Manual de Usuario"
      >
        <div>
          <h3>Guía de Uso del Dashboard</h3>
          
          <h4>1. Navegación Principal</h4>
          <p>Usa el menú desplegable en la esquina superior derecha para acceder a todas las funciones.</p>
          
          <h4>2. Mis Entregas de Hoy</h4>
          <p>Visualiza todas las entregas asignadas para el día actual. Cada entrega muestra:</p>
          <ul>
            <li>Dirección de destino</li>
            <li>Hora estimada de entrega</li>
            <li>Estado actual del paquete</li>
          </ul>
          
          <h4>3. Mapa de Ruta</h4>
          <p>El sistema calcula automáticamente la ruta más eficiente para completar todas las entregas.</p>
          
          <h4>4. Estadísticas</h4>
          <p>Monitorea tu rendimiento con métricas como:</p>
          <ul>
            <li>Entregas completadas</li>
            <li>Tiempo promedio por entrega</li>
            <li>Calificaciones de los clientes</li>
          </ul>
          
          <h4>5. Reportes</h4>
          <p>Genera reportes detallados de tu actividad para análisis y mejora continua.</p>
          
          <p><strong>¿Necesitas ayuda adicional?</strong> Contacta a soporte técnico desde el menú "Acerca de".</p>
        </div>
      </ModalGenerico>
    </div>
  );
};

export default Dashboard;
