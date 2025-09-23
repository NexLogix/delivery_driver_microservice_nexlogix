package com.example.appinterface

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appinterface.Api.ApiServicesKotlin
import com.example.appinterface.Api.LogoutResponse
import com.example.appinterface.Api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver)

        val driverName = intent.getStringExtra("driverName") ?: "Conductor"
        val tvName = findViewById<TextView>(R.id.driver_name)
        tvName.text = "Hola, $driverName"

        val btnRutas = findViewById<Button>(R.id.btn_rutas)
        val btnVehiculos = findViewById<Button>(R.id.btn_vehiculos)
        val btnReporte = findViewById<Button>(R.id.btn_reporte)

        btnRutas.setOnClickListener {
            // mock action: show rutas assigned (here we just change text)
            findViewById<TextView>(R.id.section_content).text = "Rutas asignadas:\n- Ruta A\n- Ruta B\n- Ruta C"
        }

        btnVehiculos.setOnClickListener {
            findViewById<TextView>(R.id.section_content).text = "Vehículos asignados:\n- Camión 01\n- Camión 07"
        }

        btnReporte.setOnClickListener {
            findViewById<TextView>(R.id.section_content).text = "Reporte generado: OK (mock)"
        }
    }

    fun logout(v: View) {
        Log.d("DriverActivity", "Iniciando logout...")
        
        // Obtener token guardado
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("auth_token", null)
        
        if (token.isNullOrEmpty()) {
            Log.w("DriverActivity", "No hay token guardado, logout local")
            performLocalLogout()
            return
        }
        
        // Llamada al backend para logout
        val api = RetrofitInstance.api2kotlin
        val authHeader = "Bearer $token"
        Log.d("DriverActivity", "Enviando logout al servidor con token: ${token.substring(0, 10)}...")
        
        val call = api.logout(authHeader)
        call.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("DriverActivity", "Logout exitoso: ${body?.message}")
                    Toast.makeText(this@DriverActivity, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Log.w("DriverActivity", "Logout falló en servidor: ${response.code()}, pero continuando con logout local")
                    Toast.makeText(this@DriverActivity, "Sesión cerrada localmente", Toast.LENGTH_SHORT).show()
                }
                performLocalLogout()
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                Log.e("DriverActivity", "Error en logout: ${t.message}", t)
                Toast.makeText(this@DriverActivity, "Error de conexión, cerrando sesión localmente", Toast.LENGTH_SHORT).show()
                performLocalLogout()
            }
        })
    }
    
    private fun performLocalLogout() {
        // Limpiar token guardado
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().remove("auth_token").apply()
        
        // Regresar a MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}
