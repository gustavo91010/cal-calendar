package com.ajudaqui.CalControl.config

import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Order(1)
@Component
class ServiceKeyAuthFilter : Filter {
  @Value("\${api.secret.key.bill.manager}") private lateinit var secretKey: String

  override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
    val httpRequest = request as HttpServletRequest
    val httpRespose = response as HttpServletResponse

    val authHeader = httpRequest.getHeader("Authorization")

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      httpRespose.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization ausente")
      return
    }
    val apiKey = authHeader.substring(7)
    if (apiKey != secretKey) {
      httpRespose.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Chave inválida")
      return
    }
    chain.doFilter(request, response)
  }
}
