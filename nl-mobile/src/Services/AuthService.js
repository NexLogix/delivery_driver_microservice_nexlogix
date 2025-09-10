import Conductor from '../Models/Conductor';

/**
 * Servicio de autenticación simulado.
 * Contiene la lógica para verificar las credenciales del usuario.
 */
const AuthService = {
  /**
   * Simula el proceso de inicio de sesión.
   * @param {string} email - El email ingresado por el usuario.
   * @param {string} contrasena - La contraseña ingresada por el usuario.
   * @returns {Promise<object>} - Una promesa que resuelve con un objeto de éxito o error.
   */
  login: (email, contrasena) => {
    return new Promise((resolve, reject) => {
      // Simula una pequeña demora de red
      setTimeout(() => {
        if (email === Conductor.email && contrasena === Conductor.contrasena) {
          resolve({ success: true, message: 'Inicio de sesión exitoso' });
        } else if (email !== Conductor.email) {
          reject({ success: false, message: 'El correo electrónico no existe.' });
        } else {
          reject({ success: false, message: 'Contraseña incorrecta.' });
        }
      }, 500);
    });
  }
};

export default AuthService;
