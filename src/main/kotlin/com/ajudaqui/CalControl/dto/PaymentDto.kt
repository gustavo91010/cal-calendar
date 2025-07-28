package com.ajudaqui.CalControl.dto

import jakarta.validation.constraints.*
import java.math.BigDecimal

data class PaymentDto(
        @field:Email(message = "E-mail inválido") val email: String,
        @field:Pattern(
                regexp = "\\d{4}-\\d{2}-\\d{2}",
                message = "O campo day deve estar no formato yyyy-MM-dd"
        )
        val day: String,
        val paymentData: List<PaymentData>
)

data class PaymentData(
        val id: Long,
        val description: String,
        val value: BigDecimal,
        val status: String,
        val link: String
)
