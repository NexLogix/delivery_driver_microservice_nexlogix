    package com.example.springboot_sena_kotlin1.Repository
    import com.example.springboot_sena_kotlin1.models.UserUpdateDTO

    import com.example.springboot_sena_kotlin1.models.Users
    import com.example.springboot_sena_kotlin1.models.UsersName
    import org.springframework.jdbc.core.JdbcTemplate
    import org.springframework.stereotype.Repository

    @Repository
    class UsuarioRepository(private val jdbcTemplate: JdbcTemplate) {

        fun crear(usuario: Users, hashedPassword: String): Int {
            val sql = """
                INSERT INTO usuarios (
                    documentoIdentidad, nombreCompleto, email, numContacto, contrasena,
                    direccionResidencia, idRole, idestado, idPuestos
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """
            return jdbcTemplate.update(
                sql,
                usuario.documentoIdentidad,
                usuario.nombreCompleto,
                usuario.email,
                usuario.numContacto,
                hashedPassword,
                usuario.direccionResidencia,
                usuario.idRole,
                usuario.idEstado,
                usuario.idPuestos
            )
        }

        fun allUsers(): List<UsersName> {
            val sql = "SELECT nombreCompleto FROM usuarios WHERE idestado = 1"
            return jdbcTemplate.query(sql) { rs, _ ->
                UsersName(
                    nombreCompleto = rs.getString("nombreCompleto")
                )
            }
        }

        fun detallesUsuarios(): List<Users> {
            val sql = "SELECT * FROM usuarios"
            return jdbcTemplate.query(sql) { rs, _ ->
                Users(
                    documentoIdentidad = rs.getString("documentoIdentidad"),
                    nombreCompleto = rs.getString("nombreCompleto"),
                    email = rs.getString("email"),
                    numContacto = rs.getString("numContacto"),
                    contrasena = rs.getString("contrasena"),
                    direccionResidencia = rs.getString("direccionResidencia"),
                    idRole = rs.getLong("idRole"),
                    idEstado = rs.getLong("idestado"),
                    idPuestos = rs.getLong("idPuestos")
                )
            }
        }

        fun editarUsuario(dto: UserUpdateDTO, id: String): Int {
            val sql = """
        UPDATE usuarios u
        SET 
            u.nombreCompleto = COALESCE(?, u.nombreCompleto),
            u.numContacto = COALESCE(?, u.numContacto),
            u.direccionResidencia = COALESCE(?, u.direccionResidencia)
            WHERE u.email = ? OR u.documentoIdentidad = ?
    """
            return jdbcTemplate.update(
                sql,
                dto.nombreCompleto,
                dto.numContacto,
                dto.direccionResidencia,
                id,
                id
            )
        }

        /*
        * fun desactivarUsuario(): {
aaaaaaaaaaaaaaaaaaaaaaa
        }
        * */

        /*fun activarUsuario(): {
aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaakjjjjjjjjjjjjjjjjjjjjjjjjjj
        }
        */


        fun eliminarUsuario(id: String): Int {
            val sql = "DELETE FROM usuarios WHERE documentoIdentidad = ? OR email = ?"
            return jdbcTemplate.update(sql, id, id)
        }



    }