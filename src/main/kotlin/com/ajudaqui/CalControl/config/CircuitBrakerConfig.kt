package com.ajudaqui.CalControl.config

import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Service
class CircuitBrakerConfig {
  @Bean fun mongoBreaker(factory: CircuitBreakerFactory<*, *>) = factory.create("mongoService")

  @Bean fun googleBreaker(factory: CircuitBreakerFactory<*, *>) = factory.create("googleApiService")

  // private val breaker = factory.create("mongoService")

  // fun <T> run(action: () -> T, fallback: () -> T): T {
  //   return breaker.run(action) { fallback() }
  // }
}
