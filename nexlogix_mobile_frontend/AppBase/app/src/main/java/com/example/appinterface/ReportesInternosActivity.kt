package com.example.appinterface

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.Models.ReporteInterno
import com.example.appinterface.Adapter.ReportesInternosAdapter
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.Api.CategoriaReporte
import com.example.appinterface.Api.ReporteInternoRequest
import com.example.appinterface.Api.ReporteInternoResponse
import com.example.appinterface.Api.SuccessResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.util.*

class ReportesInternosActivity : AppCompatActivity() {

    private lateinit var cardFormulario: CardView
    private lateinit var dropdownCategoria: AutoCompleteTextView
    private lateinit var etDescripcion: TextInputEditText
    private lateinit var btnGuardar: MaterialButton
    private lateinit var btnCancelar: MaterialButton
    private lateinit var recyclerReportes: RecyclerView
    private lateinit var adapter: ReportesInternosAdapter
    
    private var reportes = mutableListOf<ReporteInterno>()
    private var categorias = mutableListOf<CategoriaReporte>()
    private var reporteEditando: ReporteInterno? = null
    private var authToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportes_internos)

        // Verificar token de autenticación
        if (!verificarToken()) {
            return
        }

        initViews()
        setupRecyclerView()
        setupButtons()
        cargarCategorias()
        cargarReportes()
    }

    private fun verificarToken(): Boolean {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        authToken = prefs.getString("auth_token", null)
        
        if (authToken.isNullOrEmpty()) {
            Log.w("ReportesInternosActivity", "No hay token de autenticación")
            Toast.makeText(this, "Sesión expirada. Por favor, inicie sesión nuevamente.", Toast.LENGTH_LONG).show()
            
            // Redirigir al login
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return false
        }
        return true
    }

    private fun initViews() {
        cardFormulario = findViewById(R.id.form_card)
        dropdownCategoria = findViewById(R.id.categoria_dropdown)
        etDescripcion = findViewById(R.id.descripcion_edit_text)
        btnGuardar = findViewById(R.id.save_button)
        btnCancelar = findViewById(R.id.cancel_button)
        recyclerReportes = findViewById(R.id.reportes_list)
        
        // Ocultar formulario inicialmente
        cardFormulario.visibility = View.GONE
    }

    private fun cargarCategorias() {
        val call = RetrofitInstance.api2kotlin.getCategoriasReportes("Bearer $authToken")
        
        call.enqueue(object : Callback<List<CategoriaReporte>> {
            override fun onResponse(call: Call<List<CategoriaReporte>>, response: Response<List<CategoriaReporte>>) {
                if (response.isSuccessful) {
                    response.body()?.let { categoriasResponse ->
                        categorias.clear()
                        categorias.addAll(categoriasResponse)
                        setupCategoriaDropdown()
                        Log.d("ReportesInternosActivity", "Categorías cargadas: ${categorias.size}")
                    }
                } else {
                    Log.e("ReportesInternosActivity", "Error al cargar categorías: ${response.code()}")
                    Toast.makeText(this@ReportesInternosActivity, "Error al cargar categorías", Toast.LENGTH_SHORT).show()
                    setupCategoriaDropdownFallback()
                }
            }

            override fun onFailure(call: Call<List<CategoriaReporte>>, t: Throwable) {
                Log.e("ReportesInternosActivity", "Error de conexión al cargar categorías", t)
                Toast.makeText(this@ReportesInternosActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                setupCategoriaDropdownFallback()
            }
        })
    }

    private fun setupCategoriaDropdown() {
        val nombresCategoria = categorias.map { it.nombreCategoria }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, nombresCategoria)
        dropdownCategoria.setAdapter(adapter)
    }

    private fun setupCategoriaDropdownFallback() {
        // Categorías por defecto en caso de error
        val categoriasFallback = arrayOf(
            "Mantenimiento",
            "Incidente", 
            "Mejora",
            "Revisión Técnica",
            "Capacitación",
            "Seguridad",
            "Otro"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categoriasFallback)
        dropdownCategoria.setAdapter(adapter)
    }

    private fun setupRecyclerView() {
        adapter = ReportesInternosAdapter(
            reportes = reportes,
            onEditClick = { reporte -> editarReporte(reporte) },
            onDeleteClick = { reporte -> eliminarReporte(reporte) }
        )
        
        recyclerReportes.layoutManager = LinearLayoutManager(this)
        recyclerReportes.adapter = adapter
    }

    private fun setupButtons() {
        // Botón agregar nuevo reporte
        findViewById<MaterialButton>(R.id.add_report_button).setOnClickListener {
            mostrarFormulario()
        }

        btnGuardar.setOnClickListener {
            guardarReporte()
        }

        btnCancelar.setOnClickListener {
            cancelarFormulario()
        }

        // Botón regresar
        findViewById<LinearLayout>(R.id.back_area).setOnClickListener {
            finish()
        }
    }

    private fun cargarReportes() {
        val call = RetrofitInstance.api2kotlin.getReportesInternos("Bearer $authToken")
        
        call.enqueue(object : Callback<List<ReporteInternoResponse>> {
            override fun onResponse(call: Call<List<ReporteInternoResponse>>, response: Response<List<ReporteInternoResponse>>) {
                if (response.isSuccessful) {
                    response.body()?.let { reportesResponse ->
                        reportes.clear()
                        
                        // Convertir ReporteInternoResponse a ReporteInterno
                        val reportesConvertidos = reportesResponse.map { reporteResponse ->
                            val categoria = categorias.find { it.idcategoria == reporteResponse.idCategoriaReportes }?.nombreCategoria ?: reporteResponse.nombreCategoria ?: "Sin categoría"
                            ReporteInterno(
                                id = reporteResponse.idReporte,
                                idCategoriaReportes = reporteResponse.idCategoriaReportes,
                                categoria = categoria,
                                descripcion = reporteResponse.descripcion,
                                fechaCreacion = formatearFecha(reporteResponse.fechaCreacion)
                            )
                        }
                        
                        reportes.addAll(reportesConvertidos)
                        adapter.notifyDataSetChanged()
                        Log.d("ReportesInternosActivity", "Reportes cargados: ${reportes.size}")
                    }
                } else {
                    Log.e("ReportesInternosActivity", "Error al cargar reportes: ${response.code()}")
                    Toast.makeText(this@ReportesInternosActivity, "Error al cargar reportes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ReporteInternoResponse>>, t: Throwable) {
                Log.e("ReportesInternosActivity", "Error de conexión al cargar reportes", t)
                Toast.makeText(this@ReportesInternosActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun formatearFecha(fechaISO: String?): String {
        return try {
            if (fechaISO.isNullOrEmpty()) {
                return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            }
            
            // Formato de entrada del backend (ISO)
            val formatoEntrada = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            // Formato de salida para mostrar
            val formatoSalida = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            
            val fecha = formatoEntrada.parse(fechaISO)
            fecha?.let { formatoSalida.format(it) } ?: fechaISO
        } catch (e: Exception) {
            Log.e("ReportesInternosActivity", "Error al formatear fecha: $fechaISO", e)
            // Si falla el parseo, retornar solo la fecha o fecha actual
            try {
                if (!fechaISO.isNullOrEmpty() && fechaISO.contains("T")) {
                    fechaISO.split("T")[0]
                } else {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                }
            } catch (ex: Exception) {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            }
        }
    }

    private fun mostrarFormulario() {
        reporteEditando = null
        limpiarFormulario()
        cardFormulario.visibility = View.VISIBLE
        btnGuardar.text = "Guardar"
    }

    private fun editarReporte(reporte: ReporteInterno) {
        reporteEditando = reporte
        cardFormulario.visibility = View.VISIBLE
        
        // Llenar el formulario con los datos del reporte
        dropdownCategoria.setText(reporte.categoria, false)
        etDescripcion.setText(reporte.descripcion)
        
        btnGuardar.text = "Actualizar"
    }

    private fun guardarReporte() {
        val categoriaNombre = dropdownCategoria.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()

        if (validarCampos(categoriaNombre, descripcion)) {
            val categoriaSeleccionada = categorias.find { it.nombreCategoria == categoriaNombre }
            
            if (categoriaSeleccionada == null && categorias.isNotEmpty()) {
                Toast.makeText(this, "Seleccione una categoría válida", Toast.LENGTH_SHORT).show()
                return
            }

            val idCategoria = categoriaSeleccionada?.idcategoria ?: 1 // Fallback en caso de error
            val request = ReporteInternoRequest(
                idCategoriaReportes = idCategoria,
                descripcion = descripcion
            )

            if (reporteEditando != null) {
                actualizarReporte(reporteEditando!!.id, request)
            } else {
                crearReporte(request)
            }
        }
    }

    private fun crearReporte(request: ReporteInternoRequest) {
        val call = RetrofitInstance.api2kotlin.crearReporteInterno("Bearer $authToken", request)
        
        call.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(call: Call<SuccessResponse>, response: Response<SuccessResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { successResponse ->
                        Log.d("ReportesInternosActivity", "Reporte creado: ${successResponse.message}")
                        Toast.makeText(this@ReportesInternosActivity, "Reporte creado exitosamente", Toast.LENGTH_SHORT).show()
                        cancelarFormulario()
                        // Recargar la lista completa desde el servidor
                        cargarReportes()
                    }
                } else {
                    Log.e("ReportesInternosActivity", "Error al crear reporte: ${response.code()}")
                    Toast.makeText(this@ReportesInternosActivity, "Error al crear reporte", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                Log.e("ReportesInternosActivity", "Error de conexión al crear reporte", t)
                Toast.makeText(this@ReportesInternosActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun actualizarReporte(id: Int, request: ReporteInternoRequest) {
        val call = RetrofitInstance.api2kotlin.actualizarReporteInterno("Bearer $authToken", id, request)
        
        call.enqueue(object : Callback<SuccessResponse> {
            override fun onResponse(call: Call<SuccessResponse>, response: Response<SuccessResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { successResponse ->
                        Log.d("ReportesInternosActivity", "Reporte actualizado: ${successResponse.message}")
                        Toast.makeText(this@ReportesInternosActivity, "Reporte actualizado exitosamente", Toast.LENGTH_SHORT).show()
                        cancelarFormulario()
                        // Recargar la lista completa desde el servidor
                        cargarReportes()
                    }
                } else {
                    Log.e("ReportesInternosActivity", "Error al actualizar reporte: ${response.code()}")
                    Toast.makeText(this@ReportesInternosActivity, "Error al actualizar reporte", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SuccessResponse>, t: Throwable) {
                Log.e("ReportesInternosActivity", "Error de conexión al actualizar reporte", t)
                Toast.makeText(this@ReportesInternosActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun validarCampos(categoria: String, descripcion: String): Boolean {
        var isValid = true

        if (categoria.isEmpty()) {
            findViewById<TextInputLayout>(R.id.categoria_input_layout).error = "Seleccione una categoría"
            isValid = false
        } else {
            findViewById<TextInputLayout>(R.id.categoria_input_layout).error = null
        }

        if (descripcion.isEmpty()) {
            findViewById<TextInputLayout>(R.id.descripcion_input_layout).error = "Ingrese una descripción"
            isValid = false
        } else if (descripcion.length < 10) {
            findViewById<TextInputLayout>(R.id.descripcion_input_layout).error = "La descripción debe tener al menos 10 caracteres"
            isValid = false
        } else {
            findViewById<TextInputLayout>(R.id.descripcion_input_layout).error = null
        }

        return isValid
    }

    private fun eliminarReporte(reporte: ReporteInterno) {
        // Mostrar confirmación antes de eliminar
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Está seguro que desea eliminar este reporte?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarReporteBackend(reporte)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarReporteBackend(reporte: ReporteInterno) {
        val call = RetrofitInstance.api2kotlin.eliminarReporteInterno("Bearer $authToken", reporte.id)
        
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    adapter.removeReporte(reporte)
                    Toast.makeText(this@ReportesInternosActivity, "Reporte eliminado exitosamente", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("ReportesInternosActivity", "Error al eliminar reporte: ${response.code()}")
                    Toast.makeText(this@ReportesInternosActivity, "Error al eliminar reporte", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("ReportesInternosActivity", "Error de conexión al eliminar reporte", t)
                Toast.makeText(this@ReportesInternosActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cancelarFormulario() {
        cardFormulario.visibility = View.GONE
        limpiarFormulario()
        reporteEditando = null
    }

    private fun limpiarFormulario() {
        dropdownCategoria.text.clear()
        etDescripcion.text?.clear()
        findViewById<TextInputLayout>(R.id.categoria_input_layout).error = null
        findViewById<TextInputLayout>(R.id.descripcion_input_layout).error = null
    }
}