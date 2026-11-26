package com.example.project_app.modelos

data class Nota(
    val nombre: String,
    var valor: Double?,      // 'var' permite editarla; 'null' significa que aún no tiene nota
    val porcentaje: Double
)

class Materia(
    val nombre: String,
    val semestre: Int,
    val creditos: Int
) {
    // Lista mutable con los 3 cortes fijos de la materia
    val cortes = mutableListOf(
        Nota("Corte 1", null, 30.0),
        Nota("Corte 2", null, 30.0),
        Nota("Corte 3", null, 40.0)
    )

    // Revisa si al menos un corte ya tiene nota registrada
    fun tieneNotas(): Boolean {
        for (corte in cortes) {
            if (corte.valor != null) return true
        }
        return false
    }

    // Suma ponderada de los cortes que ya tienen nota
    fun calcularDefinitiva(): Double {
        var definitiva = 0.0
        for (corte in cortes) {
            val nota = corte.valor
            if (nota != null) {
                definitiva += nota * (corte.porcentaje / 100.0)
            }
        }
        return definitiva
    }

    // Calcula cuánto necesita en el Corte 3 (40%) para pasar en 3.0
    fun calcularNotaNecesariaCorte3(): Double? {
        val nota1 = cortes[0].valor
        val nota2 = cortes[1].valor

        if (nota1 != null && nota2 != null) {
            val acumulado = (nota1 * 0.30) + (nota2 * 0.30)
            val faltante = 3.0 - acumulado
            return faltante / 0.40
        }
        return null
    }

    // Estado según la definitiva actual
    fun obtenerEstado(): String {
        val def = calcularDefinitiva()
        return when {
            !tieneNotas() -> "Sin notas"
            def >= 3.0 -> "Aprobado"
            else -> "Reprobado"
        }
    }
}