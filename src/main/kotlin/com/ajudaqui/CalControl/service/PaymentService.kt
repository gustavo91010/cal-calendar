package com.ajudaqui.CalControl.service

import com.ajudaqui.CalControl.dto.EventCreateRequest
import com.ajudaqui.CalControl.dto.PaymentDto
import com.ajudaqui.CalControl.entity.Events
import java.io.File
import org.springframework.stereotype.Service

@Service
class PaymentService(private val eventService: EventsService) {

  fun create(email: String, paymentDto: PaymentDto): Events {
    val eventCreate =
            EventCreateRequest(
                    summary = "Contas a pagar",
                    description = factorDescription(paymentDto),
                    day = paymentDto.day
            )
    return eventService.create(email, eventCreate)
  }

  private fun factorDescription(paymentDto: PaymentDto): String {
    val template = File("payment.html").readText()

    val blocos =
            paymentDto.paymentData.joinToString("\n") { (id, description, value, status, link) ->
              """
    <div>
      <span>($status) </span>
      <strong>$description:</strong> R$ ${value.toString()}
      <a href="$link/$id" target="_blank"
         style="display:inline-block; padding:3px 8px; background:#4CAF50; color:#fff;
         text-decoration:none; border-radius:5px; margin-left:10px;">
        Pago
      </a>
    </div>
    """.trimIndent()
            }

    return template.replace("<!-- Lista de contas -->", blocos)
  }
}
