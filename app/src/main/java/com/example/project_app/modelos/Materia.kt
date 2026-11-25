package com.example.project_app.modelos

// Data class: molde sencillo para guardar cada nota
data class Nota(
    val nombre: String,
    val valor: Double,
    val porcentaje: Double
)

// Clase propia con atributos, lista mutable y métodos básicos
class Materia(
    val nombre: String,
    val semestre: Int,
    val creditos: Int
) {
    // Lista mutable de notas (puedes usar .add(), .removeAt(), .size igual que en clase)
    val notas = mutableListOf<Nota>()

    // Ciclo 'for' sencillo para calcular la nota definitiva
    fun calcularDefinitiva(): Double {
        var definitiva = 0.0
        for (nota in notas) {
            definitiva += nota.valor * (nota.porcentaje / 100.0)
        }
        return definitiva
    }

    // Condicional 'when' para saber si aprobó o reprobó
    fun obtenerEstado(): String {
        val def = calcularDefinitiva()
        return when {
            notas.isEmpty() -> "Sin notas"
            def >= 3.0 -> "Aprobado"
            else -> "Reprobado"
        }
    }
}