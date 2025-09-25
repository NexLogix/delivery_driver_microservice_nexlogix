package com.example.appinterface

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.VehiclesAdapter
import com.example.appinterface.Adapter.Vehiculo
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

    private lateinit var sectionContent: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver)

        val driverName = intent.getStringExtra("driverName") ?: "Conductor"
        val tvName = findViewById<TextView>(R.id.driver_name)
        tvName.text = "Hola, $driverName"

        sectionContent = findViewById(R.id.section_content)

        setupMenuButtons()
        // Cargar información del conductor al iniciar
        cargarConductorInfo()
    }

    
    private fun setupMenuButtons() {
        // Hacer el área del menú clickeable para mostrar opciones
        val menuArea = findViewById<android.widget.LinearLayout>(R.id.menu_area)
        val menuIcon = findViewById<android.widget.ImageView>(R.id.menu_icon)
        
        // Hacer todo el área del menú clickeable
        val clickListener = View.OnClickListener {
            showMenuOptions()
        }
        
        menuArea?.setOnClickListener(clickListener)
        menuIcon.setOnClickListener(clickListener)
    }
    
    private fun showMenuOptions() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Seleccionar opción")
        
        val options = arrayOf("🚗 Rutas", "🚚 Vehículos", "📊 Reportes")
        
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    // Rutas - cargar desde API
                    cargarRutasAsignadas()
                }
                1 -> {
                    // Vehículos - cargar desde API
                    cargarVehiculosAsignados()
                }
                2 -> {
                    // Generar reportes - preserve original functionality
                    sectionContent.text = "📊 Reporte generado exitosamente\n\n✅ Rutas completadas: 3\n✅ Vehículos utilizados: 2\n✅ Fecha: ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date())}\n\nEstado: Listo para envío"
                }
            }
            dialog.dismiss()
        }
        
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        
        builder.show()
    }

    // ======= Cargar rutas asignadas ======
    private fun cargarRutasAsignadas() {
        val routesList = findViewById<RecyclerView>(R.id.routes_list)
        val vehiclesList = findViewById<RecyclerView>(R.id.vehicles_list)
        val conductorCard = findViewById<View>(R.id.conductor_card)

        sectionContent.text = "🔄 Cargando rutas asignadas..."

        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("auth_token", null)

        if (token.isNullOrEmpty()) {
            sectionContent.text = "❌ No se encontró token de sesión."
            return
        }

        val api = RetrofitInstance.api2kotlin
        val call = api.getRutasAsignadas("Bearer $token")

        call.enqueue(object : Callback<List<com.example.appinterface.Adapter.Ruta>> {
            override fun onResponse(call: Call<List<com.example.appinterface.Adapter.Ruta>>, response: Response<List<com.example.appinterface.Adapter.Ruta>>) {
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()

                    if (lista.isEmpty()) {
                        sectionContent.text = "ℹ️ No hay rutas asignadas."
                        routesList.visibility = View.GONE
                        vehiclesList.visibility = View.GONE
                        conductorCard.visibility = View.VISIBLE
                    } else {
                        // Mostrar RecyclerView con datos
                        routesList.layoutManager = LinearLayoutManager(this@DriverActivity)
                        routesList.adapter = com.example.appinterface.Adapter.RoutesAdapter(lista)
                        routesList.visibility = View.VISIBLE
                        vehiclesList.visibility = View.GONE
                        conductorCard.visibility = View.GONE
                        sectionContent.text = ""
                    }
                } else {
                    sectionContent.text = "❌ Error al obtener rutas: ${response.code()}"
                    routesList.visibility = View.GONE
                    vehiclesList.visibility = View.GONE
                    conductorCard.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<List<com.example.appinterface.Adapter.Ruta>>, t: Throwable) {
                sectionContent.text = "⚠️ No se pudo conectar al servidor: ${t.message}\nMostrando datos de ejemplo."

                // Mostrar mock de ejemplo basado en el JSON proporcionado
                val ejemplo = listOf(
                    com.example.appinterface.Adapter.Ruta(
                        documentoIdentidad = "1154789123",
                        email = "josenorbert1959@gmail.com",
                        placa = "QPO89E",
                        marcaVehiculo = "Fiat - Strada",
                        tipoVehiculo = "B1",
                        capacidad = 0,
                        estadoVehiculo = "disponible",
                        fechaAsignacionInicio = "2025-09-15T19:00:00",
                        fechaAsignacionFinalizacion = "2025-10-14T19:00:00",
                        nombreRuta = "A14",
                        horaInicioRuta = "",
                        horaFinalizacionRuta = "",
                        estadoRuta = "EN_BODEGA",
                        descripcion = "Esta es una descripción extensa del recorrido programado para la ruta nacional A12."
                    ),
                    com.example.appinterface.Adapter.Ruta(
                        documentoIdentidad = "1154789123",
                        email = "josenorbert1959@gmail.com",
                        placa = "QPO89E",
                        marcaVehiculo = "Fiat - Strada",
                        tipoVehiculo = "B1",
                        capacidad = 0,
                        estadoVehiculo = "disponible",
                        fechaAsignacionInicio = "2025-09-15T19:00:00",
                        fechaAsignacionFinalizacion = "2025-10-14T19:00:00",
                        nombreRuta = "A14",
                        horaInicioRuta = "",
                        horaFinalizacionRuta = "",
                        estadoRuta = "EN_BODEGA",
                        descripcion = "Esta es una descripción extensa del recorrido programado para la ruta nacional A12."
                    )
                )

                routesList.layoutManager = LinearLayoutManager(this@DriverActivity)
                routesList.adapter = com.example.appinterface.Adapter.RoutesAdapter(ejemplo)
                routesList.visibility = View.VISIBLE
                vehiclesList.visibility = View.GONE
                conductorCard.visibility = View.GONE
            }
        })
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

    // ======= Cargar información del conductor autenticado ======
    private fun cargarConductorInfo() {
        sectionContent.text = "🔄 Cargando información del conductor..."

        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("auth_token", null)

        if (token.isNullOrEmpty()) {
            sectionContent.text = "❌ No se encontró token de sesión."
            return
        }

        val api = RetrofitInstance.api2kotlin
        val call = api.getConductorInfo("Bearer $token")

        call.enqueue(object : Callback<com.example.appinterface.Adapter.Conductor> {
            override fun onResponse(call: Call<com.example.appinterface.Adapter.Conductor>, response: Response<com.example.appinterface.Adapter.Conductor>) {
                if (response.isSuccessful) {
                    val conductor = response.body()
                    if (conductor != null) {
                        mostrarConductor(conductor)
                    } else {
                        sectionContent.text = "❌ Respuesta vacía del servidor"
                    }
                } else {
                    sectionContent.text = "❌ Error del servidor: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<com.example.appinterface.Adapter.Conductor>, t: Throwable) {
                sectionContent.text = "⚠️ No se pudo conectar al servidor: ${t.message}\nMostrando datos de ejemplo."
                // Mostrar ejemplo mock (según JSON proporcionado)
                val ejemplo = com.example.appinterface.Adapter.Conductor(
                    idConductor = 30,
                    role = "CONDUCTOR",
                    documentoIdentidad = "1154789123",
                    email = "josenorbert1959@gmail.com",
                    numContacto = "3208529272",
                    direccionResidencia = "calle XD",
                    licencia = "GTP78Q",
                    tipoLicencia = "C1",
                    vigenciaLicencia = "2027-09-17",
                    idEstadoConductor = 1,
                    idEstadoUsuarioControl = 1
                )
                mostrarConductor(ejemplo)
            }
        })
    }

    private fun mostrarConductor(conductor: com.example.appinterface.Adapter.Conductor) {
        val sb = StringBuilder()
        sb.append("Conductor:\n\n")
        sb.append("Role: ${conductor.role}\n")
        sb.append("Documento: ${conductor.documentoIdentidad}\n")
        sb.append("Email: ${conductor.email}\n")
        sb.append("Contacto: ${conductor.numContacto}\n")
        sb.append("Dirección: ${conductor.direccionResidencia}\n")
        sb.append("Licencia: ${conductor.licencia} (${conductor.tipoLicencia})\n")
        sb.append("Vigencia licencia: ${conductor.vigenciaLicencia}\n")
       
        sectionContent.text = sb.toString()
    }

    // ======= Cargar vehículos asignados ======
    private fun cargarVehiculosAsignados() {
        // Preparar UI
        val vehiclesList = findViewById<RecyclerView>(R.id.vehicles_list)
        val routesList = findViewById<RecyclerView>(R.id.routes_list)
        val conductorCard = findViewById<View>(R.id.conductor_card)

        sectionContent.text = "🔄 Cargando vehículos asignados..."

        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("auth_token", null)

        if (token.isNullOrEmpty()) {
            sectionContent.text = "❌ No se encontró token de sesión."
            return
        }

        val api = RetrofitInstance.api2kotlin
        val call = api.getVehiculosAsignados("Bearer $token")

        // Mostrar loader text while loading
        call.enqueue(object : Callback<List<Vehiculo>> {
            override fun onResponse(call: Call<List<Vehiculo>>, response: Response<List<Vehiculo>>) {
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()

                    if (lista.isEmpty()) {
                        sectionContent.text = "ℹ️ No hay vehículos asignados."
                        vehiclesList.visibility = View.GONE
                        routesList.visibility = View.GONE
                        conductorCard.visibility = View.VISIBLE
                    } else {
                        // Mostrar RecyclerView con datos
                        vehiclesList.layoutManager = LinearLayoutManager(this@DriverActivity)
                        vehiclesList.adapter = VehiclesAdapter(lista)
                        vehiclesList.visibility = View.VISIBLE
                        routesList.visibility = View.GONE
                        conductorCard.visibility = View.GONE
                        sectionContent.text = ""
                    }
                } else {
                    sectionContent.text = "❌ Error al obtener vehículos: ${response.code()}"
                    vehiclesList.visibility = View.GONE
                    routesList.visibility = View.GONE
                    conductorCard.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<List<Vehiculo>>, t: Throwable) {
                sectionContent.text = "⚠️ No se pudo conectar al servidor: ${t.message}\nMostrando datos de ejemplo."

                // Mostrar mock de ejemplo
                val ejemplo = listOf(
                    Vehiculo(
                        documentoIdentidad = "1154789123",
                        email = "josenorbert1959@gmail.com",
                        placa = "ABC-123",
                        marcaVehiculo = "Hino",
                        tipoVehiculo = "Camión",
                        capacidad = 1200,
                        estadoVehiculo = "Disponible",
                        ultimoMantenimiento = "2023-04-01",
                        fechaAsignacionInicio = "2024-06-12",
                        fechaEntregaVehiculo = null
                    ),
                    Vehiculo(
                        documentoIdentidad = "1154789123",
                        email = "josenorbert1959@gmail.com",
                        placa = "XYZ-789",
                        marcaVehiculo = "Mercedes",
                        tipoVehiculo = "Tráiler",
                        capacidad = 2000,
                        estadoVehiculo = "En servicio",
                        ultimoMantenimiento = "2024-01-15",
                        fechaAsignacionInicio = "2024-06-10",
                        fechaEntregaVehiculo = null
                    )
                )

                vehiclesList.layoutManager = LinearLayoutManager(this@DriverActivity)
                vehiclesList.adapter = VehiclesAdapter(ejemplo)
                vehiclesList.visibility = View.VISIBLE
                routesList.visibility = View.GONE
                conductorCard.visibility = View.GONE
            }
        })
    }

}
