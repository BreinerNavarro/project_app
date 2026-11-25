package com.example.project_app.actividades

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_app.R
import com.example.project_app.modelos.DatosApp

class MateriasActivity : AppCompatActivity() {

    // Variables de las vistas que actualizaremos dinámicamente
    private lateinit var tvSaludo: TextView
    private lateinit var tvPromedioGeneral: TextView
    private lateinit var tvTotalCreditos: TextView
    private lateinit var tvSinMaterias: TextView
    private lateinit var lvMaterias: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_materias)

        // Enlazar las vistas del XML
        tvSaludo = findViewById(R.id.tvSaludo)
        tvPromedioGeneral = findViewById(R.id.tvPromedioGeneral)
        tvTotalCreditos = findViewById(R.id.tvTotalCreditos)
        tvSinMaterias = findViewById(R.id.tvSinMaterias)
        lvMaterias = findViewById(R.id.lvMaterias)

        // Botones Salir y Agregar materia
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)
        val btnAgregarMateria = findViewById<Button>(R.id.btnAgregarMateria)

        // Botón Salir: cierra sesión y vuelve al Login
        btnCerrarSesion.setOnClickListener { irAPantallaLogin() }

        // Botón + Nueva Materia: va a la Pantalla 3 (AgregarMateriaActivity)
        btnAgregarMateria.setOnClickListener { irAPantallaAgregarMateria() }

        // Al tocar una materia de la lista: va a la Pantalla 4 (NotasActivity)
        lvMaterias.setOnItemClickListener { _, _, posicion, _ ->
            irAPantallaNotas(posicion)
        }

        // acomodamos los margenes mas estetico
        val mainView = findViewById<android.view.View>(R.id.main)
        val padIzquierdo = mainView.paddingLeft
        val padSuperior = mainView.paddingTop
        val padDerecho = mainView.paddingRight
        val padInferior = mainView.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                padIzquierdo + systemBars.left,
                padSuperior + systemBars.top,
                padDerecho + systemBars.right,
                padInferior + systemBars.bottom
            )
            insets
        }
    }

    // Se ejecuta automáticamente cada vez que volvemos a esta pantalla
    override fun onResume() {
        super.onResume()
        cargarDatosEstudiante()
    }

    private fun cargarDatosEstudiante() {
        // Nulabilidad segura: si no hay estudiante activo, sale sin cerrar la app
        val estudiante = DatosApp.estudianteActual ?: return

        tvSaludo.text = "Hola, ${estudiante.nombre}"
        tvPromedioGeneral.text = String.format("%.1f", estudiante.calcularPromedioGeneral())
        tvTotalCreditos.text = estudiante.calcularTotalCreditos().toString()

        // Condicional if/else: muestra el aviso vacío o pinta las materias
        if (estudiante.materias.isEmpty()) {
            tvSinMaterias.visibility = View.VISIBLE
            lvMaterias.visibility = View.GONE
        } else {
            tvSinMaterias.visibility = View.GONE
            lvMaterias.visibility = View.VISIBLE

            // Lista mutable sencilla (igual que en clase) + ciclo 'for'
            val listaTextos = mutableListOf<String>()
            for (materia in estudiante.materias) {
                val definitiva = String.format("%.1f", materia.calcularDefinitiva())
                listaTextos.add("${materia.nombre} (Sem ${materia.semestre} • ${materia.creditos} créd.)  —  Def: $definitiva")
            }

            // Conecta la lista de textos con el ListView
            lvMaterias.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaTextos)
        }
    }

// Cerrar sesion y devolverme a la pantalla Login
    private fun irAPantallaLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Cierra el Login para no volver a él con el botón Atrás
    }

    // Abrir Pantalla 3 para crear una materia nueva
    private fun irAPantallaAgregarMateria() {
        val intent = Intent(this, AgregarMateriaActivity::class.java)
        startActivity(intent)
    }

    // Abrir Pantalla 4 enviando la posición de la materia seleccionada
    private fun irAPantallaNotas(posicion: Int) {
        val intent = Intent(this, NotasActivity::class.java)
        intent.putExtra("POSICION_MATERIA", posicion)
        startActivity(intent)
    }

}