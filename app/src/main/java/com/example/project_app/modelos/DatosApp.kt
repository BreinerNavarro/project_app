package com.example.project_app.modelos

object DatosApp {

    // Encapsulamiento (private): lista mutable con los usuarios quemados
    private val listaEstudiantes = mutableListOf(
        Estudiante("test", "test@test", "1234"),
        Estudiante("Tawnee Soriano", "tawnee@correo.com", "1234")
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
    // Agrega un nuevo estudiante a la lista mutable si el correo no existe aún
    fun registrarEstudiante(correo: String, contrasena: String): Boolean {
        for (est in listaEstudiantes) {
            if (est.correo.equals(correo, ignoreCase = true)) {
                return false // El correo ya está registrado
            }
        }

        // Toma el texto antes del '@' como nombre (ej: "laura@correo.com" -> "Laura")
        val nombre = correo.substringBefore("@").replaceFirstChar { it.uppercase() }
        listaEstudiantes.add(Estudiante(nombre, correo, contrasena))
        return true
    }

    fun cerrarSesion() {
        estudianteActual = null
    }
}