package com.example.project_app.actividades

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_app.R
import com.example.project_app.modelos.DatosApp

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Ajuste para que el teclado empuje los bloques hacia arriba
        val contenedorPrincipal = findViewById<View>(R.id.main)
        val paddingInferiorOriginal = contenedorPrincipal.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(contenedorPrincipal) { vista, insets ->
            val alturaTeclado = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            vista.setPadding(
                vista.paddingLeft,
                vista.paddingTop,
                vista.paddingRight,
                paddingInferiorOriginal + alturaTeclado
            )
            insets
        }

        // Enlazar las vistas del XML
        val usernameInput = findViewById<EditText>(R.id.username_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val loginBtn = findViewById<Button>(R.id.login_btn)
        val registerBtn = findViewById<Button>(R.id.register_btn)

        // Acción del botón Login
        loginBtn.setOnClickListener {
            val usuario = usernameInput.text?.toString()?.trim() ?: ""
            val contrasena = passwordInput.text?.toString()?.trim() ?: ""

            if (!validarCamposNoVacios(usuario, contrasena, usernameInput, passwordInput)) {
                return@setOnClickListener
            }

            if (verificarCredenciales(usuario, contrasena)) {
                Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                irAPantallaMaterias()
            } else {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        // Acción del botón Registrar Usuario
        registerBtn.setOnClickListener {
            val usuario = usernameInput.text?.toString()?.trim() ?: ""
            val contrasena = passwordInput.text?.toString()?.trim() ?: ""

            if (!validarCamposNoVacios(usuario, contrasena, usernameInput, passwordInput)) {
                return@setOnClickListener
            }

            if (!usuario.contains("@")) {
                usernameInput.error = "Ingresa un correo válido (ej: nombre@correo.com)"
                usernameInput.requestFocus()
                return@setOnClickListener
            }

            if (DatosApp.registrarEstudiante(usuario, contrasena)) {
                Toast.makeText(this, "Usuario registrado. Ahora puedes presionar Login", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Ese correo ya está registrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validarCamposNoVacios(
        usuario: String,
        contrasena: String,
        campoUsuario: EditText,
        campoContrasena: EditText
    ): Boolean {
        if (usuario.isEmpty()) {
            campoUsuario.error = "Ingresa tu correo estudiantil"
            campoUsuario.requestFocus()
            return false
        }
        if (contrasena.isEmpty()) {
            campoContrasena.error = "Ingresa tu contraseña"
            campoContrasena.requestFocus()
            return false
        }
        return true
    }

    private fun verificarCredenciales(usuario: String, contrasena: String): Boolean {
        return DatosApp.iniciarSesion(usuario, contrasena)
    }

    private fun irAPantallaMaterias() {
        val intent = Intent(this, MateriasActivity::class.java)
        startActivity(intent)
        finish()
    }
}