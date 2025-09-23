package com.example.springboot_sena_kotlin1.Config

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException

@Component
class JwtAuthenticationFilter(
    // mantenemos la dependencia para cuando se reactive, pero no la usamos ahora
    private val utilJWT: UtilJWT
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // No-op: no validamos aquí mientras debuggeamos
        log.debug("JwtAuthenticationFilter no-op activo (debug). Path: ${request.servletPath}")
        filterChain.doFilter(request, response)
    }
}
