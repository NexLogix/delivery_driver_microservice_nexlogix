package com.example.appinterface

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ReporteExternoActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.reporte_externo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener referencias a los elementos
        val backArea = findViewById<LinearLayout>(R.id.back_area)
        val emailField = findViewById<TextInputEditText>(R.id.editTextTextEmailAddress)
        val descriptionField = findViewById<TextInputEditText>(R.id.editTextDescription)
        val sendButton = findViewById<MaterialButton>(R.id.send_button)

        // Configurar botón de volver
        backArea.setOnClickListener {
            finish() // Cierra la Activity actual y regresa a la anterior
        }

        // Configurar botón de enviar
        sendButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val description = descriptionField.text.toString().trim()

            // Validaciones básicas
            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa tu email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Por favor ingresa un email válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (description.isEmpty()) {
                Toast.makeText(this, "Por favor describe lo que pasó", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: Aquí se puede implementar el envío del reporte al servidor
            // Por ahora solo mostramos un mensaje de confirmación
            Toast.makeText(this, "Reporte enviado exitosamente", Toast.LENGTH_LONG).show()
            
            // Limpiar campos después del envío
            emailField.text?.clear()
            descriptionField.text?.clear()
            
            // Opcional: cerrar la activity después de enviar
            // finish()
        }
    }
}