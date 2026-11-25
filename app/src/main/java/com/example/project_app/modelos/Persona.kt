package com.example.project_app.modelos

// Clase padre sencilla (cumple con Herencia)
open class Persona(
    val nombre: String,
    val correo: String
)

// Clase hija: hereda nombre y correo de Persona
class Estudiante(
    nombre: String,
    correo: String,
    val contrasena: String
) : Persona(nombre, correo) {

    // Lista mutable igual que en tu clase: aquí se guardan sus materias
    val materias = mutableListOf<Materia>()

    fun calcularTotalCreditos(): Int {
        var total = 0
        for (materia in materias) {
            total += materia.creditos
        }
        return total
    }

    fun calcularPromedioGeneral(): Double {
        var suma = 0.0
        var totalCreditos = 0

        for (materia in materias) {
            if (materia.notas.isNotEmpty()) {
                suma += materia.calcularDefinitiva() * materia.creditos
                totalCreditos += materia.creditos
            }
        }

        if (totalCreditos == 0) return 0.0
        return suma / totalCreditos
    }
}