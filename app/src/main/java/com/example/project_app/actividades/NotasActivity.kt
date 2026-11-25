package com.example.project_app.actividades

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_app.R
import com.example.project_app.modelos.DatosApp
import com.example.project_app.modelos.Materia
import com.example.project_app.modelos.Nota

class NotasActivity : AppCompatActivity() {

    private var materiaActual: Materia? = null

    private lateinit var tvTituloMateria: TextView
    private lateinit var tvSubtituloMateria: TextView
    private lateinit var tvDefinitivaMateria: TextView
    private lateinit var tvPorcentajeAcumulado: TextView
    private lateinit var tvEstadoMateria: TextView
    private lateinit var tvSinNotas: TextView
    private lateinit var lvNotas: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notas)

        ajustarMargenes()

        // 1. Recuperar la posición de la materia que se tocó en MateriasActivity
        val posicionMateria = intent.getIntExtra("POSICION_MATERIA", -1)
        val estudiante = DatosApp.estudianteActual

        if (estudiante == null || posicionMateria !in estudiante.materias.indices) {
            Toast.makeText(this, "No se pudo cargar la materia", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        materiaActual = estudiante.materias[posicionMateria]

        // 2. Enlazar vistas del XML
        tvTituloMateria = findViewById(R.id.tvTituloMateria)
        tvSubtituloMateria = findViewById(R.id.tvSubtituloMateria)
        tvDefinitivaMateria = findViewById(R.id.tvDefinitivaMateria)
        tvPorcentajeAcumulado = findViewById(R.id.tvPorcentajeAcumulado)
        tvEstadoMateria = findViewById(R.id.tvEstadoMateria)
        tvSinNotas = findViewById(R.id.tvSinNotas)
        lvNotas = findViewById(R.id.lvNotas)

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val etNombreNota = findViewById<EditText>(R.id.etNombreNota)
        val etValorNota = findViewById<EditText>(R.id.etValorNota)
        val etPorcentajeNota = findViewById<EditText>(R.id.etPorcentajeNota)
        val btnAgregarNota = findViewById<Button>(R.id.btnAgregarNota)

        // 3. Botón Volver: regresa a MateriasActivity (donde se actualizará el promedio general)
        btnVolver.setOnClickListener {
            finish()
        }

        // 4. Botón + Agregar Nota
        btnAgregarNota.setOnClickListener {
            agregarNuevaNota(etNombreNota, etValorNota, etPorcentajeNota)
        }

        // Mostrar los datos iniciales de la materia
        actualizarPantalla()
    }

    private fun agregarNuevaNota(
        campoNombre: EditText,
        campoValor: EditText,
        campoPorcentaje: EditText
    ) {
        val materia = materiaActual ?: return

        val nombre = campoNombre.text?.toString()?.trim() ?: ""
        val valorTexto = campoValor.text?.toString()?.trim() ?: ""
        val porcentajeTexto = campoPorcentaje.text?.toString()?.trim() ?: ""

        // Validar campos vacíos
        if (nombre.isEmpty()) {
            campoNombre.error = "Escribe la descripción (Ej: Parcial 1)"
            campoNombre.requestFocus()
            return
        }
        if (valorTexto.isEmpty()) {
            campoValor.error = "Ingresa la nota"
            campoValor.requestFocus()
            return
        }
        if (porcentajeTexto.isEmpty()) {
            campoPorcentaje.error = "Ingresa el porcentaje"
            campoPorcentaje.requestFocus()
            return
        }

        // Conversión segura de texto a decimal
        val valor = valorTexto.toDoubleOrNull() ?: -1.0
        val porcentaje = porcentajeTexto.toDoubleOrNull() ?: -1.0

        // Validar rango de nota (0.0 a 5.0)
        if (valor < 0.0 || valor > 5.0) {
            campoValor.error = "La nota debe estar entre 0.0 y 5.0"
            campoValor.requestFocus()
            return
        }

        // Validar rango de porcentaje y que el acumulado no pase del 100%
        val porcentajeAcumulado = calcularPorcentajeTotal(materia)
        if (porcentaje <= 0.0 || porcentajeAcumulado + porcentaje > 100.0) {
            val disponible = 100.0 - porcentajeAcumulado
            campoPorcentaje.error = "Porcentaje inválido (disponible: ${String.format("%.0f", disponible)}%)"
            campoPorcentaje.requestFocus()
            return
        }

        // Agregar la nota a la lista mutable de la materia
        val nuevaNota = Nota(nombre, valor, porcentaje)
        materia.notas.add(nuevaNota)

        // Limpiar las cajas de texto para registrar otra nota cómodamente
        campoNombre.setText("")
        campoValor.setText("")
        campoPorcentaje.setText("")
        campoNombre.requestFocus()

        Toast.makeText(this, "Nota agregada", Toast.LENGTH_SHORT).show()
        actualizarPantalla()
    }

    // Ciclo 'for' sencillo para sumar cuánto porcentaje lleva evaluado la materia
    private fun calcularPorcentajeTotal(materia: Materia): Double {
        var suma = 0.0
        for (nota in materia.notas) {
            suma += nota.porcentaje
        }
        return suma
    }

    // Refresca la tarjeta azul y la lista inferior
    private fun actualizarPantalla() {
        val materia = materiaActual ?: return

        tvTituloMateria.text = materia.nombre
        tvSubtituloMateria.text = "Semestre ${materia.semestre} • ${materia.creditos} créditos"
        tvDefinitivaMateria.text = String.format("%.1f", materia.calcularDefinitiva())

        val evaluado = calcularPorcentajeTotal(materia)
        tvPorcentajeAcumulado.text = "Evaluado: ${String.format("%.0f", evaluado)}%"
        tvEstadoMateria.text = materia.obtenerEstado()

        if (materia.notas.isEmpty()) {
            tvSinNotas.visibility = View.VISIBLE
            lvNotas.visibility = View.GONE
        } else {
            tvSinNotas.visibility = View.GONE
            lvNotas.visibility = View.VISIBLE

            val listaTextos = mutableListOf<String>()
            for (nota in materia.notas) {
                listaTextos.add("${nota.nombre}  —  Nota: ${nota.valor} (${nota.porcentaje}%)")
            }

            lvNotas.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaTextos)
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