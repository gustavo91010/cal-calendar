package com.ajudaqui.CalControl.dto

import jakarta.validation.constraints.*


data class EventCreateRequest(
    @field:NotBlank(message = "Resumo é obrigatório")
    val summary: String,
    @field:NotBlank(message = "Descrição é obrigatório")
    val description: String,
    val location: String? = null,
    @field:NotNull(message = "O periodo de inicio do evento  é obrigatório")
    val start: EventCreateDateTime,
    @field:NotNull(message = "O periodo de encerramento do evento  é obrigatório")
    val end: EventCreateDateTime
)

data class EventCreateDateTime(
    val date: String? = null,
    val dateTime: String? = null,
    val timeZone: String? = null
)


