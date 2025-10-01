package com.example.appinterface

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Context
import android.widget.Toast
import android.util.Log
import com.example.appinterface.Api.AuthRequest
import com.example.appinterface.Api.ApiServicesKotlin
import com.example.appinterface.Api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val loginBtn = findViewById<Button>(R.id.buttonLogin)
        val reporteBtn = findViewById<Button>(R.id.button3)
        val errorText = findViewById<TextView>(R.id.login_error)

        // Botón Hacer Reporte
        reporteBtn.setOnClickListener {
            val intent = Intent(this, ReporteExternoActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            val user = username.text.toString().trim()
            val pass = password.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                errorText.text = "Completa email y contraseña"
                errorText.visibility = android.view.View.VISIBLE
                return@setOnClickListener
            }

            // Validar formato de email básico
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
                errorText.text = "Ingresa un email válido"
                errorText.visibility = android.view.View.VISIBLE
                return@setOnClickListener
            }

            // Ocultar error anterior
            errorText.visibility = android.view.View.GONE

            Log.d("MainActivity", "Intentando login con email: $user")
            Log.d("MainActivity", "URL base: ${RetrofitInstance.BASE_URL_APIKOTLIN}")

            // Llamada Retrofit al backend local
            val api = RetrofitInstance.api2kotlin
            val authRequest = AuthRequest(user, pass)
            Log.d("MainActivity", "Enviando request: email=${authRequest.email}, contrasena=***")
            
            val call = api.login(authRequest)
            call.enqueue(object : Callback<com.example.appinterface.Api.AuthResponse> {
                override fun onResponse(call: Call<com.example.appinterface.Api.AuthResponse>, response: Response<com.example.appinterface.Api.AuthResponse>) {
                    Log.d("MainActivity", "Response code: ${response.code()}")
                    Log.d("MainActivity", "Response message: ${response.message()}")
                    
                    if (response.isSuccessful) {
                        val body = response.body()
                        val token = body?.token
                        Log.d("MainActivity", "Login exitoso, token: ${token?.substring(0, 10)}...")
                        
                        if (!token.isNullOrEmpty()) {
                            // Guardar token en SharedPreferences
                            val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                            prefs.edit().putString("auth_token", token).apply()

                            // navegar a DriverActivity
                            val intent = Intent(this@MainActivity, DriverActivity::class.java)
                            intent.putExtra("driverName", user)
                            startActivity(intent)
                            finish()
                        } else {
                            errorText.text = "Credenciales inválidas"
                            errorText.visibility = android.view.View.VISIBLE
                        }
                    } else {
                        // Obtener más información del error
                        val errorBody = response.errorBody()?.string()
                        Log.e("MainActivity", "Error ${response.code()}: $errorBody")
                        
                        val errorMessage = when (response.code()) {
                            400 -> "Datos inválidos. Verifica email y contraseña"
                            401 -> "Credenciales incorrectas"
                            404 -> "Servidor no encontrado"
                            500 -> "Error interno del servidor"
                            else -> "Error en servidor: ${response.code()}"
                        }
                        
                        errorText.text = errorMessage
                        errorText.visibility = android.view.View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<com.example.appinterface.Api.AuthResponse>, t: Throwable) {
                    Log.e("MainActivity", "Error de red: ${t.message}", t)
                    
                    val errorMessage = when {
                        t.message?.contains("failed to connect", ignoreCase = true) == true -> 
                            "No se pudo conectar al servidor. Verifica que esté corriendo en localhost:8081"
                        t.message?.contains("timeout", ignoreCase = true) == true -> 
                            "Tiempo de espera agotado. El servidor no responde"
                        else -> "Error de conexión: ${t.message}"
                    }
                    
                    errorText.text = errorMessage
                    errorText.visibility = android.view.View.VISIBLE
                }
            })
        }
    }

}
