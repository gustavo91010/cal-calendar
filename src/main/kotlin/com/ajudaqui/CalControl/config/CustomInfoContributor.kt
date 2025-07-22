package com.ajudaqui.CalControl.config


import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.info.Info
import org.springframework.boot.actuate.info.InfoContributor
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CustomInfoContributor(
        @Value("\${info.environment}") private val environment: String,
        @Value("\${info.description}") private val description: String,
        @Value("\${spring.application.name}") private val aplicationName: String,
        @Value("\${info.version}") private val version: String
) : InfoContributor {
    val buildTime= LocalDateTime.now().toString()
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
            .withDetail("buildTime", buildTime)
            .withDetail("environment", environment)
            .withDetail("features", listOf("create-event", "delete-event", "list-events"))
  }
}
