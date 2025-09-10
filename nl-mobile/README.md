# NexLogix - Sistema de Gestión de Entregas para Conductores

<div align="center">
  <img src="assets/nl-mobile/src/assets/logo.png" alt="NexLogix Logo" width="300"/>
</div>

<div align="center">
  <h3>Frontend móvil para conductores - Mockup funcional</h3>
  <p>Sistema de autenticación y dashboard simulado para gestión de entregas</p>
</div>

---

## 📋 Descripción del Proyecto

NexLogix Mobile es una aplicación frontend diseñada para conductores de delivery, que proporciona una interfaz intuitiva para la gestión de entregas, rutas y vehículos asignados. Este proyecto implementa un **mockup funcional** con autenticación simulada y un dashboard completo.

## 🚀 Características Implementadas

### ✅ Sistema de Autenticación
- **Login funcional** con validación de credenciales
- **Autenticación simulada** sin necesidad de backend
- **Manejo de errores** y mensajes informativos
- **Redirección automática** tras login exitoso
- **Modal de solicitud de desbloqueo** para usuarios bloqueados

### ✅ Dashboard del Conductor
- **Panel principal** con 4 secciones funcionales:
  - Rutas Asignadas
  - Vehículos Asignados
  - Mapa de Ruta
  - Estadísticas de Rendimiento
- **Menú desplegable** con opciones completas:
  - 📊 Reportes (modal informativo)
  - ℹ️ Acerca de (información del sistema)
  - 📖 Manual de Usuario (guía completa)
  - 🚪 Cerrar Sesión (redirección a login)

### ✅ Diseño y UX
- **Interfaz moderna** con gradientes y animaciones
- **Diseño responsivo** adaptable a diferentes dispositivos
- **Tema oscuro** consistente en toda la aplicación
- **Componentes reutilizables** y bien estructurados

## 🛠️ Tecnologías Utilizadas

- **React 19.1.1** - Biblioteca principal de UI
- **Vite 7.1.2** - Herramienta de build y desarrollo
- **React Router DOM** - Navegación entre páginas
- **React Icons** - Iconografía del sistema
- **CSS3** - Estilos avanzados con gradientes y animaciones
- **ESLint** - Linting y calidad de código

## 📁 Estructura del Proyecto

```
src/
├── Models/                 # Modelos de datos
│   └── Conductor.js       # Clase Conductor con credenciales
├── Services/              # Servicios de la aplicación
│   └── AuthService.js     # Servicio de autenticación simulada
├── Routes/                # Configuración de rutas
│   └── AppRouter.jsx      # Router principal
├── Views/
│   ├── components/        # Componentes reutilizables
│   │   └── MenuDesplegable.jsx
│   ├── css/              # Estilos CSS
│   │   ├── Login.css
│   │   ├── Dashboard.css
│   │   ├── MenuDesplegable.css
│   │   ├── ModalGenerico.css
│   │   └── ReporteUsuarioBloqueado.css
│   ├── modals/           # Componentes modales
│   │   ├── ModalGenerico.jsx
│   │   └── ReporteUsuarioBloqueado.jsx
│   └── pages/            # Páginas principales
│       ├── Login/
│       │   └── Login.jsx
│       └── Dashboard/
│           └── Dashboard.jsx
└── assets/               # Recursos estáticos
    ├── logo.png
    └── logo.ico
```

## 🔐 Credenciales de Prueba

Para probar el sistema de autenticación, utiliza las siguientes credenciales:

- **Email:** `conductor@nexlogix.com`
- **Contraseña:** `password123`

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Node.js 16+ 
- npm o yarn

### Pasos de instalación

1. **Clonar el repositorio**
```bash
git clone [URL_DEL_REPOSITORIO]
cd nl-mobile
```

2. **Instalar dependencias**
```bash
npm install
```

3. **Ejecutar en modo desarrollo**
```bash
npm run dev
```

4. **Abrir en el navegador**
```
http://localhost:5173
```

### Scripts disponibles

