package com.ajudaqui.CalControl.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

data class MessageDTO(
    @field:Email(message = "E-mail inválido") val email: String,
    @field:Pattern(
        regexp = "\\d{4}-\\d{2}-\\d{2}",
        message = "O campo day deve estar no formato yyyy-MM-dd",
    )
    val day: String,
    val application: String,
    val type: String,
    val payload: Map<String, String>,
)
