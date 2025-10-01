/**
 * Clase que representa a un conductor con credenciales de prueba.
 * En una aplicación real, estos datos vendrían de una base de datos.
 */
class Conductor {
    constructor() {
      this.email = 'conductor@nexlogix.com';
      this.contrasena = 'password123';
    }
  }
  
  // Exportamos una única instancia de la clase Conductor
  export default new Conductor();
  