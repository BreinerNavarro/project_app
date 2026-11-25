package com.example.project_app.modelos

object DatosApp {

    // Encapsulamiento (private): lista mutable con los usuarios quemados
    private val listaEstudiantes = mutableListOf(
        Estudiante("test", "test@test", "1234"),
        Estudiante("Tawnee Soriano", "tawnee@correo.com", "1234"),
        Estudiante("Breiner Navarro", "breiner@correo.com", "1234"),
        Estudiante("Compañero Prueba", "companero@correo.com", "5678")
    )

    // Guarda el estudiante que entró (es null antes de iniciar sesión)
    var estudianteActual: Estudiante? = null

    fun iniciarSesion(correo: String, contrasena: String): Boolean {
        for (est in listaEstudiantes) {
            if (est.correo == correo && est.contrasena == contrasena) {
                estudianteActual = est
                return true
            }
        }
        return false
    }

    fun cerrarSesion() {
        estudianteActual = null
    }
}