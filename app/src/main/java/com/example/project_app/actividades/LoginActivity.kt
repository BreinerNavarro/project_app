package com.example.project_app.actividades

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project_app.R
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    // Credenciales de prueba (como pusiste inputType="textEmailAddress", usamos un correo)
    private val correoValido = "test@test"
    private val contrasenaValida = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Detecta la altura del teclado y empuja el contenido hacia arriba
        val contenedorPrincipal = findViewById<android.view.View>(R.id.main)
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

        // Enlazar las vistas del XML con variables en Kotlin
        val usernameInput = findViewById<EditText>(R.id.username_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val loginBtn = findViewById<Button>(R.id.login_btn)

        // Acción al presionar el botón Login
        loginBtn.setOnClickListener {
            // Lectura segura evitando nulos
            val usuario = usernameInput.text?.toString()?.trim() ?: ""
            val contrasena = passwordInput.text?.toString()?.trim() ?: ""

            // Validamos que no haya campos vacíos
            if (!validarCamposNoVacios(usuario, contrasena, usernameInput, passwordInput)) {
                return@setOnClickListener
            }

            // Validamos las credenciales
            if (verificarCredenciales(usuario, contrasena)) {
                Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                irAPantallaMaterias(usuario)
            } else {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Valida campos vacíos y muestra el mensaje de alerta en la caja correspondiente
    private fun validarCamposNoVacios(
        usuario: String,
        contrasena: String,
        campoUsuario: EditText,
        campoContrasena: EditText
    ): Boolean {
        if (usuario.isEmpty()) {
            campoUsuario.error = "Ingresa un correo valido"
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

    // Compara las credenciales (con parámetros y retorno Boolean)
    private fun verificarCredenciales(usuario: String, contrasena: String): Boolean {
        return usuario == correoValido && contrasena == contrasenaValida
    }

    // Abre la Pantalla 2 y envía el dato del estudiante
    private fun irAPantallaMaterias(usuario: String) {
        val intent = Intent(this, MateriasActivity::class.java)
        intent.putExtra("USUARIO_ESTUDIANTE", usuario)
        startActivity(intent)
        finish() // Cierra el Login para no volver a él con el botón Atrás
    }
}