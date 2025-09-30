package com.example.appinterface

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appinterface.Adapter.Models.ReporteExterno
import com.example.appinterface.Api.ApiServicesKotlin
import com.example.appinterface.Api.RetrofitInstance
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

            // Crear objeto ReporteExterno
            val reporte = ReporteExterno(
                idcategoriaReportes = 1, // Por defecto categoría 1
                descripcion = description,
                idusuarios = email
            )

            // Enviar reporte al backend
            enviarReporte(reporte, emailField, descriptionField)
        }
    }

    private fun enviarReporte(reporte: ReporteExterno, emailField: TextInputEditText, descriptionField: TextInputEditText) {
        Log.d("ReporteExterno", "Enviando reporte: ${reporte.descripcion}")
        
        val api = RetrofitInstance.api2kotlin
        val call = api.enviarReporteExterno(reporte)
        
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("ReporteExterno", "Response code: ${response.code()}")
                
                if (response.isSuccessful) {
                    try {
                        val message = response.body()?.string() ?: "Reporte enviado exitosamente"
                        Log.d("ReporteExterno", "Reporte enviado exitosamente: $message")
                        
                        Toast.makeText(this@ReporteExternoActivity, "Reporte enviado exitosamente", Toast.LENGTH_LONG).show()
                        
                        // Limpiar campos después del envío exitoso
                        emailField.text?.clear()
                        descriptionField.text?.clear()
                        
                    } catch (e: Exception) {
                        Log.e("ReporteExterno", "Error al leer respuesta: ${e.message}")
                        Toast.makeText(this@ReporteExternoActivity, "Reporte enviado exitosamente", Toast.LENGTH_LONG).show()
                        
                        // Limpiar campos de todas formas
                        emailField.text?.clear()
                        descriptionField.text?.clear()
                    }
                    
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ReporteExterno", "Error ${response.code()}: $errorBody")
                    
                    val errorMessage = when (response.code()) {
                        400 -> "Datos inválidos. Verifica la información"
                        500 -> "Error interno del servidor"
                        else -> "Error al enviar reporte: ${response.code()}"
                    }
                    
                    Toast.makeText(this@ReporteExternoActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("ReporteExterno", "Error de red: ${t.message}", t)
                
                val errorMessage = when {
                    t.message?.contains("failed to connect", ignoreCase = true) == true -> 
                        "No se pudo conectar al servidor. Verifica que esté corriendo en localhost:8081"
                    t.message?.contains("timeout", ignoreCase = true) == true -> 
                        "Tiempo de espera agotado. El servidor no responde"
                    else -> "Error de conexión: ${t.message}"
                }
                
                Toast.makeText(this@ReporteExternoActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }
}