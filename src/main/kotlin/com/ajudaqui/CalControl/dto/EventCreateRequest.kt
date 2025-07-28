package com.ajudaqui.CalControl.dto

import jakarta.validation.constraints.*

data class EventCreateRequest(
        @field:NotBlank(message = "Resumo é obrigatório") val summary: String,
        @field:NotBlank(message = "Descrição é obrigatório") val description: String,
        val location: String? = null,
        @field:NotNull(message = "O periodo de inicio do evento  é obrigatório")
        val start: EventCreateDateTime,
        @field:NotNull(message = "O periodo de encerramento do evento  é obrigatório")
        val end: EventCreateDateTime
) {
  constructor(
          summary: String,
          description: String,
          day: String
  ) : this(
          summary = summary,
          description = description,
          location = null,
          start = EventCreateDateTime(dateTime = "${day}T00:00:00-03:00"),
          end = EventCreateDateTime(dateTime = "${day}T23:59:59-03:00")
  ) {
    require(Regex("\\d{4}-\\d{2}-\\d{2}").matches(day)) {
      "O campo day deve estar no formato yyyy-MM-dd"
    }
  }
}

data class EventCreateDateTime(
        val date: String? = null,
        val dateTime: String? = null,
        val timeZone: String? =  "America/Recife"
)