- `npm run dev` - Modo desarrollo
- `npm run build` - Build de producción
- `npm run preview` - Vista previa del build
- `npm run lint` - Análisis de código

## 🔧 Integración con Backend Real

Para conectar esta aplicación con un backend real, se requieren las siguientes modificaciones:

### 📍 1. Servicio de Autenticación (`src/Services/AuthService.js`)

**Modificaciones requeridas:**
```javascript
// Reemplazar la autenticación simulada por llamadas reales a la API
const AuthService = {
  login: async (email, password) => {
    try {
      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
      });
      
      const data = await response.json();
      
      if (response.ok) {
        // Guardar token JWT
        localStorage.setItem('authToken', data.token);
        return { success: true, user: data.user };
      } else {
        throw new Error(data.message);
      }
    } catch (error) {
      throw new Error('Error de conexión con el servidor');
    }
  }
};
```

**Endpoints de backend necesarios:**
- `POST /api/auth/login` - Autenticación de conductor
- `POST /api/auth/logout` - Cerrar sesión
- `POST /api/auth/reset-password` - Solicitud de desbloqueo

### 📍 2. Modelo de Conductor (`src/Models/Conductor.js`)

**Modificaciones requeridas:**
```javascript
// Reemplazar la clase estática por un servicio de API
const ConductorService = {
  getCurrentUser: async () => {
    const token = localStorage.getItem('authToken');
    const response = await fetch('/api/conductor/profile', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    return response.json();
  }
};
```

**Endpoints de backend necesarios:**
- `GET /api/conductor/profile` - Perfil del conductor
- `PUT /api/conductor/profile` - Actualizar perfil

### 📍 3. Dashboard (`src/Views/pages/Dashboard/Dashboard.jsx`)

**Modificaciones requeridas:**
```javascript
// Agregar llamadas a APIs para datos reales
useEffect(() => {
  // Cargar rutas asignadas
  fetch('/api/conductor/routes')
    .then(res => res.json())
    .then(setRutas);
  
  // Cargar vehículos asignados
  fetch('/api/conductor/vehicles')
    .then(res => res.json())
    .then(setVehiculos);
    
  // Cargar estadísticas
  fetch('/api/conductor/stats')
    .then(res => res.json())
    .then(setEstadisticas);
}, []);
```

**Endpoints de backend necesarios:**
- `GET /api/conductor/routes` - Rutas asignadas
- `GET /api/conductor/vehicles` - Vehículos asignados
- `GET /api/conductor/stats` - Estadísticas del conductor
- `GET /api/conductor/reports` - Reportes disponibles

### 📍 4. Manejo de Tokens JWT

**Implementar interceptor para autenticación:**
```javascript
// src/utils/apiClient.js
const apiClient = {
  get: (url) => fetch(url, {
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('authToken')}`
    }
  }),
  // ... otros métodos HTTP
};
```

### 📍 5. Variables de Entorno

**Crear archivo `.env`:**
```env
VITE_API_BASE_URL=http://localhost:3000/api
VITE_WEBSOCKET_URL=ws://localhost:3000
```

## 🗄️ Base de Datos Requerida

### Tablas principales:
- `conductores` - Información de conductores
- `rutas` - Rutas de entrega
- `vehiculos` - Información de vehículos
- `entregas` - Registro de entregas
- `reportes` - Reportes generados

## 🔄 Próximas Funcionalidades

- [ ] Integración con APIs REST reales
- [ ] Notificaciones push en tiempo real
- [ ] Geolocalización y mapas interactivos
- [ ] Carga de imágenes de entrega
- [ ] Chat con soporte técnico
- [ ] Modo offline con sincronización

## 🤝 Contribución

1. Fork del proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit de los cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👥 Equipo de Desarrollo

- **Frontend Developer** - Implementación de UI/UX
- **Backend Developer** - APIs y servicios (pendiente)
- **DevOps** - Despliegue y CI/CD (pendiente)

---

<div align="center">
  <p>Desarrollado con ❤️ para NexLogix</p>
  <p>© 2025 NexLogix. Todos los derechos reservados.</p>
</div>
