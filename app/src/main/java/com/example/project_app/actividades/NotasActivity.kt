package com.example.project_app.actividades

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_app.R
import com.example.project_app.modelos.DatosApp
import com.example.project_app.modelos.Materia

class NotasActivity : AppCompatActivity() {

    private var materiaActual: Materia? = null

    private lateinit var tvTituloMateria: TextView
    private lateinit var tvSubtituloMateria: TextView
    private lateinit var tvDefinitivaMateria: TextView
    private lateinit var tvPorcentajeAcumulado: TextView
    private lateinit var tvEstadoMateria: TextView
    private lateinit var tvProyeccion: TextView

    private lateinit var etCorte1: EditText
    private lateinit var etCorte2: EditText
    private lateinit var etCorte3: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notas)

        ajustarMargenes()

        val posicionMateria = intent.getIntExtra("POSICION_MATERIA", -1)
        val estudiante = DatosApp.estudianteActual

        if (estudiante == null || posicionMateria !in estudiante.materias.indices) {
            Toast.makeText(this, "No se pudo cargar la materia", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        materiaActual = estudiante.materias[posicionMateria]

        // Enlazar vistas
        tvTituloMateria = findViewById(R.id.tvTituloMateria)
        tvSubtituloMateria = findViewById(R.id.tvSubtituloMateria)
        tvDefinitivaMateria = findViewById(R.id.tvDefinitivaMateria)
        tvPorcentajeAcumulado = findViewById(R.id.tvPorcentajeAcumulado)
        tvEstadoMateria = findViewById(R.id.tvEstadoMateria)
        tvProyeccion = findViewById(R.id.tvProyeccion)

        etCorte1 = findViewById(R.id.etCorte1)
        etCorte2 = findViewById(R.id.etCorte2)
        etCorte3 = findViewById(R.id.etCorte3)

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val btnGuardarNotas = findViewById<Button>(R.id.btnGuardarNotas)

        btnVolver.setOnClickListener { finish() }
        btnGuardarNotas.setOnClickListener { guardarNotasCortes() }

        cargarNotasGuardadas()
        actualizarResumen()
    }

    // Rellena las 3 cajas con las notas ya registradas para poder verlas o editarlas
    private fun cargarNotasGuardadas() {
        val materia = materiaActual ?: return

        etCorte1.setText(materia.cortes[0].valor?.toString() ?: "")
        etCorte2.setText(materia.cortes[1].valor?.toString() ?: "")
        etCorte3.setText(materia.cortes[2].valor?.toString() ?: "")
    }

    // Valida cada caja (puede estar vacía o tener un número entre 0.0 y 5.0)
    private fun leerNotaDeCampo(campo: EditText): Pair<Boolean, Double?> {
        val texto = campo.text?.toString()?.trim() ?: ""

        // Si se deja vacío, es válido (significa corte sin nota o nota borrada)
        if (texto.isEmpty()) {
            return Pair(true, null)
        }

        val numero = texto.toDoubleOrNull()
        if (numero == null || numero < 0.0 || numero > 5.0) {
            campo.error = "Debe ser entre 0.0 y 5.0"
            campo.requestFocus()
            return Pair(false, null)
        }

        return Pair(true, numero)
    }

    private fun guardarNotasCortes() {
        val materia = materiaActual ?: return

        val (valido1, nota1) = leerNotaDeCampo(etCorte1)
        if (!valido1) return

        val (valido2, nota2) = leerNotaDeCampo(etCorte2)
        if (!valido2) return

        val (valido3, nota3) = leerNotaDeCampo(etCorte3)
        if (!valido3) return

        // Actualiza los 3 cortes en la lista mutable (sirve tanto para crear como para editar)
        materia.cortes[0].valor = nota1
        materia.cortes[1].valor = nota2
        materia.cortes[2].valor = nota3

        Toast.makeText(this, "Notas actualizadas", Toast.LENGTH_SHORT).show()
        actualizarResumen()
    }

    private fun actualizarResumen() {
        val materia = materiaActual ?: return

        tvTituloMateria.text = materia.nombre
        tvSubtituloMateria.text = "Semestre ${materia.semestre} • ${materia.creditos} créditos"
        tvDefinitivaMateria.text = String.format("%.2f", materia.calcularDefinitiva())
        tvEstadoMateria.text = materia.obtenerEstado()

        // Calcular porcentaje evaluado con ciclo 'for'
        var evaluado = 0.0
        for (corte in materia.cortes) {
            if (corte.valor != null) {
                evaluado += corte.porcentaje
            }
        }
        tvPorcentajeAcumulado.text = "Evaluado: ${String.format("%.0f", evaluado)}%"

        // Mostrar cuánto necesita en el Corte 3 para pasar en 3.0
        val necesaria = materia.calcularNotaNecesariaCorte3()
        tvProyeccion.text = when {
            necesaria == null -> "Ingresa Corte 1 y Corte 2 para ver cuánto necesitas en el Corte 3."
            necesaria <= 0.0 -> "¡Ya tienes aprobada la materia! Necesitas 0.0 en el Corte 3."
            necesaria > 5.0 -> "Para pasar en 3.0 necesitas ${String.format("%.2f", necesaria)} en el Corte 3 (supera 5.0)."
            else -> "Para pasar la materia en 3.0 necesitas mínimo: ${String.format("%.2f", necesaria)} en el Corte 3."
        }
    }

    private fun ajustarMargenes() {
        val mainView = findViewById<View>(R.id.main)
        val padIzq = mainView.paddingLeft
        val padSup = mainView.paddingTop
        val padDer = mainView.paddingRight
        val padInf = mainView.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                padIzq + systemBars.left,
                padSup + systemBars.top,
                padDer + systemBars.right,
                padInf + systemBars.bottom
            )
            insets
        }
    }
}