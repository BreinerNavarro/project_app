package com.example.project_app.actividades

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_app.R
import com.example.project_app.modelos.DatosApp
import com.example.project_app.modelos.Materia

class AgregarMateriaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_materia)

        ajustarMargenes()

        // 1. Enlazar las vistas del XML
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val etNombreMateria = findViewById<EditText>(R.id.etNombreMateria)
        val etSemestre = findViewById<EditText>(R.id.etSemestre)
        val etCreditos = findViewById<EditText>(R.id.etCreditos)
        val btnGuardarMateria = findViewById<Button>(R.id.btnGuardarMateria)

        // 2. Botón Volver: cierra esta pantalla y regresa a la lista de materias
        btnVolver.setOnClickListener {
            finish()
        }

        // 3. Botón Guardar Materia: valida los datos y agrega la materia a la lista
        btnGuardarMateria.setOnClickListener {
            guardarNuevaMateria(etNombreMateria, etSemestre, etCreditos)
        }
    }

    // Función principal para validar entradas y guardar en la lista mutable del estudiante
    private fun guardarNuevaMateria(
        campoNombre: EditText,
        campoSemestre: EditText,
        campoCreditos: EditText
    ) {
        // Lectura segura evitando valores nulos
        val nombre = campoNombre.text?.toString()?.trim() ?: ""
        val semestreTexto = campoSemestre.text?.toString()?.trim() ?: ""
        val creditosTexto = campoCreditos.text?.toString()?.trim() ?: ""

        // Validar que ningún campo quede vacío
        if (nombre.isEmpty()) {
            campoNombre.error = "Escribe el nombre de la materia"
            campoNombre.requestFocus()
            return
        }
        if (semestreTexto.isEmpty()) {
            campoSemestre.error = "Escribe el semestre"
            campoSemestre.requestFocus()
            return
        }
        if (creditosTexto.isEmpty()) {
            campoCreditos.error = "Escribe los créditos"
            campoCreditos.requestFocus()
            return
        }

        // Conversión segura de texto a número entero (cumple Nulabilidad sin usar !!)
        val semestre = semestreTexto.toIntOrNull() ?: 0
        val creditos = creditosTexto.toIntOrNull() ?: 0

        // Validar que los números tengan sentido
        if (semestre < 1 || semestre > 12) {
            campoSemestre.error = "El semestre debe estar entre 1 y 12"
            campoSemestre.requestFocus()
            return
        }
        if (creditos < 1 || creditos > 10) {
            campoCreditos.error = "Los créditos deben estar entre 1 y 10"
            campoCreditos.requestFocus()
            return
        }

        // Crear el objeto Materia y agregarlo a la lista mutable del estudiante activo
        val nuevaMateria = Materia(nombre, semestre, creditos)
        val estudiante = DatosApp.estudianteActual

        if (estudiante != null) {
            estudiante.materias.add(nuevaMateria)
            Toast.makeText(this, "Materia '$nombre' agregada", Toast.LENGTH_SHORT).show()
            finish() // Cierra esta pantalla y vuelve automáticamente a MateriasActivity
        } else {
            Toast.makeText(this, "Error: no hay sesión activa", Toast.LENGTH_SHORT).show()
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