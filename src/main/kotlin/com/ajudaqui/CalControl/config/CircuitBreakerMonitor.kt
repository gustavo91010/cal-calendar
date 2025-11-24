package com.ajudaqui.CalControl.config

import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent
import jakarta.annotation.PostConstruct
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.stereotype.Component

@Component
class CircuitBreakerMonitor(private val factory: Resilience4JCircuitBreakerFactory) {

  @PostConstruct
  fun init() {
    val registry = factory.circuitBreakerRegistry

    registry.allCircuitBreakers.forEach { cb ->
      cb.eventPublisher.onStateTransition { event ->
        val transition = (event as CircuitBreakerOnStateTransitionEvent).stateTransition

        println("[CB: ${cb.name}] → $transition")

        when (transition.name) {
          "CLOSED_TO_OPEN" -> println("❌ Serviço caiu! Circuito aberto.")
          "OPEN_TO_HALF_OPEN" -> println("⚠️ Tentando recuperar...")
          "HALF_OPEN_TO_CLOSED" -> println("✅ Recuperado! Pode reenviar pendências.")
        }
      }
    }
  }
}
