package com.ajudaqui.CalControl.config

import java.time.LocalDateTime
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.info.Info
import org.springframework.boot.actuate.info.InfoContributor
import org.springframework.stereotype.Component

@Component
class CustomInfoContributor(
        @Value("\${info.environment}") private val environment: String,
        @Value("\${info.description}") private val description: String,
        @Value("\${spring.application.name}") private val aplicationName: String,
        @Value("\${info.version}") private val version: String
) : InfoContributor {
  override fun contribute(builder: Info.Builder) {

    builder.withDetail(
                    "app",
                    mapOf(
                            "name" to aplicationName,
                            "description" to description,
                            "version" to version
                    )
            )
            .withDetail("developer", mapOf("name" to "Gustavo", "email" to "gustavo@email.com"))
            .withDetail("environment", environment)
            .withDetail("buildTime", LocalDateTime.now().toString())
            .withDetail("environment", environment)
            .withDetail("features", listOf("create-event", "delete-event", "list-events"))
  }
}
